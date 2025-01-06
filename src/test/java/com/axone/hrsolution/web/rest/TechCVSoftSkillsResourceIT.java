package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVSoftSkillsAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVSoftSkills;
import com.axone.hrsolution.repository.TechCVSoftSkillsRepository;
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
 * Integration tests for the {@link TechCVSoftSkillsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVSoftSkillsResourceIT {

    private static final String DEFAULT_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_SKILLS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-soft-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVSoftSkillsRepository techCVSoftSkillsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVSoftSkillsMockMvc;

    private TechCVSoftSkills techCVSoftSkills;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVSoftSkills createEntity(EntityManager em) {
        TechCVSoftSkills techCVSoftSkills = new TechCVSoftSkills().skills(DEFAULT_SKILLS);
        return techCVSoftSkills;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVSoftSkills createUpdatedEntity(EntityManager em) {
        TechCVSoftSkills techCVSoftSkills = new TechCVSoftSkills().skills(UPDATED_SKILLS);
        return techCVSoftSkills;
    }

    @BeforeEach
    public void initTest() {
        techCVSoftSkills = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVSoftSkills
        var returnedTechCVSoftSkills = om.readValue(
            restTechCVSoftSkillsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVSoftSkills)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVSoftSkills.class
        );

        // Validate the TechCVSoftSkills in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVSoftSkillsUpdatableFieldsEquals(returnedTechCVSoftSkills, getPersistedTechCVSoftSkills(returnedTechCVSoftSkills));
    }

    @Test
    @Transactional
    void createTechCVSoftSkillsWithExistingId() throws Exception {
        // Create the TechCVSoftSkills with an existing ID
        techCVSoftSkills.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVSoftSkillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVSoftSkills)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkillsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVSoftSkills.setSkills(null);

        // Create the TechCVSoftSkills, which fails.

        restTechCVSoftSkillsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVSoftSkills)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVSoftSkills() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        // Get all the techCVSoftSkillsList
        restTechCVSoftSkillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVSoftSkills.getId().intValue())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)));
    }

    @Test
    @Transactional
    void getTechCVSoftSkills() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        // Get the techCVSoftSkills
        restTechCVSoftSkillsMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVSoftSkills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVSoftSkills.getId().intValue()))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS));
    }

    @Test
    @Transactional
    void getNonExistingTechCVSoftSkills() throws Exception {
        // Get the techCVSoftSkills
        restTechCVSoftSkillsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVSoftSkills() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVSoftSkills
        TechCVSoftSkills updatedTechCVSoftSkills = techCVSoftSkillsRepository.findById(techCVSoftSkills.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVSoftSkills are not directly saved in db
        em.detach(updatedTechCVSoftSkills);
        updatedTechCVSoftSkills.skills(UPDATED_SKILLS);

        restTechCVSoftSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVSoftSkills.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVSoftSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVSoftSkillsToMatchAllProperties(updatedTechCVSoftSkills);
    }

    @Test
    @Transactional
    void putNonExistingTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVSoftSkills.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVSoftSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVSoftSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVSoftSkills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVSoftSkillsWithPatch() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVSoftSkills using partial update
        TechCVSoftSkills partialUpdatedTechCVSoftSkills = new TechCVSoftSkills();
        partialUpdatedTechCVSoftSkills.setId(techCVSoftSkills.getId());

        partialUpdatedTechCVSoftSkills.skills(UPDATED_SKILLS);

        restTechCVSoftSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVSoftSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVSoftSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVSoftSkills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVSoftSkillsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVSoftSkills, techCVSoftSkills),
            getPersistedTechCVSoftSkills(techCVSoftSkills)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVSoftSkillsWithPatch() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVSoftSkills using partial update
        TechCVSoftSkills partialUpdatedTechCVSoftSkills = new TechCVSoftSkills();
        partialUpdatedTechCVSoftSkills.setId(techCVSoftSkills.getId());

        partialUpdatedTechCVSoftSkills.skills(UPDATED_SKILLS);

        restTechCVSoftSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVSoftSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVSoftSkills))
            )
            .andExpect(status().isOk());

        // Validate the TechCVSoftSkills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVSoftSkillsUpdatableFieldsEquals(
            partialUpdatedTechCVSoftSkills,
            getPersistedTechCVSoftSkills(partialUpdatedTechCVSoftSkills)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVSoftSkills.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVSoftSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVSoftSkills))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVSoftSkills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVSoftSkills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVSoftSkillsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVSoftSkills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVSoftSkills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVSoftSkills() throws Exception {
        // Initialize the database
        techCVSoftSkillsRepository.saveAndFlush(techCVSoftSkills);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVSoftSkills
        restTechCVSoftSkillsMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVSoftSkills.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVSoftSkillsRepository.count();
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

    protected TechCVSoftSkills getPersistedTechCVSoftSkills(TechCVSoftSkills techCVSoftSkills) {
        return techCVSoftSkillsRepository.findById(techCVSoftSkills.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVSoftSkillsToMatchAllProperties(TechCVSoftSkills expectedTechCVSoftSkills) {
        assertTechCVSoftSkillsAllPropertiesEquals(expectedTechCVSoftSkills, getPersistedTechCVSoftSkills(expectedTechCVSoftSkills));
    }

    protected void assertPersistedTechCVSoftSkillsToMatchUpdatableProperties(TechCVSoftSkills expectedTechCVSoftSkills) {
        assertTechCVSoftSkillsAllUpdatablePropertiesEquals(
            expectedTechCVSoftSkills,
            getPersistedTechCVSoftSkills(expectedTechCVSoftSkills)
        );
    }
}
