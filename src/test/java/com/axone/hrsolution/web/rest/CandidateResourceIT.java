package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.CandidateAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Candidate;
import com.axone.hrsolution.domain.Domain;
import com.axone.hrsolution.domain.TechnicalCV;
import com.axone.hrsolution.repository.CandidateRepository;
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
 * Integration tests for the {@link CandidateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CandidateResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN_URL = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEARS_OF_EXPERIENCE = 1;
    private static final Integer UPDATED_YEARS_OF_EXPERIENCE = 2;

    private static final Double DEFAULT_CURRENT_SALARY = 1D;
    private static final Double UPDATED_CURRENT_SALARY = 2D;

    private static final Double DEFAULT_DESIRED_SALARY = 1D;
    private static final Double UPDATED_DESIRED_SALARY = 2D;

    private static final Boolean DEFAULT_HAS_CONTRACT = false;
    private static final Boolean UPDATED_HAS_CONTRACT = true;

    private static final Boolean DEFAULT_HIRED = false;
    private static final Boolean UPDATED_HIRED = true;

    private static final Float DEFAULT_RATE = 1F;
    private static final Float UPDATED_RATE = 2F;

    private static final String ENTITY_API_URL = "/api/candidates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateRepository candidateRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidateMockMvc;

    private Candidate candidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .linkedinUrl(DEFAULT_LINKEDIN_URL)
            .fullName(DEFAULT_FULL_NAME)
            .yearsOfExperience(DEFAULT_YEARS_OF_EXPERIENCE)
            .currentSalary(DEFAULT_CURRENT_SALARY)
            .desiredSalary(DEFAULT_DESIRED_SALARY)
            .hasContract(DEFAULT_HAS_CONTRACT)
            .hired(DEFAULT_HIRED)
            .rate(DEFAULT_RATE);
        // Add required entity
        TechnicalCV technicalCV;
        if (TestUtil.findAll(em, TechnicalCV.class).isEmpty()) {
            technicalCV = TechnicalCVResourceIT.createEntity(em);
            em.persist(technicalCV);
            em.flush();
        } else {
            technicalCV = TestUtil.findAll(em, TechnicalCV.class).get(0);
        }
        candidate.setTechCV(technicalCV);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        candidate.getDomains().add(domain);
        return candidate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createUpdatedEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .fullName(UPDATED_FULL_NAME)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .currentSalary(UPDATED_CURRENT_SALARY)
            .desiredSalary(UPDATED_DESIRED_SALARY)
            .hasContract(UPDATED_HAS_CONTRACT)
            .hired(UPDATED_HIRED)
            .rate(UPDATED_RATE);
        // Add required entity
        TechnicalCV technicalCV;
        if (TestUtil.findAll(em, TechnicalCV.class).isEmpty()) {
            technicalCV = TechnicalCVResourceIT.createUpdatedEntity(em);
            em.persist(technicalCV);
            em.flush();
        } else {
            technicalCV = TestUtil.findAll(em, TechnicalCV.class).get(0);
        }
        candidate.setTechCV(technicalCV);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createUpdatedEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        candidate.getDomains().add(domain);
        return candidate;
    }

    @BeforeEach
    public void initTest() {
        candidate = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Candidate
        var returnedCandidate = om.readValue(
            restCandidateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Candidate.class
        );

        // Validate the Candidate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCandidateUpdatableFieldsEquals(returnedCandidate, getPersistedCandidate(returnedCandidate));
    }

    @Test
    @Transactional
    void createCandidateWithExistingId() throws Exception {
        // Create the Candidate with an existing ID
        candidate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidate.setFullName(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearsOfExperienceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidate.setYearsOfExperience(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasContractIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidate.setHasContract(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidate.setHired(null);

        // Create the Candidate, which fails.

        restCandidateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCandidates() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].linkedinUrl").value(hasItem(DEFAULT_LINKEDIN_URL)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].currentSalary").value(hasItem(DEFAULT_CURRENT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].desiredSalary").value(hasItem(DEFAULT_DESIRED_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].hasContract").value(hasItem(DEFAULT_HAS_CONTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].hired").value(hasItem(DEFAULT_HIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCandidatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(candidateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(candidateRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCandidatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(candidateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCandidateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(candidateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get the candidate
        restCandidateMockMvc
            .perform(get(ENTITY_API_URL_ID, candidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.linkedinUrl").value(DEFAULT_LINKEDIN_URL))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.yearsOfExperience").value(DEFAULT_YEARS_OF_EXPERIENCE))
            .andExpect(jsonPath("$.currentSalary").value(DEFAULT_CURRENT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.desiredSalary").value(DEFAULT_DESIRED_SALARY.doubleValue()))
            .andExpect(jsonPath("$.hasContract").value(DEFAULT_HAS_CONTRACT.booleanValue()))
            .andExpect(jsonPath("$.hired").value(DEFAULT_HIRED.booleanValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCandidate() throws Exception {
        // Get the candidate
        restCandidateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidate
        Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCandidate are not directly saved in db
        em.detach(updatedCandidate);
        updatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .fullName(UPDATED_FULL_NAME)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .currentSalary(UPDATED_CURRENT_SALARY)
            .desiredSalary(UPDATED_DESIRED_SALARY)
            .hasContract(UPDATED_HAS_CONTRACT)
            .hired(UPDATED_HIRED)
            .rate(UPDATED_RATE);

        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCandidate))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCandidateToMatchAllProperties(updatedCandidate);
    }

    @Test
    @Transactional
    void putNonExistingCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidate.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate.firstName(UPDATED_FIRST_NAME);

        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidate))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCandidate, candidate),
            getPersistedCandidate(candidate)
        );
    }

    @Test
    @Transactional
    void fullUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .fullName(UPDATED_FULL_NAME)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .currentSalary(UPDATED_CURRENT_SALARY)
            .desiredSalary(UPDATED_DESIRED_SALARY)
            .hasContract(UPDATED_HAS_CONTRACT)
            .hired(UPDATED_HIRED)
            .rate(UPDATED_RATE);

        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidate))
            )
            .andExpect(status().isOk());

        // Validate the Candidate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidateUpdatableFieldsEquals(partialUpdatedCandidate, getPersistedCandidate(partialUpdatedCandidate));
    }

    @Test
    @Transactional
    void patchNonExistingCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(candidate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the candidate
        restCandidateMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return candidateRepository.count();
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

    protected Candidate getPersistedCandidate(Candidate candidate) {
        return candidateRepository.findById(candidate.getId()).orElseThrow();
    }

    protected void assertPersistedCandidateToMatchAllProperties(Candidate expectedCandidate) {
        assertCandidateAllPropertiesEquals(expectedCandidate, getPersistedCandidate(expectedCandidate));
    }

    protected void assertPersistedCandidateToMatchUpdatableProperties(Candidate expectedCandidate) {
        assertCandidateAllUpdatablePropertiesEquals(expectedCandidate, getPersistedCandidate(expectedCandidate));
    }
}
