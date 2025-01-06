package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.AppTestAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.AppTest;
import com.axone.hrsolution.repository.AppTestRepository;
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
 * Integration tests for the {@link AppTestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppTestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INVITATION_LINK = "AAAAAAAAAA";
    private static final String UPDATED_INVITATION_LINK = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_SCORE = 1;
    private static final Integer UPDATED_TOTAL_SCORE = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Long DEFAULT_TEST_ID = 1L;
    private static final Long UPDATED_TEST_ID = 2L;

    private static final String DEFAULT_ALGORITHM = "AAAAAAAAAA";
    private static final String UPDATED_ALGORITHM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CODE_TEST = false;
    private static final Boolean UPDATED_IS_CODE_TEST = true;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String ENTITY_API_URL = "/api/app-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppTestRepository appTestRepository;

    @Mock
    private AppTestRepository appTestRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppTestMockMvc;

    private AppTest appTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppTest createEntity(EntityManager em) {
        AppTest appTest = new AppTest()
            .name(DEFAULT_NAME)
            .invitationLink(DEFAULT_INVITATION_LINK)
            .totalScore(DEFAULT_TOTAL_SCORE)
            .status(DEFAULT_STATUS)
            .completed(DEFAULT_COMPLETED)
            .testID(DEFAULT_TEST_ID)
            .algorithm(DEFAULT_ALGORITHM)
            .isCodeTest(DEFAULT_IS_CODE_TEST)
            .duration(DEFAULT_DURATION);
        return appTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppTest createUpdatedEntity(EntityManager em) {
        AppTest appTest = new AppTest()
            .name(UPDATED_NAME)
            .invitationLink(UPDATED_INVITATION_LINK)
            .totalScore(UPDATED_TOTAL_SCORE)
            .status(UPDATED_STATUS)
            .completed(UPDATED_COMPLETED)
            .testID(UPDATED_TEST_ID)
            .algorithm(UPDATED_ALGORITHM)
            .isCodeTest(UPDATED_IS_CODE_TEST)
            .duration(UPDATED_DURATION);
        return appTest;
    }

    @BeforeEach
    public void initTest() {
        appTest = createEntity(em);
    }

    @Test
    @Transactional
    void createAppTest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppTest
        var returnedAppTest = om.readValue(
            restAppTestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppTest.class
        );

        // Validate the AppTest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppTestUpdatableFieldsEquals(returnedAppTest, getPersistedAppTest(returnedAppTest));
    }

    @Test
    @Transactional
    void createAppTestWithExistingId() throws Exception {
        // Create the AppTest with an existing ID
        appTest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isBadRequest());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appTest.setName(null);

        // Create the AppTest, which fails.

        restAppTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInvitationLinkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appTest.setInvitationLink(null);

        // Create the AppTest, which fails.

        restAppTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppTests() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        // Get all the appTestList
        restAppTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].invitationLink").value(hasItem(DEFAULT_INVITATION_LINK)))
            .andExpect(jsonPath("$.[*].totalScore").value(hasItem(DEFAULT_TOTAL_SCORE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].testID").value(hasItem(DEFAULT_TEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM)))
            .andExpect(jsonPath("$.[*].isCodeTest").value(hasItem(DEFAULT_IS_CODE_TEST.booleanValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppTestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(appTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appTestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppTestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(appTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(appTestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAppTest() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        // Get the appTest
        restAppTestMockMvc
            .perform(get(ENTITY_API_URL_ID, appTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appTest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.invitationLink").value(DEFAULT_INVITATION_LINK))
            .andExpect(jsonPath("$.totalScore").value(DEFAULT_TOTAL_SCORE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.testID").value(DEFAULT_TEST_ID.intValue()))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM))
            .andExpect(jsonPath("$.isCodeTest").value(DEFAULT_IS_CODE_TEST.booleanValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingAppTest() throws Exception {
        // Get the appTest
        restAppTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppTest() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTest
        AppTest updatedAppTest = appTestRepository.findById(appTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppTest are not directly saved in db
        em.detach(updatedAppTest);
        updatedAppTest
            .name(UPDATED_NAME)
            .invitationLink(UPDATED_INVITATION_LINK)
            .totalScore(UPDATED_TOTAL_SCORE)
            .status(UPDATED_STATUS)
            .completed(UPDATED_COMPLETED)
            .testID(UPDATED_TEST_ID)
            .algorithm(UPDATED_ALGORITHM)
            .isCodeTest(UPDATED_IS_CODE_TEST)
            .duration(UPDATED_DURATION);

        restAppTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppTest))
            )
            .andExpect(status().isOk());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppTestToMatchAllProperties(updatedAppTest);
    }

    @Test
    @Transactional
    void putNonExistingAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(put(ENTITY_API_URL_ID, appTest.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isBadRequest());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppTestWithPatch() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTest using partial update
        AppTest partialUpdatedAppTest = new AppTest();
        partialUpdatedAppTest.setId(appTest.getId());

        partialUpdatedAppTest
            .invitationLink(UPDATED_INVITATION_LINK)
            .totalScore(UPDATED_TOTAL_SCORE)
            .testID(UPDATED_TEST_ID)
            .algorithm(UPDATED_ALGORITHM)
            .isCodeTest(UPDATED_IS_CODE_TEST);

        restAppTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppTest))
            )
            .andExpect(status().isOk());

        // Validate the AppTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppTestUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppTest, appTest), getPersistedAppTest(appTest));
    }

    @Test
    @Transactional
    void fullUpdateAppTestWithPatch() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appTest using partial update
        AppTest partialUpdatedAppTest = new AppTest();
        partialUpdatedAppTest.setId(appTest.getId());

        partialUpdatedAppTest
            .name(UPDATED_NAME)
            .invitationLink(UPDATED_INVITATION_LINK)
            .totalScore(UPDATED_TOTAL_SCORE)
            .status(UPDATED_STATUS)
            .completed(UPDATED_COMPLETED)
            .testID(UPDATED_TEST_ID)
            .algorithm(UPDATED_ALGORITHM)
            .isCodeTest(UPDATED_IS_CODE_TEST)
            .duration(UPDATED_DURATION);

        restAppTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppTest))
            )
            .andExpect(status().isOk());

        // Validate the AppTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppTestUpdatableFieldsEquals(partialUpdatedAppTest, getPersistedAppTest(partialUpdatedAppTest));
    }

    @Test
    @Transactional
    void patchNonExistingAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appTest.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppTest() throws Exception {
        // Initialize the database
        appTestRepository.saveAndFlush(appTest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appTest
        restAppTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, appTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appTestRepository.count();
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

    protected AppTest getPersistedAppTest(AppTest appTest) {
        return appTestRepository.findById(appTest.getId()).orElseThrow();
    }

    protected void assertPersistedAppTestToMatchAllProperties(AppTest expectedAppTest) {
        assertAppTestAllPropertiesEquals(expectedAppTest, getPersistedAppTest(expectedAppTest));
    }

    protected void assertPersistedAppTestToMatchUpdatableProperties(AppTest expectedAppTest) {
        assertAppTestAllUpdatablePropertiesEquals(expectedAppTest, getPersistedAppTest(expectedAppTest));
    }
}
