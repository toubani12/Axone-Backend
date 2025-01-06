package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.InterviewAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Application;
import com.axone.hrsolution.domain.Interview;
import com.axone.hrsolution.repository.InterviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link InterviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterviewResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_ENTRY_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ENTRY_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_INVITATION_LINK = "AAAAAAAAAA";
    private static final String UPDATED_INVITATION_LINK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INTERVIEW_RESULT = false;
    private static final Boolean UPDATED_INTERVIEW_RESULT = true;

    private static final Float DEFAULT_RATE = 1F;
    private static final Float UPDATED_RATE = 2F;

    private static final LocalDate DEFAULT_STARTED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewMockMvc;

    private Interview interview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createEntity(EntityManager em) {
        Interview interview = new Interview()
            .label(DEFAULT_LABEL)
            .entryLink(DEFAULT_ENTRY_LINK)
            .invitationLink(DEFAULT_INVITATION_LINK)
            .interviewResult(DEFAULT_INTERVIEW_RESULT)
            .rate(DEFAULT_RATE)
            .startedAt(DEFAULT_STARTED_AT)
            .endedAt(DEFAULT_ENDED_AT)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        interview.setApplication(application);
        return interview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createUpdatedEntity(EntityManager em) {
        Interview interview = new Interview()
            .label(UPDATED_LABEL)
            .entryLink(UPDATED_ENTRY_LINK)
            .invitationLink(UPDATED_INVITATION_LINK)
            .interviewResult(UPDATED_INTERVIEW_RESULT)
            .rate(UPDATED_RATE)
            .startedAt(UPDATED_STARTED_AT)
            .endedAt(UPDATED_ENDED_AT)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createUpdatedEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        interview.setApplication(application);
        return interview;
    }

    @BeforeEach
    public void initTest() {
        interview = createEntity(em);
    }

    @Test
    @Transactional
    void createInterview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Interview
        var returnedInterview = om.readValue(
            restInterviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Interview.class
        );

        // Validate the Interview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInterviewUpdatableFieldsEquals(returnedInterview, getPersistedInterview(returnedInterview));
    }

    @Test
    @Transactional
    void createInterviewWithExistingId() throws Exception {
        // Create the Interview with an existing ID
        interview.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        interview.setLabel(null);

        // Create the Interview, which fails.

        restInterviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntryLinkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        interview.setEntryLink(null);

        // Create the Interview, which fails.

        restInterviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInvitationLinkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        interview.setInvitationLink(null);

        // Create the Interview, which fails.

        restInterviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterviews() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList
        restInterviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interview.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].entryLink").value(hasItem(DEFAULT_ENTRY_LINK)))
            .andExpect(jsonPath("$.[*].invitationLink").value(hasItem(DEFAULT_INVITATION_LINK)))
            .andExpect(jsonPath("$.[*].interviewResult").value(hasItem(DEFAULT_INTERVIEW_RESULT.booleanValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].endedAt").value(hasItem(DEFAULT_ENDED_AT.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get the interview
        restInterviewMockMvc
            .perform(get(ENTITY_API_URL_ID, interview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interview.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.entryLink").value(DEFAULT_ENTRY_LINK))
            .andExpect(jsonPath("$.invitationLink").value(DEFAULT_INVITATION_LINK))
            .andExpect(jsonPath("$.interviewResult").value(DEFAULT_INTERVIEW_RESULT.booleanValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.endedAt").value(DEFAULT_ENDED_AT.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingInterview() throws Exception {
        // Get the interview
        restInterviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the interview
        Interview updatedInterview = interviewRepository.findById(interview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInterview are not directly saved in db
        em.detach(updatedInterview);
        updatedInterview
            .label(UPDATED_LABEL)
            .entryLink(UPDATED_ENTRY_LINK)
            .invitationLink(UPDATED_INVITATION_LINK)
            .interviewResult(UPDATED_INTERVIEW_RESULT)
            .rate(UPDATED_RATE)
            .startedAt(UPDATED_STARTED_AT)
            .endedAt(UPDATED_ENDED_AT)
            .comments(UPDATED_COMMENTS);

        restInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInterview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInterview))
            )
            .andExpect(status().isOk());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInterviewToMatchAllProperties(updatedInterview);
    }

    @Test
    @Transactional
    void putNonExistingInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interview.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(interview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterviewWithPatch() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the interview using partial update
        Interview partialUpdatedInterview = new Interview();
        partialUpdatedInterview.setId(interview.getId());

        partialUpdatedInterview
            .label(UPDATED_LABEL)
            .entryLink(UPDATED_ENTRY_LINK)
            .rate(UPDATED_RATE)
            .startedAt(UPDATED_STARTED_AT)
            .endedAt(UPDATED_ENDED_AT)
            .comments(UPDATED_COMMENTS);

        restInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInterview))
            )
            .andExpect(status().isOk());

        // Validate the Interview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInterview, interview),
            getPersistedInterview(interview)
        );
    }

    @Test
    @Transactional
    void fullUpdateInterviewWithPatch() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the interview using partial update
        Interview partialUpdatedInterview = new Interview();
        partialUpdatedInterview.setId(interview.getId());

        partialUpdatedInterview
            .label(UPDATED_LABEL)
            .entryLink(UPDATED_ENTRY_LINK)
            .invitationLink(UPDATED_INVITATION_LINK)
            .interviewResult(UPDATED_INTERVIEW_RESULT)
            .rate(UPDATED_RATE)
            .startedAt(UPDATED_STARTED_AT)
            .endedAt(UPDATED_ENDED_AT)
            .comments(UPDATED_COMMENTS);

        restInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInterview))
            )
            .andExpect(status().isOk());

        // Validate the Interview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterviewUpdatableFieldsEquals(partialUpdatedInterview, getPersistedInterview(partialUpdatedInterview));
    }

    @Test
    @Transactional
    void patchNonExistingInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(interview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(interview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        interview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(interview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Interview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the interview
        restInterviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, interview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return interviewRepository.count();
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

    protected Interview getPersistedInterview(Interview interview) {
        return interviewRepository.findById(interview.getId()).orElseThrow();
    }

    protected void assertPersistedInterviewToMatchAllProperties(Interview expectedInterview) {
        assertInterviewAllPropertiesEquals(expectedInterview, getPersistedInterview(expectedInterview));
    }

    protected void assertPersistedInterviewToMatchUpdatableProperties(Interview expectedInterview) {
        assertInterviewAllUpdatablePropertiesEquals(expectedInterview, getPersistedInterview(expectedInterview));
    }
}
