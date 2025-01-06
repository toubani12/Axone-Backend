package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TechCVDocsAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.TechCVDocs;
import com.axone.hrsolution.repository.TechCVDocsRepository;
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
 * Integration tests for the {@link TechCVDocsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechCVDocsResourceIT {

    private static final byte[] DEFAULT_ATTACHED_DOC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHED_DOC = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHED_DOC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHED_DOC_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/tech-cv-docs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechCVDocsRepository techCVDocsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechCVDocsMockMvc;

    private TechCVDocs techCVDocs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVDocs createEntity(EntityManager em) {
        TechCVDocs techCVDocs = new TechCVDocs()
            .attachedDoc(DEFAULT_ATTACHED_DOC)
            .attachedDocContentType(DEFAULT_ATTACHED_DOC_CONTENT_TYPE);
        return techCVDocs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechCVDocs createUpdatedEntity(EntityManager em) {
        TechCVDocs techCVDocs = new TechCVDocs()
            .attachedDoc(UPDATED_ATTACHED_DOC)
            .attachedDocContentType(UPDATED_ATTACHED_DOC_CONTENT_TYPE);
        return techCVDocs;
    }

    @BeforeEach
    public void initTest() {
        techCVDocs = createEntity(em);
    }

    @Test
    @Transactional
    void createTechCVDocs() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TechCVDocs
        var returnedTechCVDocs = om.readValue(
            restTechCVDocsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVDocs)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TechCVDocs.class
        );

        // Validate the TechCVDocs in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechCVDocsUpdatableFieldsEquals(returnedTechCVDocs, getPersistedTechCVDocs(returnedTechCVDocs));
    }

    @Test
    @Transactional
    void createTechCVDocsWithExistingId() throws Exception {
        // Create the TechCVDocs with an existing ID
        techCVDocs.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechCVDocsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVDocs)))
            .andExpect(status().isBadRequest());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTechCVDocs() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        // Get all the techCVDocsList
        restTechCVDocsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(techCVDocs.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachedDocContentType").value(hasItem(DEFAULT_ATTACHED_DOC_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachedDoc").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ATTACHED_DOC))));
    }

    @Test
    @Transactional
    void getTechCVDocs() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        // Get the techCVDocs
        restTechCVDocsMockMvc
            .perform(get(ENTITY_API_URL_ID, techCVDocs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(techCVDocs.getId().intValue()))
            .andExpect(jsonPath("$.attachedDocContentType").value(DEFAULT_ATTACHED_DOC_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachedDoc").value(Base64.getEncoder().encodeToString(DEFAULT_ATTACHED_DOC)));
    }

    @Test
    @Transactional
    void getNonExistingTechCVDocs() throws Exception {
        // Get the techCVDocs
        restTechCVDocsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechCVDocs() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVDocs
        TechCVDocs updatedTechCVDocs = techCVDocsRepository.findById(techCVDocs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTechCVDocs are not directly saved in db
        em.detach(updatedTechCVDocs);
        updatedTechCVDocs.attachedDoc(UPDATED_ATTACHED_DOC).attachedDocContentType(UPDATED_ATTACHED_DOC_CONTENT_TYPE);

        restTechCVDocsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechCVDocs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechCVDocs))
            )
            .andExpect(status().isOk());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechCVDocsToMatchAllProperties(updatedTechCVDocs);
    }

    @Test
    @Transactional
    void putNonExistingTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, techCVDocs.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVDocs))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(techCVDocs))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(techCVDocs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechCVDocsWithPatch() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVDocs using partial update
        TechCVDocs partialUpdatedTechCVDocs = new TechCVDocs();
        partialUpdatedTechCVDocs.setId(techCVDocs.getId());

        partialUpdatedTechCVDocs.attachedDoc(UPDATED_ATTACHED_DOC).attachedDocContentType(UPDATED_ATTACHED_DOC_CONTENT_TYPE);

        restTechCVDocsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVDocs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVDocs))
            )
            .andExpect(status().isOk());

        // Validate the TechCVDocs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVDocsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechCVDocs, techCVDocs),
            getPersistedTechCVDocs(techCVDocs)
        );
    }

    @Test
    @Transactional
    void fullUpdateTechCVDocsWithPatch() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the techCVDocs using partial update
        TechCVDocs partialUpdatedTechCVDocs = new TechCVDocs();
        partialUpdatedTechCVDocs.setId(techCVDocs.getId());

        partialUpdatedTechCVDocs.attachedDoc(UPDATED_ATTACHED_DOC).attachedDocContentType(UPDATED_ATTACHED_DOC_CONTENT_TYPE);

        restTechCVDocsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechCVDocs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechCVDocs))
            )
            .andExpect(status().isOk());

        // Validate the TechCVDocs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechCVDocsUpdatableFieldsEquals(partialUpdatedTechCVDocs, getPersistedTechCVDocs(partialUpdatedTechCVDocs));
    }

    @Test
    @Transactional
    void patchNonExistingTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, techCVDocs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVDocs))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(techCVDocs))
            )
            .andExpect(status().isBadRequest());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechCVDocs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        techCVDocs.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechCVDocsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(techCVDocs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TechCVDocs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechCVDocs() throws Exception {
        // Initialize the database
        techCVDocsRepository.saveAndFlush(techCVDocs);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the techCVDocs
        restTechCVDocsMockMvc
            .perform(delete(ENTITY_API_URL_ID, techCVDocs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return techCVDocsRepository.count();
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

    protected TechCVDocs getPersistedTechCVDocs(TechCVDocs techCVDocs) {
        return techCVDocsRepository.findById(techCVDocs.getId()).orElseThrow();
    }

    protected void assertPersistedTechCVDocsToMatchAllProperties(TechCVDocs expectedTechCVDocs) {
        assertTechCVDocsAllPropertiesEquals(expectedTechCVDocs, getPersistedTechCVDocs(expectedTechCVDocs));
    }

    protected void assertPersistedTechCVDocsToMatchUpdatableProperties(TechCVDocs expectedTechCVDocs) {
        assertTechCVDocsAllUpdatablePropertiesEquals(expectedTechCVDocs, getPersistedTechCVDocs(expectedTechCVDocs));
    }
}
