package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ContractAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Application;
import com.axone.hrsolution.domain.Contract;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.Recruiter;
import com.axone.hrsolution.domain.Template;
import com.axone.hrsolution.domain.enumeration.ContractStatus;
import com.axone.hrsolution.domain.enumeration.TemplateContractType;
import com.axone.hrsolution.repository.ContractRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final TemplateContractType DEFAULT_TYPE = TemplateContractType.CDI;
    private static final TemplateContractType UPDATED_TYPE = TemplateContractType.CDD;

    private static final ContractStatus DEFAULT_STATUS = ContractStatus.CREATED;
    private static final ContractStatus UPDATED_STATUS = ContractStatus.PROCESSING;

    private static final Boolean DEFAULT_DIRECT_CONTRACT = false;
    private static final Boolean UPDATED_DIRECT_CONTRACT = true;

    private static final Double DEFAULT_PAYMENT_AMOUNT = 1D;
    private static final Double UPDATED_PAYMENT_AMOUNT = 2D;

    private static final Float DEFAULT_RECRUITER_INCOME_RATE = 1F;
    private static final Float UPDATED_RECRUITER_INCOME_RATE = 2F;

    private static final Float DEFAULT_CANDIDATE_INCOME_RATE = 1F;
    private static final Float UPDATED_CANDIDATE_INCOME_RATE = 2F;

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .label(DEFAULT_LABEL)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .directContract(DEFAULT_DIRECT_CONTRACT)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .recruiterIncomeRate(DEFAULT_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(DEFAULT_CANDIDATE_INCOME_RATE);
        // Add required entity
        Template template;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            template = TemplateResourceIT.createEntity(em);
            em.persist(template);
            em.flush();
        } else {
            template = TestUtil.findAll(em, Template.class).get(0);
        }
        contract.setTemplate(template);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        contract.setRecruiter(recruiter);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        contract.setEmployer(employer);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        contract.setApplication(application);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .directContract(UPDATED_DIRECT_CONTRACT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE);
        // Add required entity
        Template template;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            template = TemplateResourceIT.createUpdatedEntity(em);
            em.persist(template);
            em.flush();
        } else {
            template = TestUtil.findAll(em, Template.class).get(0);
        }
        contract.setTemplate(template);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createUpdatedEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        contract.setRecruiter(recruiter);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createUpdatedEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        contract.setEmployer(employer);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createUpdatedEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        contract.setApplication(application);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contract
        var returnedContract = om.readValue(
            restContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Contract.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setLabel(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setType(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setStatus(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectContractIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setDirectContract(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setPaymentAmount(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecruiterIncomeRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setRecruiterIncomeRate(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCandidateIncomeRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setCandidateIncomeRate(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].directContract").value(hasItem(DEFAULT_DIRECT_CONTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].recruiterIncomeRate").value(hasItem(DEFAULT_RECRUITER_INCOME_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].candidateIncomeRate").value(hasItem(DEFAULT_CANDIDATE_INCOME_RATE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.directContract").value(DEFAULT_DIRECT_CONTRACT.booleanValue()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.recruiterIncomeRate").value(DEFAULT_RECRUITER_INCOME_RATE.doubleValue()))
            .andExpect(jsonPath("$.candidateIncomeRate").value(DEFAULT_CANDIDATE_INCOME_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .directContract(UPDATED_DIRECT_CONTRACT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .label(UPDATED_LABEL)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .directContract(UPDATED_DIRECT_CONTRACT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
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

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
