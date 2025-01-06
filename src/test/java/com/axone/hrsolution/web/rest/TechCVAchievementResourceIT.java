package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVAchievementAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVAchievement;
import com.axone.hrsolution.repository.TechCVAchievementRepository;
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
 * Integration tests for the {@link TechCVAchievementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVAchievementResourceIT {

    private static final String DEFAULT_ACHIEVEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ACHIEVEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-achievements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVAchievementRepository techCVAchievementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVAchievementMockMvc;

    private TechCVAchievement techCVAchievement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVAchievement createEntity(EntityManager em) {
        TechCVAchievement techCVAchievement = new TechCVAchievement().achievement(DEFAULT_ACHIEVEMENT);
        return techCVAchievement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVAchievement createUpdatedEntity(EntityManager em) {
        TechCVAchievement techCVAchievement = new TechCVAchievement().achievement(UPDATED_ACHIEVEMENT);
        return techCVAchievement;
    }

    @BeforeEach
    public void initTest() {
        techCVAchievement = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVAchievement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVAchievement
        var returnedTechCVAchievement = om.readValue(
            restTechCVAchievementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAchievement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVAchievement.class
        );

        // Validate the TechCVAchievement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVAchievementUpdatableFieldsEquals(returnedTechCVAchievement, getPersistedTechCVAchievement(returnedTechCVAchievement));
    }

    @Test
    @Transactional
    void createTechCVAchievementWithExistingId() throws Exception {
        // Create the TechCVAchievement with an existing ID
        techCVAchievement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAchievement)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAchievementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVAchievement.setAchievement(null);

        // Create the TechCVAchievement, which fails.

        restTechCVAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAchievement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVAchievements() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        // Get all the techCVAchievementList
        restTechCVAchievementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVAchievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievement").value(hasItem(DEFAULT_ACHIEVEMENT)));
    }

    @Test
    @Transactional
    void getTechCVAchievement() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        // Get the techCVAchievement
        restTechCVAchievementMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVAchievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVAchievement.getId().intValue()))
            .andExpect(jsonPath("$.achievement").value(DEFAULT_ACHIEVEMENT));
    }

    @Test
    @Transactional
    void getNonExistingTechCVAchievement() throws Exception {
        // Get the techCVAchievement
        restTechCVAchievementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVAchievement() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAchievement
        TechCVAchievement updatedTechCVAchievement = techCVAchievementRepository.findById(techCVAchievement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVAchievement are not directly saved in db
        em.detach(updatedTechCVAchievement);
        updatedTechCVAchievement.achievement(UPDATED_ACHIEVEMENT);

        restTechCVAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVAchievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVAchievement))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVAchievementToMatchAllProperties(updatedTechCVAchievement);
    }

    @Test
    @Transactional
    void putNonExistingTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVAchievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVAchievement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVAchievementWithPatch() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAchievement using partial update
        TechCVAchievement partialUpdatedTechCVAchievement = new TechCVAchievement();
        partialUpdatedTechCVAchievement.setId(techCVAchievement.getId());

        restTechCVAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVAchievement))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAchievement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVAchievementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVAchievement, techCVAchievement),
            getPersistedTechCVAchievement(techCVAchievement)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVAchievementWithPatch() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVAchievement using partial update
        TechCVAchievement partialUpdatedTechCVAchievement = new TechCVAchievement();
        partialUpdatedTechCVAchievement.setId(techCVAchievement.getId());

        partialUpdatedTechCVAchievement.achievement(UPDATED_ACHIEVEMENT);

        restTechCVAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVAchievement))
            )
            .andExpect(status().isOk());

        // Validate the TechCVAchievement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVAchievementUpdatableFieldsEquals(
            partialUpdatedTechCVAchievement,
            getPersistedTechCVAchievement(partialUpdatedTechCVAchievement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVAchievement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVAchievementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVAchievement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVAchievement() throws Exception {
        // Initialize the database
        techCVAchievementRepository.saveAndFlush(techCVAchievement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVAchievement
        restTechCVAchievementMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVAchievement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVAchievementRepository.count();
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

    protected TechCVAchievement getPersistedTechCVAchievement(TechCVAchievement techCVAchievement) {
        return techCVAchievementRepository.findById(techCVAchievement.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVAchievementToMatchAllProperties(TechCVAchievement expectedTechCVAchievement) {
        assertTechCVAchievementAllPropertiesEquals(expectedTechCVAchievement, getPersistedTechCVAchievement(expectedTechCVAchievement));
    }

    protected void assertPersistedTechCVAchievementToMatchUpdatableProperties(TechCVAchievement expectedTechCVAchievement) {
        assertTechCVAchievementAllUpdatablePropertiesEquals(
            expectedTechCVAchievement,
            getPersistedTechCVAchievement(expectedTechCVAchievement)
        );
    }
}
