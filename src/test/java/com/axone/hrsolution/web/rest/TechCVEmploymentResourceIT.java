package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVEmploymentAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVEmployment;
import com.axone.hrsolution.repository.TechCVEmploymentRepository;
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
 * Integration tests for the {@link TechCVEmploymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVEmploymentResourceIT {

    private static final String DEFAULT_EMPLOYMENT = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-employments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVEmploymentRepository techCVEmploymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVEmploymentMockMvc;

    private TechCVEmployment techCVEmployment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVEmployment createEntity(EntityManager em) {
        TechCVEmployment techCVEmployment = new TechCVEmployment().employment(DEFAULT_EMPLOYMENT);
        return techCVEmployment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVEmployment createUpdatedEntity(EntityManager em) {
        TechCVEmployment techCVEmployment = new TechCVEmployment().employment(UPDATED_EMPLOYMENT);
        return techCVEmployment;
    }

    @BeforeEach
    public void initTest() {
        techCVEmployment = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVEmployment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVEmployment
        var returnedTechCVEmployment = om.readValue(
            restTechCVEmploymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEmployment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVEmployment.class
        );

        // Validate the TechCVEmployment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVEmploymentUpdatableFieldsEquals(returnedTechCVEmployment, getPersistedTechCVEmployment(returnedTechCVEmployment));
    }

    @Test
    @Transactional
    void createTechCVEmploymentWithExistingId() throws Exception {
        // Create the TechCVEmployment with an existing ID
        techCVEmployment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEmployment)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmploymentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVEmployment.setEmployment(null);

        // Create the TechCVEmployment, which fails.

        restTechCVEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEmployment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVEmployments() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        // Get all the techCVEmploymentList
        restTechCVEmploymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVEmployment.getId().intValue())))
            .andExpect(jsonPath("$.[*].employment").value(hasItem(DEFAULT_EMPLOYMENT)));
    }

    @Test
    @Transactional
    void getTechCVEmployment() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        // Get the techCVEmployment
        restTechCVEmploymentMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVEmployment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVEmployment.getId().intValue()))
            .andExpect(jsonPath("$.employment").value(DEFAULT_EMPLOYMENT));
    }

    @Test
    @Transactional
    void getNonExistingTechCVEmployment() throws Exception {
        // Get the techCVEmployment
        restTechCVEmploymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVEmployment() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEmployment
        TechCVEmployment updatedTechCVEmployment = techCVEmploymentRepository.findById(techCVEmployment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVEmployment are not directly saved in db
        em.detach(updatedTechCVEmployment);
        updatedTechCVEmployment.employment(UPDATED_EMPLOYMENT);

        restTechCVEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVEmployment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVEmployment))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVEmploymentToMatchAllProperties(updatedTechCVEmployment);
    }

    @Test
    @Transactional
    void putNonExistingTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVEmployment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVEmployment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVEmployment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEmployment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVEmploymentWithPatch() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEmployment using partial update
        TechCVEmployment partialUpdatedTechCVEmployment = new TechCVEmployment();
        partialUpdatedTechCVEmployment.setId(techCVEmployment.getId());

        restTechCVEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVEmployment))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEmployment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVEmploymentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVEmployment, techCVEmployment),
            getPersistedTechCVEmployment(techCVEmployment)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVEmploymentWithPatch() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEmployment using partial update
        TechCVEmployment partialUpdatedTechCVEmployment = new TechCVEmployment();
        partialUpdatedTechCVEmployment.setId(techCVEmployment.getId());

        partialUpdatedTechCVEmployment.employment(UPDATED_EMPLOYMENT);

        restTechCVEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVEmployment))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEmployment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVEmploymentUpdatableFieldsEquals(
            partialUpdatedTechCVEmployment,
            getPersistedTechCVEmployment(partialUpdatedTechCVEmployment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVEmployment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVEmployment))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVEmployment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEmployment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEmploymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVEmployment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVEmployment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVEmployment() throws Exception {
        // Initialize the database
        techCVEmploymentRepository.saveAndFlush(techCVEmployment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVEmployment
        restTechCVEmploymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVEmployment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVEmploymentRepository.count();
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

    protected TechCVEmployment getPersistedTechCVEmployment(TechCVEmployment techCVEmployment) {
        return techCVEmploymentRepository.findById(techCVEmployment.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVEmploymentToMatchAllProperties(TechCVEmployment expectedTechCVEmployment) {
        assertTechCVEmploymentAllPropertiesEquals(expectedTechCVEmployment, getPersistedTechCVEmployment(expectedTechCVEmployment));
    }

    protected void assertPersistedTechCVEmploymentToMatchUpdatableProperties(TechCVEmployment expectedTechCVEmployment) {
        assertTechCVEmploymentAllUpdatablePropertiesEquals(
            expectedTechCVEmployment,
            getPersistedTechCVEmployment(expectedTechCVEmployment)
        );
    }
}
