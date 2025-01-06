package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ContractTypeAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.ContractType;
import com.axone.hrsolution.repository.ContractTypeRepository;
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
 * Integration tests for the {@link ContractTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contract-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractTypeMockMvc;

    private ContractType contractType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractType createEntity(EntityManager em) {
        ContractType contractType = new ContractType().name(DEFAULT_NAME);
        return contractType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractType createUpdatedEntity(EntityManager em) {
        ContractType contractType = new ContractType().name(UPDATED_NAME);
        return contractType;
    }

    @BeforeEach
    public void initTest() {
        contractType = createEntity(em);
    }

    @Test
    @Transactional
    void createContractType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContractType
        var returnedContractType = om.readValue(
            restContractTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractType.class
        );

        // Validate the ContractType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractTypeUpdatableFieldsEquals(returnedContractType, getPersistedContractType(returnedContractType));
    }

    @Test
    @Transactional
    void createContractTypeWithExistingId() throws Exception {
        // Create the ContractType with an existing ID
        contractType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractType)))
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractType.setName(null);

        // Create the ContractType, which fails.

        restContractTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractTypes() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList
        restContractTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getContractType() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get the contractType
        restContractTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, contractType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingContractType() throws Exception {
        // Get the contractType
        restContractTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContractType() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractType
        ContractType updatedContractType = contractTypeRepository.findById(contractType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContractType are not directly saved in db
        em.detach(updatedContractType);
        updatedContractType.name(UPDATED_NAME);

        restContractTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContractType))
            )
            .andExpect(status().isOk());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractTypeToMatchAllProperties(updatedContractType);
    }

    @Test
    @Transactional
    void putNonExistingContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractTypeWithPatch() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractType using partial update
        ContractType partialUpdatedContractType = new ContractType();
        partialUpdatedContractType.setId(contractType.getId());

        partialUpdatedContractType.name(UPDATED_NAME);

        restContractTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractType))
            )
            .andExpect(status().isOk());

        // Validate the ContractType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContractType, contractType),
            getPersistedContractType(contractType)
        );
    }

    @Test
    @Transactional
    void fullUpdateContractTypeWithPatch() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractType using partial update
        ContractType partialUpdatedContractType = new ContractType();
        partialUpdatedContractType.setId(contractType.getId());

        partialUpdatedContractType.name(UPDATED_NAME);

        restContractTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractType))
            )
            .andExpect(status().isOk());

        // Validate the ContractType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractTypeUpdatableFieldsEquals(partialUpdatedContractType, getPersistedContractType(partialUpdatedContractType));
    }

    @Test
    @Transactional
    void patchNonExistingContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractType() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contractType
        restContractTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractTypeRepository.count();
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

    protected ContractType getPersistedContractType(ContractType contractType) {
        return contractTypeRepository.findById(contractType.getId()).orElseThrow();
    }

    protected void assertPersistedContractTypeToMatchAllProperties(ContractType expectedContractType) {
        assertContractTypeAllPropertiesEquals(expectedContractType, getPersistedContractType(expectedContractType));
    }

    protected void assertPersistedContractTypeToMatchUpdatableProperties(ContractType expectedContractType) {
        assertContractTypeAllUpdatablePropertiesEquals(expectedContractType, getPersistedContractType(expectedContractType));
    }
}
