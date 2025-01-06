package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.AppAccountTypeAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.AppAccountType;
import com.axone.hrsolution.repository.AppAccountTypeRepository;
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
 * Integration tests for the {@link AppAccountTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppAccountTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-account-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppAccountTypeRepository appAccountTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppAccountTypeMockMvc;

    private AppAccountType appAccountType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppAccountType createEntity(EntityManager em) {
        AppAccountType appAccountType = new AppAccountType().type(DEFAULT_TYPE);
        return appAccountType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppAccountType createUpdatedEntity(EntityManager em) {
        AppAccountType appAccountType = new AppAccountType().type(UPDATED_TYPE);
        return appAccountType;
    }

    @BeforeEach
    public void initTest() {
        appAccountType = createEntity(em);
    }

    @Test
    @Transactional
    void createAppAccountType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppAccountType
        var returnedAppAccountType = om.readValue(
            restAppAccountTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccountType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppAccountType.class
        );

        // Validate the AppAccountType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppAccountTypeUpdatableFieldsEquals(returnedAppAccountType, getPersistedAppAccountType(returnedAppAccountType));
    }

    @Test
    @Transactional
    void createAppAccountTypeWithExistingId() throws Exception {
        // Create the AppAccountType with an existing ID
        appAccountType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppAccountTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccountType)))
            .andExpect(status().isBadRequest());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appAccountType.setType(null);

        // Create the AppAccountType, which fails.

        restAppAccountTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccountType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppAccountTypes() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        // Get all the appAccountTypeList
        restAppAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appAccountType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getAppAccountType() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        // Get the appAccountType
        restAppAccountTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, appAccountType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appAccountType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingAppAccountType() throws Exception {
        // Get the appAccountType
        restAppAccountTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppAccountType() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccountType
        AppAccountType updatedAppAccountType = appAccountTypeRepository.findById(appAccountType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppAccountType are not directly saved in db
        em.detach(updatedAppAccountType);
        updatedAppAccountType.type(UPDATED_TYPE);

        restAppAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppAccountType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppAccountType))
            )
            .andExpect(status().isOk());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppAccountTypeToMatchAllProperties(updatedAppAccountType);
    }

    @Test
    @Transactional
    void putNonExistingAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appAccountType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appAccountType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appAccountType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccountType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppAccountTypeWithPatch() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccountType using partial update
        AppAccountType partialUpdatedAppAccountType = new AppAccountType();
        partialUpdatedAppAccountType.setId(appAccountType.getId());

        restAppAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppAccountType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppAccountType))
            )
            .andExpect(status().isOk());

        // Validate the AppAccountType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppAccountTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppAccountType, appAccountType),
            getPersistedAppAccountType(appAccountType)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppAccountTypeWithPatch() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccountType using partial update
        AppAccountType partialUpdatedAppAccountType = new AppAccountType();
        partialUpdatedAppAccountType.setId(appAccountType.getId());

        partialUpdatedAppAccountType.type(UPDATED_TYPE);

        restAppAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppAccountType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppAccountType))
            )
            .andExpect(status().isOk());

        // Validate the AppAccountType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppAccountTypeUpdatableFieldsEquals(partialUpdatedAppAccountType, getPersistedAppAccountType(partialUpdatedAppAccountType));
    }

    @Test
    @Transactional
    void patchNonExistingAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appAccountType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appAccountType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appAccountType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppAccountType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccountType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appAccountType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppAccountType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppAccountType() throws Exception {
        // Initialize the database
        appAccountTypeRepository.saveAndFlush(appAccountType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appAccountType
        restAppAccountTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, appAccountType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appAccountTypeRepository.count();
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

    protected AppAccountType getPersistedAppAccountType(AppAccountType appAccountType) {
        return appAccountTypeRepository.findById(appAccountType.getId()).orElseThrow();
    }

    protected void assertPersistedAppAccountTypeToMatchAllProperties(AppAccountType expectedAppAccountType) {
        assertAppAccountTypeAllPropertiesEquals(expectedAppAccountType, getPersistedAppAccountType(expectedAppAccountType));
    }

    protected void assertPersistedAppAccountTypeToMatchUpdatableProperties(AppAccountType expectedAppAccountType) {
        assertAppAccountTypeAllUpdatablePropertiesEquals(expectedAppAccountType, getPersistedAppAccountType(expectedAppAccountType));
    }
}
