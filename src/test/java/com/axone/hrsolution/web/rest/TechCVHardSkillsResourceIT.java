package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVHardSkillsAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVHardSkills;
import com.axone.hrsolution.repository.TechCVHardSkillsRepository;
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
 * Integration tests for the {@link TechCVHardSkillsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVHardSkillsResourceIT {

    private static final String DEFAULT_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_SKILLS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-hard-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVHardSkillsRepository techCVHardSkillsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVHardSkillsMockMvc;

    private TechCVHardSkills techCVHardSkills;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVHardSkills createEntity(EntityManager em) {
        TechCVHardSkills techCVHardSkills = new TechCVHardSkills().skills(DEFAULT_SKILLS);
        return techCVHardSkills;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVHardSkills createUpdatedEntity(EntityManager em) {
        TechCVHardSkills techCVHardSkills = new TechCVHardSkills().skills(UPDATED_SKILLS);
        return techCVHardSkills;
    }

    @BeforeEach
    public void initTest() {
        techCVHardSkills = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVHardSkills() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVHardSkills
        var returnedTechCVHardSkills = om.readValue(
            restTechCVHardSkillsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVHardSkills)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVHardSkills.class
        );

        // Validate the TechCVHardSkills in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVHardSkillsUpdatableFieldsEquals(returnedTechCVHardSkills, getPersistedTechCVHardSkills(returnedTechCVHardSkills));
    }

    @Test
    @Transactional
    void createTechCVHardSkillsWithExistingId() throws Exception {
        // Create the TechCVHardSkills with an existing ID
        techCVHardSkills.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVHardSkillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVHardSkills)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkillsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVHardSkills.setSkills(null);

        // Create the TechCVHardSkills, which fails.

        restTechCVHardSkillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVHardSkills)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVHardSkills() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        // Get all the techCVHardSkillsList
        restTechCVHardSkillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVHardSkills.getId().intValue())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)));
    }

    @Test
    @Transactional
    void getTechCVHardSkills() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        // Get the techCVHardSkills
        restTechCVHardSkillsMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVHardSkills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVHardSkills.getId().intValue()))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS));
    }

    @Test
    @Transactional
    void getNonExistingTechCVHardSkills() throws Exception {
        // Get the techCVHardSkills
        restTechCVHardSkillsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVHardSkills() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVHardSkills
        TechCVHardSkills updatedTechCVHardSkills = techCVHardSkillsRepository.findById(techCVHardSkills.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVHardSkills are not directly saved in db
        em.detach(updatedTechCVHardSkills);
        updatedTechCVHardSkills.skills(UPDATED_SKILLS);

        restTechCVHardSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVHardSkills.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVHardSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVHardSkillsToMatchAllProperties(updatedTechCVHardSkills);
    }

    @Test
    @Transactional
    void putNonExistingTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVHardSkills.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVHardSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVHardSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVHardSkills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVHardSkillsWithPatch() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVHardSkills using partial update
        TechCVHardSkills partialUpdatedTechCVHardSkills = new TechCVHardSkills();
        partialUpdatedTechCVHardSkills.setId(techCVHardSkills.getId());

        partialUpdatedTechCVHardSkills.skills(UPDATED_SKILLS);

        restTechCVHardSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVHardSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVHardSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVHardSkills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVHardSkillsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVHardSkills, techCVHardSkills),
            getPersistedTechCVHardSkills(techCVHardSkills)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVHardSkillsWithPatch() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVHardSkills using partial update
        TechCVHardSkills partialUpdatedTechCVHardSkills = new TechCVHardSkills();
        partialUpdatedTechCVHardSkills.setId(techCVHardSkills.getId());

        partialUpdatedTechCVHardSkills.skills(UPDATED_SKILLS);

        restTechCVHardSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVHardSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVHardSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVHardSkills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVHardSkillsUpdatableFieldsEquals(
            partialUpdatedTechCVHardSkills,
            getPersistedTechCVHardSkills(partialUpdatedTechCVHardSkills)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVHardSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVHardSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVHardSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVHardSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVHardSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVHardSkillsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVHardSkills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVHardSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVHardSkills() throws Exception {
        // Initialize the database
        techCVHardSkillsRepository.saveAndFlush(techCVHardSkills);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVHardSkills
        restTechCVHardSkillsMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVHardSkills.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVHardSkillsRepository.count();
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

    protected TechCVHardSkills getPersistedTechCVHardSkills(TechCVHardSkills techCVHardSkills) {
        return techCVHardSkillsRepository.findById(techCVHardSkills.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVHardSkillsToMatchAllProperties(TechCVHardSkills expectedTechCVHardSkills) {
        assertTechCVHardSkillsAllPropertiesEquals(expectedTechCVHardSkills, getPersistedTechCVHardSkills(expectedTechCVHardSkills));
    }

    protected void assertPersistedTechCVHardSkillsToMatchUpdatableProperties(TechCVHardSkills expectedTechCVHardSkills) {
        assertTechCVHardSkillsAllUpdatablePropertiesEquals(
            expectedTechCVHardSkills,
            getPersistedTechCVHardSkills(expectedTechCVHardSkills)
        );
    }
}
