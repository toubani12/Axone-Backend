package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.DomainAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Domain;
import com.axone.hrsolution.repository.DomainRepository;
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
 * Integration tests for the {@link DomainResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomainResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomainMockMvc;

    private Domain domain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createEntity(EntityManager em) {
        Domain domain = new Domain().name(DEFAULT_NAME);
        return domain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createUpdatedEntity(EntityManager em) {
        Domain domain = new Domain().name(UPDATED_NAME);
        return domain;
    }

    @BeforeEach
    public void initTest() {
        domain = createEntity(em);
    }

    @Test
    @Transactional
    void createDomain() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Domain
        var returnedDomain = om.readValue(
            restDomainMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domain)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Domain.class
        );

        // Validate the Domain in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDomainUpdatableFieldsEquals(returnedDomain, getPersistedDomain(returnedDomain));
    }

    @Test
    @Transactional
    void createDomainWithExistingId() throws Exception {
        // Create the Domain with an existing ID
        domain.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domain)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        domain.setName(null);

        // Create the Domain, which fails.

        restDomainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domain)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDomains() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList
        restDomainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get the domain
        restDomainMockMvc
            .perform(get(ENTITY_API_URL_ID, domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domain.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDomain() throws Exception {
        // Get the domain
        restDomainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domain
        Domain updatedDomain = domainRepository.findById(domain.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDomain are not directly saved in db
        em.detach(updatedDomain);
        updatedDomain.name(UPDATED_NAME);

        restDomainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDomain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDomain))
            )
            .andExpect(status().isOk());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDomainToMatchAllProperties(updatedDomain);
    }

    @Test
    @Transactional
    void putNonExistingDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(put(ENTITY_API_URL_ID, domain.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domain)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(domain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomainWithPatch() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domain using partial update
        Domain partialUpdatedDomain = new Domain();
        partialUpdatedDomain.setId(domain.getId());

        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomain.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDomain))
            )
            .andExpect(status().isOk());

        // Validate the Domain in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDomainUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDomain, domain), getPersistedDomain(domain));
    }

    @Test
    @Transactional
    void fullUpdateDomainWithPatch() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domain using partial update
        Domain partialUpdatedDomain = new Domain();
        partialUpdatedDomain.setId(domain.getId());

        partialUpdatedDomain.name(UPDATED_NAME);

        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomain.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDomain))
            )
            .andExpect(status().isOk());

        // Validate the Domain in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDomainUpdatableFieldsEquals(partialUpdatedDomain, getPersistedDomain(partialUpdatedDomain));
    }

    @Test
    @Transactional
    void patchNonExistingDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domain.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(domain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(domain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domain.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(domain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domain in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the domain
        restDomainMockMvc
            .perform(delete(ENTITY_API_URL_ID, domain.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return domainRepository.count();
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

    protected Domain getPersistedDomain(Domain domain) {
        return domainRepository.findById(domain.getId()).orElseThrow();
    }

    protected void assertPersistedDomainToMatchAllProperties(Domain expectedDomain) {
        assertDomainAllPropertiesEquals(expectedDomain, getPersistedDomain(expectedDomain));
    }

    protected void assertPersistedDomainToMatchUpdatableProperties(Domain expectedDomain) {
        assertDomainAllUpdatablePropertiesEquals(expectedDomain, getPersistedDomain(expectedDomain));
    }
}
