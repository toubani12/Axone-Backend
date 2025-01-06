package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ApplicationAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Application;
import com.axone.hrsolution.domain.ContractType;
import com.axone.hrsolution.domain.Criteria;
import com.axone.hrsolution.domain.Domain;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.enumeration.ApplicationStatus;
import com.axone.hrsolution.repository.ApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_CANDIDATES = 1;
    private static final Integer UPDATED_NUMBER_OF_CANDIDATES = 2;

    private static final Double DEFAULT_PAYMENT_AMOUNT = 1D;
    private static final Double UPDATED_PAYMENT_AMOUNT = 2D;

    private static final Float DEFAULT_RECRUITER_INCOME_RATE = 1F;
    private static final Float UPDATED_RECRUITER_INCOME_RATE = 2F;

    private static final Float DEFAULT_CANDIDATE_INCOME_RATE = 1F;
    private static final Float UPDATED_CANDIDATE_INCOME_RATE = 2F;

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());

    private static final ApplicationStatus DEFAULT_STATUS = ApplicationStatus.NEW;
    private static final ApplicationStatus UPDATED_STATUS = ApplicationStatus.OPEN;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OPENED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DONE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationRepository applicationRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationMockMvc;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .numberOfCandidates(DEFAULT_NUMBER_OF_CANDIDATES)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .recruiterIncomeRate(DEFAULT_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(DEFAULT_CANDIDATE_INCOME_RATE)
            .deadline(DEFAULT_DEADLINE)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .openedAt(DEFAULT_OPENED_AT)
            .closedAt(DEFAULT_CLOSED_AT)
            .doneAt(DEFAULT_DONE_AT);
        // Add required entity
        ContractType contractType;
        if (TestUtil.findAll(em, ContractType.class).isEmpty()) {
            contractType = ContractTypeResourceIT.createEntity(em);
            em.persist(contractType);
            em.flush();
        } else {
            contractType = TestUtil.findAll(em, ContractType.class).get(0);
        }
        application.getContractTypes().add(contractType);
        // Add required entity
        Criteria criteria;
        if (TestUtil.findAll(em, Criteria.class).isEmpty()) {
            criteria = CriteriaResourceIT.createEntity(em);
            em.persist(criteria);
            em.flush();
        } else {
            criteria = TestUtil.findAll(em, Criteria.class).get(0);
        }
        application.getCriteria().add(criteria);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        application.getDomains().add(domain);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        application.setEmployer(employer);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfCandidates(UPDATED_NUMBER_OF_CANDIDATES)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .doneAt(UPDATED_DONE_AT);
        // Add required entity
        ContractType contractType;
        if (TestUtil.findAll(em, ContractType.class).isEmpty()) {
            contractType = ContractTypeResourceIT.createUpdatedEntity(em);
            em.persist(contractType);
            em.flush();
        } else {
            contractType = TestUtil.findAll(em, ContractType.class).get(0);
        }
        application.getContractTypes().add(contractType);
        // Add required entity
        Criteria criteria;
        if (TestUtil.findAll(em, Criteria.class).isEmpty()) {
            criteria = CriteriaResourceIT.createUpdatedEntity(em);
            em.persist(criteria);
            em.flush();
        } else {
            criteria = TestUtil.findAll(em, Criteria.class).get(0);
        }
        application.getCriteria().add(criteria);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createUpdatedEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        application.getDomains().add(domain);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createUpdatedEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        application.setEmployer(employer);
        return application;
    }

    @BeforeEach
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    void createApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Application
        var returnedApplication = om.readValue(
            restApplicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Application.class
        );

        // Validate the Application in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertApplicationUpdatableFieldsEquals(returnedApplication, getPersistedApplication(returnedApplication));
    }

    @Test
    @Transactional
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setTitle(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setDescription(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfCandidatesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setNumberOfCandidates(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setPaymentAmount(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setStatus(null);

        // Create the Application, which fails.

        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfCandidates").value(hasItem(DEFAULT_NUMBER_OF_CANDIDATES)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].recruiterIncomeRate").value(hasItem(DEFAULT_RECRUITER_INCOME_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].candidateIncomeRate").value(hasItem(DEFAULT_CANDIDATE_INCOME_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].openedAt").value(hasItem(DEFAULT_OPENED_AT.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].doneAt").value(hasItem(DEFAULT_DONE_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(applicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.numberOfCandidates").value(DEFAULT_NUMBER_OF_CANDIDATES))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.recruiterIncomeRate").value(DEFAULT_RECRUITER_INCOME_RATE.doubleValue()))
            .andExpect(jsonPath("$.candidateIncomeRate").value(DEFAULT_CANDIDATE_INCOME_RATE.doubleValue()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.openedAt").value(DEFAULT_OPENED_AT.toString()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.doneAt").value(DEFAULT_DONE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfCandidates(UPDATED_NUMBER_OF_CANDIDATES)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .doneAt(UPDATED_DONE_AT);

        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationToMatchAllProperties(updatedApplication);
    }

    @Test
    @Transactional
    void putNonExistingApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, application.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(application)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS)
            .doneAt(UPDATED_DONE_AT);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplication, application),
            getPersistedApplication(application)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .numberOfCandidates(UPDATED_NUMBER_OF_CANDIDATES)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .recruiterIncomeRate(UPDATED_RECRUITER_INCOME_RATE)
            .candidateIncomeRate(UPDATED_CANDIDATE_INCOME_RATE)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .doneAt(UPDATED_DONE_AT);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUpdatableFieldsEquals(partialUpdatedApplication, getPersistedApplication(partialUpdatedApplication));
    }

    @Test
    @Transactional
    void patchNonExistingApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, application.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(application)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the application
        restApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, application.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationRepository.count();
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

    protected Application getPersistedApplication(Application application) {
        return applicationRepository.findById(application.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationToMatchAllProperties(Application expectedApplication) {
        assertApplicationAllPropertiesEquals(expectedApplication, getPersistedApplication(expectedApplication));
    }

    protected void assertPersistedApplicationToMatchUpdatableProperties(Application expectedApplication) {
        assertApplicationAllUpdatablePropertiesEquals(expectedApplication, getPersistedApplication(expectedApplication));
    }
}
