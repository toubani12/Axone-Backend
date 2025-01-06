package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.RequestAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Application;
import com.axone.hrsolution.domain.Recruiter;
import com.axone.hrsolution.domain.Request;
import com.axone.hrsolution.domain.enumeration.RequestStatus;
import com.axone.hrsolution.repository.RequestRepository;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestResourceIT {

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.PROCESSING;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.ACCEPTED;

    private static final String DEFAULT_EXPRESSION_OF_INTEREST = "AAAAAAAAAA";
    private static final String UPDATED_EXPRESSION_OF_INTEREST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestMockMvc;

    private Request request;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity(EntityManager em) {
        Request request = new Request().status(DEFAULT_STATUS).expressionOfInterest(DEFAULT_EXPRESSION_OF_INTEREST);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        request.setRelatedApplication(application);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        request.setRecruiter(recruiter);
        return request;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity(EntityManager em) {
        Request request = new Request().status(UPDATED_STATUS).expressionOfInterest(UPDATED_EXPRESSION_OF_INTEREST);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createUpdatedEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        request.setRelatedApplication(application);
        // Add required entity
        Recruiter recruiter;
        if (TestUtil.findAll(em, Recruiter.class).isEmpty()) {
            recruiter = RecruiterResourceIT.createUpdatedEntity(em);
            em.persist(recruiter);
            em.flush();
        } else {
            recruiter = TestUtil.findAll(em, Recruiter.class).get(0);
        }
        request.setRecruiter(recruiter);
        return request;
    }

    @BeforeEach
    public void initTest() {
        request = createEntity(em);
    }

    @Test
    @Transactional
    void createRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Request
        var returnedRequest = om.readValue(
            restRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Request.class
        );

        // Validate the Request in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRequestUpdatableFieldsEquals(returnedRequest, getPersistedRequest(returnedRequest));
    }

    @Test
    @Transactional
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        request.setStatus(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpressionOfInterestIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        request.setExpressionOfInterest(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expressionOfInterest").value(hasItem(DEFAULT_EXPRESSION_OF_INTEREST)));
    }

    @Test
    @Transactional
    void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expressionOfInterest").value(DEFAULT_EXPRESSION_OF_INTEREST));
    }

    @Test
    @Transactional
    void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRequest are not directly saved in db
        em.detach(updatedRequest);
        updatedRequest.status(UPDATED_STATUS).expressionOfInterest(UPDATED_EXPRESSION_OF_INTEREST);

        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRequestToMatchAllProperties(updatedRequest);
    }

    @Test
    @Transactional
    void putNonExistingRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL_ID, request.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRequest, request), getPersistedRequest(request));
    }

    @Test
    @Transactional
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.status(UPDATED_STATUS).expressionOfInterest(UPDATED_EXPRESSION_OF_INTEREST);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestUpdatableFieldsEquals(partialUpdatedRequest, getPersistedRequest(partialUpdatedRequest));
    }

    @Test
    @Transactional
    void patchNonExistingRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, request.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the request
        restRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, request.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return requestRepository.count();
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

    protected Request getPersistedRequest(Request request) {
        return requestRepository.findById(request.getId()).orElseThrow();
    }

    protected void assertPersistedRequestToMatchAllProperties(Request expectedRequest) {
        assertRequestAllPropertiesEquals(expectedRequest, getPersistedRequest(expectedRequest));
    }

    protected void assertPersistedRequestToMatchUpdatableProperties(Request expectedRequest) {
        assertRequestAllUpdatablePropertiesEquals(expectedRequest, getPersistedRequest(expectedRequest));
    }
}
