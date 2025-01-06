package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVProjectAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVProject;
import com.axone.hrsolution.repository.TechCVProjectRepository;
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
 * Integration tests for the {@link TechCVProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVProjectResourceIT {

    private static final String DEFAULT_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tech-cv-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVProjectRepository techCVProjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVProjectMockMvc;

    private TechCVProject techCVProject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVProject createEntity(EntityManager em) {
        TechCVProject techCVProject = new TechCVProject().project(DEFAULT_PROJECT);
        return techCVProject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVProject createUpdatedEntity(EntityManager em) {
        TechCVProject techCVProject = new TechCVProject().project(UPDATED_PROJECT);
        return techCVProject;
    }

    @BeforeEach
    public void initTest() {
        techCVProject = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVProject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVProject
        var returnedTechCVProject = om.readValue(
            restTechCVProjectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVProject)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVProject.class
        );

        // Validate the TechCVProject in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVProjectUpdatableFieldsEquals(returnedTechCVProject, getPersistedTechCVProject(returnedTechCVProject));
    }

    @Test
    @Transactional
    void createTechCVProjectWithExistingId() throws Exception {
        // Create the TechCVProject with an existing ID
        techCVProject.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVProject)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        techCVProject.setProject(null);

        // Create the TechCVProject, which fails.

        restTechCVProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVProject)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechCVProjects() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        // Get all the techCVProjectList
        restTechCVProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT)));
    }

    @Test
    @Transactional
    void getTechCVProject() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        // Get the techCVProject
        restTechCVProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVProject.getId().intValue()))
            .andExpect(jsonPath("$.project").value(DEFAULT_PROJECT));
    }

    @Test
    @Transactional
    void getNonExistingTechCVProject() throws Exception {
        // Get the techCVProject
        restTechCVProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVProject() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVProject
        TechCVProject updatedTechCVProject = techCVProjectRepository.findById(techCVProject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVProject are not directly saved in db
        em.detach(updatedTechCVProject);
        updatedTechCVProject.project(UPDATED_PROJECT);

        restTechCVProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVProject))
            )
            .andExpect(status().isOk());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVProjectToMatchAllProperties(updatedTechCVProject);
    }

    @Test
    @Transactional
    void putNonExistingTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVProject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVProjectWithPatch() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVProject using partial update
        TechCVProject partialUpdatedTechCVProject = new TechCVProject();
        partialUpdatedTechCVProject.setId(techCVProject.getId());

        partialUpdatedTechCVProject.project(UPDATED_PROJECT);

        restTechCVProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVProject))
            )
            .andExpect(status().isOk());

        // Validate the TechCVProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVProjectUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVProject, techCVProject),
            getPersistedTechCVProject(techCVProject)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVProjectWithPatch() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVProject using partial update
        TechCVProject partialUpdatedTechCVProject = new TechCVProject();
        partialUpdatedTechCVProject.setId(techCVProject.getId());

        partialUpdatedTechCVProject.project(UPDATED_PROJECT);

        restTechCVProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVProject))
            )
            .andExpect(status().isOk());

        // Validate the TechCVProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVProjectUpdatableFieldsEquals(partialUpdatedTechCVProject, getPersistedTechCVProject(partialUpdatedTechCVProject));
    }

    @Test
    @Transactional
    void patchNonExistingTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVProject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVProject() throws Exception {
        // Initialize the database
        techCVProjectRepository.saveAndFlush(techCVProject);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVProject
        restTechCVProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVProject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVProjectRepository.count();
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

    protected TechCVProject getPersistedTechCVProject(TechCVProject techCVProject) {
        return techCVProjectRepository.findById(techCVProject.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVProjectToMatchAllProperties(TechCVProject expectedTechCVProject) {
        assertTechCVProjectAllPropertiesEquals(expectedTechCVProject, getPersistedTechCVProject(expectedTechCVProject));
    }

    protected void assertPersistedTechCVProjectToMatchUpdatableProperties(TechCVProject expectedTechCVProject) {
        assertTechCVProjectAllUpdatablePropertiesEquals(expectedTechCVProject, getPersistedTechCVProject(expectedTechCVProject));
    }
}
