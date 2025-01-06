package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.AppTestTypeAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.AppTestType;
import com.axone.hrsolution.repository.AppTestTypeRepository;
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
 * Integration tests for the {@link AppTestTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppTestTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-test-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppTestTypeRepository appTestTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppTestTypeMockMvc;

    private AppTestType appTestType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppTestType createEntity(EntityManager em) {
        AppTestType appTestType = new AppTestType().type(DEFAULT_TYPE);
        return appTestType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppTestType createUpdatedEntity(EntityManager em) {
        AppTestType appTestType = new AppTestType().type(UPDATED_TYPE);
        return appTestType;
    }

    @BeforeEach
    public void initTest() {
        appTestType = createEntity(em);
    }

    @Test
    @Transactional
    void createAppTestType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppTestType
        var returnedAppTestType = om.readValue(
            restAppTestTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTestType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppTestType.class
        );

        // Validate the AppTestType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppTestTypeUpdatableFieldsEquals(returnedAppTestType, getPersistedAppTestType(returnedAppTestType));
    }

    @Test
    @Transactional
    void createAppTestTypeWithExistingId() throws Exception {
        // Create the AppTestType with an existing ID
        appTestType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppTestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTestType)))
            .andExpect(status().isBadRequest());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appTestType.setType(null);

        // Create the AppTestType, which fails.

        restAppTestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTestType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppTestTypes() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        // Get all the appTestTypeList
        restAppTestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appTestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getAppTestType() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        // Get the appTestType
        restAppTestTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, appTestType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appTestType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingAppTestType() throws Exception {
        // Get the appTestType
        restAppTestTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppTestType() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTestType
        AppTestType updatedAppTestType = appTestTypeRepository.findById(appTestType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppTestType are not directly saved in db
        em.detach(updatedAppTestType);
        updatedAppTestType.type(UPDATED_TYPE);

        restAppTestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppTestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppTestType))
            )
            .andExpect(status().isOk());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppTestTypeToMatchAllProperties(updatedAppTestType);
    }

    @Test
    @Transactional
    void putNonExistingAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appTestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appTestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appTestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTestType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppTestTypeWithPatch() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTestType using partial update
        AppTestType partialUpdatedAppTestType = new AppTestType();
        partialUpdatedAppTestType.setId(appTestType.getId());

        restAppTestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppTestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppTestType))
            )
            .andExpect(status().isOk());

        // Validate the AppTestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppTestTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppTestType, appTestType),
            getPersistedAppTestType(appTestType)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppTestTypeWithPatch() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTestType using partial update
        AppTestType partialUpdatedAppTestType = new AppTestType();
        partialUpdatedAppTestType.setId(appTestType.getId());

        partialUpdatedAppTestType.type(UPDATED_TYPE);

        restAppTestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppTestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppTestType))
            )
            .andExpect(status().isOk());

        // Validate the AppTestType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppTestTypeUpdatableFieldsEquals(partialUpdatedAppTestType, getPersistedAppTestType(partialUpdatedAppTestType));
    }

    @Test
    @Transactional
    void patchNonExistingAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appTestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appTestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appTestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppTestType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTestType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appTestType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppTestType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppTestType() throws Exception {
        // Initialize the database
        appTestTypeRepository.saveAndFlush(appTestType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appTestType
        restAppTestTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, appTestType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appTestTypeRepository.count();
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

    protected AppTestType getPersistedAppTestType(AppTestType appTestType) {
        return appTestTypeRepository.findById(appTestType.getId()).orElseThrow();
    }

    protected void assertPersistedAppTestTypeToMatchAllProperties(AppTestType expectedAppTestType) {
        assertAppTestTypeAllPropertiesEquals(expectedAppTestType, getPersistedAppTestType(expectedAppTestType));
    }

    protected void assertPersistedAppTestTypeToMatchUpdatableProperties(AppTestType expectedAppTestType) {
        assertAppTestTypeAllUpdatablePropertiesEquals(expectedAppTestType, getPersistedAppTestType(expectedAppTestType));
    }
}
