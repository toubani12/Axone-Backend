package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.CriteriaAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Criteria;
import com.axone.hrsolution.repository.CriteriaRepository;
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
 * Integration tests for the {@link CriteriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CriteriaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/criteria";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCriteriaMockMvc;

    private Criteria criteria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Criteria createEntity(EntityManager em) {
        Criteria criteria = new Criteria().name(DEFAULT_NAME);
        return criteria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Criteria createUpdatedEntity(EntityManager em) {
        Criteria criteria = new Criteria().name(UPDATED_NAME);
        return criteria;
    }

    @BeforeEach
    public void initTest() {
        criteria = createEntity(em);
    }

    @Test
    @Transactional
    void createCriteria() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Criteria
        var returnedCriteria = om.readValue(
            restCriteriaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criteria)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Criteria.class
        );

        // Validate the Criteria in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCriteriaUpdatableFieldsEquals(returnedCriteria, getPersistedCriteria(returnedCriteria));
    }

    @Test
    @Transactional
    void createCriteriaWithExistingId() throws Exception {
        // Create the Criteria with an existing ID
        criteria.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriteriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criteria)))
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        criteria.setName(null);

        // Create the Criteria, which fails.

        restCriteriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criteria)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        // Get all the criteriaList
        restCriteriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criteria.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        // Get the criteria
        restCriteriaMockMvc
            .perform(get(ENTITY_API_URL_ID, criteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(criteria.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCriteria() throws Exception {
        // Get the criteria
        restCriteriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the criteria
        Criteria updatedCriteria = criteriaRepository.findById(criteria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCriteria are not directly saved in db
        em.detach(updatedCriteria);
        updatedCriteria.name(UPDATED_NAME);

        restCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCriteria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCriteria))
            )
            .andExpect(status().isOk());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCriteriaToMatchAllProperties(updatedCriteria);
    }

    @Test
    @Transactional
    void putNonExistingCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, criteria.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(criteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criteria)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCriteriaWithPatch() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the criteria using partial update
        Criteria partialUpdatedCriteria = new Criteria();
        partialUpdatedCriteria.setId(criteria.getId());

        partialUpdatedCriteria.name(UPDATED_NAME);

        restCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCriteria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCriteria))
            )
            .andExpect(status().isOk());

        // Validate the Criteria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCriteriaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCriteria, criteria), getPersistedCriteria(criteria));
    }

    @Test
    @Transactional
    void fullUpdateCriteriaWithPatch() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the criteria using partial update
        Criteria partialUpdatedCriteria = new Criteria();
        partialUpdatedCriteria.setId(criteria.getId());

        partialUpdatedCriteria.name(UPDATED_NAME);

        restCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCriteria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCriteria))
            )
            .andExpect(status().isOk());

        // Validate the Criteria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCriteriaUpdatableFieldsEquals(partialUpdatedCriteria, getPersistedCriteria(partialUpdatedCriteria));
    }

    @Test
    @Transactional
    void patchNonExistingCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, criteria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(criteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(criteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCriteria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        criteria.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriteriaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(criteria)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Criteria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCriteria() throws Exception {
        // Initialize the database
        criteriaRepository.saveAndFlush(criteria);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the criteria
        restCriteriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, criteria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return criteriaRepository.count();
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

    protected Criteria getPersistedCriteria(Criteria criteria) {
        return criteriaRepository.findById(criteria.getId()).orElseThrow();
    }

    protected void assertPersistedCriteriaToMatchAllProperties(Criteria expectedCriteria) {
        assertCriteriaAllPropertiesEquals(expectedCriteria, getPersistedCriteria(expectedCriteria));
    }

    protected void assertPersistedCriteriaToMatchUpdatableProperties(Criteria expectedCriteria) {
        assertCriteriaAllUpdatablePropertiesEquals(expectedCriteria, getPersistedCriteria(expectedCriteria));
    }
}
