package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ProviderAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Provider;
import com.axone.hrsolution.repository.ProviderRepository;
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
 * Integration tests for the {@link ProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProviderMockMvc;

    private Provider provider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createEntity(EntityManager em) {
        Provider provider = new Provider().name(DEFAULT_NAME).country(DEFAULT_COUNTRY);
        return provider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createUpdatedEntity(EntityManager em) {
        Provider provider = new Provider().name(UPDATED_NAME).country(UPDATED_COUNTRY);
        return provider;
    }

    @BeforeEach
    public void initTest() {
        provider = createEntity(em);
    }

    @Test
    @Transactional
    void createProvider() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Provider
        var returnedProvider = om.readValue(
            restProviderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provider)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Provider.class
        );

        // Validate the Provider in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProviderUpdatableFieldsEquals(returnedProvider, getPersistedProvider(returnedProvider));
    }

    @Test
    @Transactional
    void createProviderWithExistingId() throws Exception {
        // Create the Provider with an existing ID
        provider.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provider)))
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provider.setName(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provider)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProviders() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get all the providerList
        restProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get the provider
        restProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(provider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingProvider() throws Exception {
        // Get the provider
        restProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provider
        Provider updatedProvider = providerRepository.findById(provider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProvider are not directly saved in db
        em.detach(updatedProvider);
        updatedProvider.name(UPDATED_NAME).country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProviderToMatchAllProperties(updatedProvider);
    }

    @Test
    @Transactional
    void putNonExistingProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provider.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provider)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProviderWithPatch() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provider using partial update
        Provider partialUpdatedProvider = new Provider();
        partialUpdatedProvider.setId(provider.getId());

        partialUpdatedProvider.name(UPDATED_NAME).country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProviderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProvider, provider), getPersistedProvider(provider));
    }

    @Test
    @Transactional
    void fullUpdateProviderWithPatch() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provider using partial update
        Provider partialUpdatedProvider = new Provider();
        partialUpdatedProvider.setId(provider.getId());

        partialUpdatedProvider.name(UPDATED_NAME).country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProviderUpdatableFieldsEquals(partialUpdatedProvider, getPersistedProvider(partialUpdatedProvider));
    }

    @Test
    @Transactional
    void patchNonExistingProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provider.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(provider)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the provider
        restProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, provider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return providerRepository.count();
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

    protected Provider getPersistedProvider(Provider provider) {
        return providerRepository.findById(provider.getId()).orElseThrow();
    }

    protected void assertPersistedProviderToMatchAllProperties(Provider expectedProvider) {
        assertProviderAllPropertiesEquals(expectedProvider, getPersistedProvider(expectedProvider));
    }

    protected void assertPersistedProviderToMatchUpdatableProperties(Provider expectedProvider) {
        assertProviderAllUpdatablePropertiesEquals(expectedProvider, getPersistedProvider(expectedProvider));
    }
}
