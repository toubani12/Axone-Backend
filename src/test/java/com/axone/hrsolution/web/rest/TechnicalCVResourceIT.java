package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechnicalCVAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechnicalCV;
import com.axone.hrsolution.domain.enumeration.TechCVLevel;
import com.axone.hrsolution.repository.TechnicalCVRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TechnicalCVResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechnicalCVResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TechCVLevel DEFAULT_LEVEL = TechCVLevel.BEGINNER;
    private static final TechCVLevel UPDATED_LEVEL = TechCVLevel.INTERMEDIATE;

    private static final String DEFAULT_PROFILE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/technical-cvs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechnicalCVRepository technicalCVRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechnicalCVMockMvc;

    private TechnicalCV technicalCV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechnicalCV createEntity(EntityManager em) {
        TechnicalCV technicalCV = new TechnicalCV().name(DEFAULT_NAME).level(DEFAULT_LEVEL).profileDescription(DEFAULT_PROFILE_DESCRIPTION);
        return technicalCV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechnicalCV createUpdatedEntity(EntityManager em) {
        TechnicalCV technicalCV = new TechnicalCV().name(UPDATED_NAME).level(UPDATED_LEVEL).profileDescription(UPDATED_PROFILE_DESCRIPTION);
        return technicalCV;
    }

    @BeforeEach
    public void initTest() {
        technicalCV = createEntity(em);
    }

    @Test
    @Transactional
    void createTechnicalCV() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechnicalCV
        var returnedTechnicalCV = om.readValue(
            restTechnicalCVMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technicalCV)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechnicalCV.class
        );

        // Validate the TechnicalCV in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechnicalCVUpdatableFieldsEquals(returnedTechnicalCV, getPersistedTechnicalCV(returnedTechnicalCV));
    }

    @Test
    @Transactional
    void createTechnicalCVWithExistingId() throws Exception {
        // Create the TechnicalCV with an existing ID
        technicalCV.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnicalCVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technicalCV)))
            .andExpect(status().isBadRequest());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        technicalCV.setName(null);

        // Create the TechnicalCV, which fails.

        restTechnicalCVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technicalCV)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        technicalCV.setLevel(null);

        // Create the TechnicalCV, which fails.

        restTechnicalCVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technicalCV)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechnicalCVS() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        // Get all the technicalCVList
        restTechnicalCVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technicalCV.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].profileDescription").value(hasItem(DEFAULT_PROFILE_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTechnicalCV() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        // Get the technicalCV
        restTechnicalCVMockMvc
            .perform(get(ENTITY_API_URL_ID, technicalCV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(technicalCV.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.profileDescription").value(DEFAULT_PROFILE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTechnicalCV() throws Exception {
        // Get the technicalCV
        restTechnicalCVMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechnicalCV() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the technicalCV
        TechnicalCV updatedTechnicalCV = technicalCVRepository.findById(technicalCV.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechnicalCV are not directly saved in db
        em.detach(updatedTechnicalCV);
        updatedTechnicalCV.name(UPDATED_NAME).level(UPDATED_LEVEL).profileDescription(UPDATED_PROFILE_DESCRIPTION);

        restTechnicalCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechnicalCV.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechnicalCV))
            )
            .andExpect(status().isOk());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechnicalCVToMatchAllProperties(updatedTechnicalCV);
    }

    @Test
    @Transactional
    void putNonExistingTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, technicalCV.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(technicalCV))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(technicalCV))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technicalCV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechnicalCVWithPatch() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the technicalCV using partial update
        TechnicalCV partialUpdatedTechnicalCV = new TechnicalCV();
        partialUpdatedTechnicalCV.setId(technicalCV.getId());

        partialUpdatedTechnicalCV.profileDescription(UPDATED_PROFILE_DESCRIPTION);

        restTechnicalCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnicalCV.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechnicalCV))
            )
            .andExpect(status().isOk());

        // Validate the TechnicalCV in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechnicalCVUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechnicalCV, technicalCV),
            getPersistedTechnicalCV(technicalCV)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechnicalCVWithPatch() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the technicalCV using partial update
        TechnicalCV partialUpdatedTechnicalCV = new TechnicalCV();
        partialUpdatedTechnicalCV.setId(technicalCV.getId());

        partialUpdatedTechnicalCV.name(UPDATED_NAME).level(UPDATED_LEVEL).profileDescription(UPDATED_PROFILE_DESCRIPTION);

        restTechnicalCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnicalCV.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechnicalCV))
            )
            .andExpect(status().isOk());

        // Validate the TechnicalCV in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechnicalCVUpdatableFieldsEquals(partialUpdatedTechnicalCV, getPersistedTechnicalCV(partialUpdatedTechnicalCV));
    }

    @Test
    @Transactional
    void patchNonExistingTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, technicalCV.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(technicalCV))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(technicalCV))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechnicalCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        technicalCV.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicalCVMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(technicalCV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechnicalCV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechnicalCV() throws Exception {
        // Initialize the database
        technicalCVRepository.saveAndFlush(technicalCV);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the technicalCV
        restTechnicalCVMockMvc
            .perform(delete(ENTITY_API_URL_ID, technicalCV.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return technicalCVRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TechnicalCV getPersistedTechnicalCV(TechnicalCV technicalCV) {
        return technicalCVRepository.findById(technicalCV.getId()).orElseThrow();
    }

    protected void assertPersistedTechnicalCVToMatchAllProperties(TechnicalCV expectedTechnicalCV) {
        assertTechnicalCVAllPropertiesEquals(expectedTechnicalCV, getPersistedTechnicalCV(expectedTechnicalCV));
    }

    protected void assertPersistedTechnicalCVToMatchUpdatableProperties(TechnicalCV expectedTechnicalCV) {
        assertTechnicalCVAllUpdatablePropertiesEquals(expectedTechnicalCV, getPersistedTechnicalCV(expectedTechnicalCV));
    }
}
