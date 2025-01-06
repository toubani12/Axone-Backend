package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ResumeAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Resume;
import com.axone.hrsolution.repository.ResumeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link ResumeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResumeResourceIT {

    private static final byte[] DEFAULT_RESUME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RESUME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RESUME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RESUME_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/resumes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResumeMockMvc;

    private Resume resume;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume().resume(DEFAULT_RESUME).resumeContentType(DEFAULT_RESUME_CONTENT_TYPE);
        return resume;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createUpdatedEntity(EntityManager em) {
        Resume resume = new Resume().resume(UPDATED_RESUME).resumeContentType(UPDATED_RESUME_CONTENT_TYPE);
        return resume;
    }

    @BeforeEach
    public void initTest() {
        resume = createEntity(em);
    }

    @Test
    @Transactional
    void createResume() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resume
        var returnedResume = om.readValue(
            restResumeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resume)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Resume.class
        );

        // Validate the Resume in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResumeUpdatableFieldsEquals(returnedResume, getPersistedResume(returnedResume));
    }

    @Test
    @Transactional
    void createResumeWithExistingId() throws Exception {
        // Create the Resume with an existing ID
        resume.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resume)))
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList
        restResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].resumeContentType").value(hasItem(DEFAULT_RESUME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_RESUME))));
    }

    @Test
    @Transactional
    void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc
            .perform(get(ENTITY_API_URL_ID, resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.resumeContentType").value(DEFAULT_RESUME_CONTENT_TYPE))
            .andExpect(jsonPath("$.resume").value(Base64.getEncoder().encodeToString(DEFAULT_RESUME)));
    }

    @Test
    @Transactional
    void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume
        Resume updatedResume = resumeRepository.findById(resume.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResume are not directly saved in db
        em.detach(updatedResume);
        updatedResume.resume(UPDATED_RESUME).resumeContentType(UPDATED_RESUME_CONTENT_TYPE);

        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResume.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResumeToMatchAllProperties(updatedResume);
    }

    @Test
    @Transactional
    void putNonExistingResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(put(ENTITY_API_URL_ID, resume.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resume)))
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resume)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume.resume(UPDATED_RESUME).resumeContentType(UPDATED_RESUME_CONTENT_TYPE);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResumeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResume, resume), getPersistedResume(resume));
    }

    @Test
    @Transactional
    void fullUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume.resume(UPDATED_RESUME).resumeContentType(UPDATED_RESUME_CONTENT_TYPE);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResumeUpdatableFieldsEquals(partialUpdatedResume, getPersistedResume(partialUpdatedResume));
    }

    @Test
    @Transactional
    void patchNonExistingResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resume.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResume() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resume.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resume)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resume
        restResumeMockMvc
            .perform(delete(ENTITY_API_URL_ID, resume.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resumeRepository.count();
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

    protected Resume getPersistedResume(Resume resume) {
        return resumeRepository.findById(resume.getId()).orElseThrow();
    }

    protected void assertPersistedResumeToMatchAllProperties(Resume expectedResume) {
        assertResumeAllPropertiesEquals(expectedResume, getPersistedResume(expectedResume));
    }

    protected void assertPersistedResumeToMatchUpdatableProperties(Resume expectedResume) {
        assertResumeAllUpdatablePropertiesEquals(expectedResume, getPersistedResume(expectedResume));
    }
}
