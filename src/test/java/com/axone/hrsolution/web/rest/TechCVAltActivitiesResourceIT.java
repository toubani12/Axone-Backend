package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVAltActivitiesAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVAltActivities;
import com.axone.hrsolution.repository.TechCVAltActivitiesRepository;
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
 * Integration tests for the {@link TechCVAltActivitiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVAltActivitiesResourceIT {

    private static final String DEFAULT_ACTIVITIES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITIES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-alt-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVAltActivitiesRepository techCVAltActivitiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVAltActivitiesMockMvc;

    private TechCVAltActivities techCVAltActivities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVAltActivities createEntity(EntityManager em) {
        TechCVAltActivities techCVAltActivities = new TechCVAltActivities().activities(DEFAULT_ACTIVITIES);
        return techCVAltActivities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVAltActivities createUpdatedEntity(EntityManager em) {
        TechCVAltActivities techCVAltActivities = new TechCVAltActivities().activities(UPDATED_ACTIVITIES);
        return techCVAltActivities;
    }

    @BeforeEach
    public void initTest() {
        techCVAltActivities = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVAltActivities() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVAltActivities
        var returnedTechCVAltActivities = om.readValue(
            restTechCVAltActivitiesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAltActivities)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVAltActivities.class
        );

        // Validate the TechCVAltActivities in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVAltActivitiesUpdatableFieldsEquals(
            returnedTechCVAltActivities,
            getPersistedTechCVAltActivities(returnedTechCVAltActivities)
        );
    }

    @Test
    @Transactional
    void createTechCVAltActivitiesWithExistingId() throws Exception {
        // Create the TechCVAltActivities with an existing ID
        techCVAltActivities.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVAltActivitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAltActivities)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActivitiesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVAltActivities.setActivities(null);

        // Create the TechCVAltActivities, which fails.

        restTechCVAltActivitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAltActivities)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVAltActivities() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        // Get all the techCVAltActivitiesList
        restTechCVAltActivitiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVAltActivities.getId().intValue())))
            .andExpect(jsonPath("$.[*].activities").value(hasItem(DEFAULT_ACTIVITIES)));
    }

    @Test
    @Transactional
    void getTechCVAltActivities() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        // Get the techCVAltActivities
        restTechCVAltActivitiesMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVAltActivities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVAltActivities.getId().intValue()))
            .andExpect(jsonPath("$.activities").value(DEFAULT_ACTIVITIES));
    }

    @Test
    @Transactional
    void getNonExistingTechCVAltActivities() throws Exception {
        // Get the techCVAltActivities
        restTechCVAltActivitiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVAltActivities() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAltActivities
        TechCVAltActivities updatedTechCVAltActivities = techCVAltActivitiesRepository.findById(techCVAltActivities.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVAltActivities are not directly saved in db
        em.detach(updatedTechCVAltActivities);
        updatedTechCVAltActivities.activities(UPDATED_ACTIVITIES);

        restTechCVAltActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVAltActivities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVAltActivities))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVAltActivitiesToMatchAllProperties(updatedTechCVAltActivities);
    }

    @Test
    @Transactional
    void putNonExistingTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVAltActivities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVAltActivities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVAltActivities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAltActivities)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVAltActivitiesWithPatch() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAltActivities using partial update
        TechCVAltActivities partialUpdatedTechCVAltActivities = new TechCVAltActivities();
        partialUpdatedTechCVAltActivities.setId(techCVAltActivities.getId());

        partialUpdatedTechCVAltActivities.activities(UPDATED_ACTIVITIES);

        restTechCVAltActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVAltActivities.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVAltActivities))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAltActivities in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVAltActivitiesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVAltActivities, techCVAltActivities),
            getPersistedTechCVAltActivities(techCVAltActivities)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVAltActivitiesWithPatch() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAltActivities using partial update
        TechCVAltActivities partialUpdatedTechCVAltActivities = new TechCVAltActivities();
        partialUpdatedTechCVAltActivities.setId(techCVAltActivities.getId());

        partialUpdatedTechCVAltActivities.activities(UPDATED_ACTIVITIES);

        restTechCVAltActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVAltActivities.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVAltActivities))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAltActivities in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVAltActivitiesUpdatableFieldsEquals(
            partialUpdatedTechCVAltActivities,
            getPersistedTechCVAltActivities(partialUpdatedTechCVAltActivities)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVAltActivities.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVAltActivities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVAltActivities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVAltActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAltActivities.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAltActivitiesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVAltActivities)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVAltActivities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVAltActivities() throws Exception {
        // Initialize the database
        techCVAltActivitiesRepository.saveAndFlush(techCVAltActivities);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVAltActivities
        restTechCVAltActivitiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVAltActivities.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVAltActivitiesRepository.count();
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

    protected TechCVAltActivities getPersistedTechCVAltActivities(TechCVAltActivities techCVAltActivities) {
        return techCVAltActivitiesRepository.findById(techCVAltActivities.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVAltActivitiesToMatchAllProperties(TechCVAltActivities expectedTechCVAltActivities) {
        assertTechCVAltActivitiesAllPropertiesEquals(
            expectedTechCVAltActivities,
            getPersistedTechCVAltActivities(expectedTechCVAltActivities)
        );
    }

    protected void assertPersistedTechCVAltActivitiesToMatchUpdatableProperties(TechCVAltActivities expectedTechCVAltActivities) {
        assertTechCVAltActivitiesAllUpdatablePropertiesEquals(
            expectedTechCVAltActivities,
            getPersistedTechCVAltActivities(expectedTechCVAltActivities)
        );
    }
}
