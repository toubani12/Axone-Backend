package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.NDAAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Candidate;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.NDA;
import com.axone.hrsolution.domain.Recruiter;
import com.axone.hrsolution.domain.enumeration.NDAStatus;
import com.axone.hrsolution.repository.NDARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link NDAResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NDAResourceIT {

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final NDAStatus DEFAULT_STATUS = NDAStatus.PROCESSING;
    private static final NDAStatus UPDATED_STATUS = NDAStatus.SIGNED;

    private static final LocalDate DEFAULT_PERIOD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/ndas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NDARepository nDARepository;

    @Mock
    private NDARepository nDARepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNDAMockMvc;

    private NDA nDA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NDA createEntity(EntityManager em) {
        NDA nDA = new NDA()
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE)
            .status(DEFAULT_STATUS)
            .period(DEFAULT_PERIOD);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        nDA.setEmployer(employer);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        nDA.setMediator(recruiter);
        // Add required entity
        Candidate candidate;
        if (TestUtil.findAll(em, Candidate.class).isEmpty()) {
            candidate = CandidateResourceIT.createEntity(em);
            em.persist(candidate);
            em.flush();
        } else {
            candidate = TestUtil.findAll(em, Candidate.class).get(0);
        }
        nDA.setCandidate(candidate);
        return nDA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NDA createUpdatedEntity(EntityManager em) {
        NDA nDA = new NDA()
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .period(UPDATED_PERIOD);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createUpdatedEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        nDA.setEmployer(employer);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createUpdatedEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        nDA.setMediator(recruiter);
        // Add required entity
        Candidate candidate;
        if (TestUtil.findAll(em, Candidate.class).isEmpty()) {
            candidate = CandidateResourceIT.createUpdatedEntity(em);
            em.persist(candidate);
            em.flush();
        } else {
            candidate = TestUtil.findAll(em, Candidate.class).get(0);
        }
        nDA.setCandidate(candidate);
        return nDA;
    }

    @BeforeEach
    public void initTest() {
        nDA = createEntity(em);
    }

    @Test
    @Transactional
    void createNDA() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NDA
        var returnedNDA = om.readValue(
            restNDAMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NDA.class
        );

        // Validate the NDA in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNDAUpdatableFieldsEquals(returnedNDA, getPersistedNDA(returnedNDA));
    }

    @Test
    @Transactional
    void createNDAWithExistingId() throws Exception {
        // Create the NDA with an existing ID
        nDA.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNDAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isBadRequest());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nDA.setStatus(null);

        // Create the NDA, which fails.

        restNDAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nDA.setPeriod(null);

        // Create the NDA, which fails.

        restNDAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNDAS() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        // Get all the nDAList
        restNDAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nDA.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNDASWithEagerRelationshipsIsEnabled() throws Exception {
        when(nDARepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNDAMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nDARepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNDASWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nDARepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNDAMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nDARepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNDA() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        // Get the nDA
        restNDAMockMvc
            .perform(get(ENTITY_API_URL_ID, nDA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nDA.getId().intValue()))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNDA() throws Exception {
        // Get the nDA
        restNDAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNDA() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nDA
        NDA updatedNDA = nDARepository.findById(nDA.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNDA are not directly saved in db
        em.detach(updatedNDA);
        updatedNDA
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .period(UPDATED_PERIOD);

        restNDAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNDA.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedNDA))
            )
            .andExpect(status().isOk());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNDAToMatchAllProperties(updatedNDA);
    }

    @Test
    @Transactional
    void putNonExistingNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(put(ENTITY_API_URL_ID, nDA.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isBadRequest());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nDA))
            )
            .andExpect(status().isBadRequest());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNDAWithPatch() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nDA using partial update
        NDA partialUpdatedNDA = new NDA();
        partialUpdatedNDA.setId(nDA.getId());

        partialUpdatedNDA.status(UPDATED_STATUS).period(UPDATED_PERIOD);

        restNDAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNDA.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNDA))
            )
            .andExpect(status().isOk());

        // Validate the NDA in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNDAUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNDA, nDA), getPersistedNDA(nDA));
    }

    @Test
    @Transactional
    void fullUpdateNDAWithPatch() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nDA using partial update
        NDA partialUpdatedNDA = new NDA();
        partialUpdatedNDA.setId(nDA.getId());

        partialUpdatedNDA
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .period(UPDATED_PERIOD);

        restNDAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNDA.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNDA))
            )
            .andExpect(status().isOk());

        // Validate the NDA in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNDAUpdatableFieldsEquals(partialUpdatedNDA, getPersistedNDA(partialUpdatedNDA));
    }

    @Test
    @Transactional
    void patchNonExistingNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(patch(ENTITY_API_URL_ID, nDA.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isBadRequest());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nDA))
            )
            .andExpect(status().isBadRequest());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNDA() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nDA.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNDAMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nDA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NDA in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNDA() throws Exception {
        // Initialize the database
        nDARepository.saveAndFlush(nDA);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nDA
        restNDAMockMvc.perform(delete(ENTITY_API_URL_ID, nDA.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nDARepository.count();
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

    protected NDA getPersistedNDA(NDA nDA) {
        return nDARepository.findById(nDA.getId()).orElseThrow();
    }

    protected void assertPersistedNDAToMatchAllProperties(NDA expectedNDA) {
        assertNDAAllPropertiesEquals(expectedNDA, getPersistedNDA(expectedNDA));
    }

    protected void assertPersistedNDAToMatchUpdatableProperties(NDA expectedNDA) {
        assertNDAAllUpdatablePropertiesEquals(expectedNDA, getPersistedNDA(expectedNDA));
    }
}
