package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVEducationAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVEducation;
import com.axone.hrsolution.repository.TechCVEducationRepository;
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
 * Integration tests for the {@link TechCVEducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVEducationResourceIT {

    private static final String DEFAULT_EDUCATION = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVEducationRepository techCVEducationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVEducationMockMvc;

    private TechCVEducation techCVEducation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVEducation createEntity(EntityManager em) {
        TechCVEducation techCVEducation = new TechCVEducation().education(DEFAULT_EDUCATION);
        return techCVEducation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVEducation createUpdatedEntity(EntityManager em) {
        TechCVEducation techCVEducation = new TechCVEducation().education(UPDATED_EDUCATION);
        return techCVEducation;
    }

    @BeforeEach
    public void initTest() {
        techCVEducation = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVEducation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVEducation
        var returnedTechCVEducation = om.readValue(
            restTechCVEducationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEducation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVEducation.class
        );

        // Validate the TechCVEducation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVEducationUpdatableFieldsEquals(returnedTechCVEducation, getPersistedTechCVEducation(returnedTechCVEducation));
    }

    @Test
    @Transactional
    void createTechCVEducationWithExistingId() throws Exception {
        // Create the TechCVEducation with an existing ID
        techCVEducation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEducation)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEducationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVEducation.setEducation(null);

        // Create the TechCVEducation, which fails.

        restTechCVEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEducation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVEducations() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        // Get all the techCVEducationList
        restTechCVEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVEducation.getId().intValue())))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION)));
    }

    @Test
    @Transactional
    void getTechCVEducation() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        // Get the techCVEducation
        restTechCVEducationMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVEducation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVEducation.getId().intValue()))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION));
    }

    @Test
    @Transactional
    void getNonExistingTechCVEducation() throws Exception {
        // Get the techCVEducation
        restTechCVEducationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVEducation() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEducation
        TechCVEducation updatedTechCVEducation = techCVEducationRepository.findById(techCVEducation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVEducation are not directly saved in db
        em.detach(updatedTechCVEducation);
        updatedTechCVEducation.education(UPDATED_EDUCATION);

        restTechCVEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVEducation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVEducation))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVEducationToMatchAllProperties(updatedTechCVEducation);
    }

    @Test
    @Transactional
    void putNonExistingTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVEducation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVEducation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVEducation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVEducation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVEducationWithPatch() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEducation using partial update
        TechCVEducation partialUpdatedTechCVEducation = new TechCVEducation();
        partialUpdatedTechCVEducation.setId(techCVEducation.getId());

        restTechCVEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVEducation))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEducation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVEducationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVEducation, techCVEducation),
            getPersistedTechCVEducation(techCVEducation)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVEducationWithPatch() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVEducation using partial update
        TechCVEducation partialUpdatedTechCVEducation = new TechCVEducation();
        partialUpdatedTechCVEducation.setId(techCVEducation.getId());

        partialUpdatedTechCVEducation.education(UPDATED_EDUCATION);

        restTechCVEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVEducation))
            )
            .andExpect(status().isOk());

        // Validate the TechCVEducation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVEducationUpdatableFieldsEquals(
            partialUpdatedTechCVEducation,
            getPersistedTechCVEducation(partialUpdatedTechCVEducation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVEducation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVEducation))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVEducation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVEducation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVEducationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVEducation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVEducation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVEducation() throws Exception {
        // Initialize the database
        techCVEducationRepository.saveAndFlush(techCVEducation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVEducation
        restTechCVEducationMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVEducation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVEducationRepository.count();
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

    protected TechCVEducation getPersistedTechCVEducation(TechCVEducation techCVEducation) {
        return techCVEducationRepository.findById(techCVEducation.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVEducationToMatchAllProperties(TechCVEducation expectedTechCVEducation) {
        assertTechCVEducationAllPropertiesEquals(expectedTechCVEducation, getPersistedTechCVEducation(expectedTechCVEducation));
    }

    protected void assertPersistedTechCVEducationToMatchUpdatableProperties(TechCVEducation expectedTechCVEducation) {
        assertTechCVEducationAllUpdatablePropertiesEquals(expectedTechCVEducation, getPersistedTechCVEducation(expectedTechCVEducation));
    }
}
