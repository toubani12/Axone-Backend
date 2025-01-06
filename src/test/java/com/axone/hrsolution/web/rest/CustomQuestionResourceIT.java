package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.CustomQuestionAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.CustomQuestion;
import com.axone.hrsolution.repository.CustomQuestionRepository;
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
 * Integration tests for the {@link CustomQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomQuestionResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_CORRECT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_CORRECT_ANSWER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/custom-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomQuestionRepository customQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomQuestionMockMvc;

    private CustomQuestion customQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomQuestion createEntity(EntityManager em) {
        CustomQuestion customQuestion = new CustomQuestion()
            .question(DEFAULT_QUESTION)
            .answer(DEFAULT_ANSWER)
            .correctAnswer(DEFAULT_CORRECT_ANSWER);
        return customQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomQuestion createUpdatedEntity(EntityManager em) {
        CustomQuestion customQuestion = new CustomQuestion()
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .correctAnswer(UPDATED_CORRECT_ANSWER);
        return customQuestion;
    }

    @BeforeEach
    public void initTest() {
        customQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomQuestion
        var returnedCustomQuestion = om.readValue(
            restCustomQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customQuestion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomQuestion.class
        );

        // Validate the CustomQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomQuestionUpdatableFieldsEquals(returnedCustomQuestion, getPersistedCustomQuestion(returnedCustomQuestion));
    }

    @Test
    @Transactional
    void createCustomQuestionWithExistingId() throws Exception {
        // Create the CustomQuestion with an existing ID
        customQuestion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuestionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customQuestion.setQuestion(null);

        // Create the CustomQuestion, which fails.

        restCustomQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customQuestion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomQuestions() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        // Get all the customQuestionList
        restCustomQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].correctAnswer").value(hasItem(DEFAULT_CORRECT_ANSWER)));
    }

    @Test
    @Transactional
    void getCustomQuestion() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        // Get the customQuestion
        restCustomQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, customQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customQuestion.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.correctAnswer").value(DEFAULT_CORRECT_ANSWER));
    }

    @Test
    @Transactional
    void getNonExistingCustomQuestion() throws Exception {
        // Get the customQuestion
        restCustomQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomQuestion() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customQuestion
        CustomQuestion updatedCustomQuestion = customQuestionRepository.findById(customQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomQuestion are not directly saved in db
        em.detach(updatedCustomQuestion);
        updatedCustomQuestion.question(UPDATED_QUESTION).answer(UPDATED_ANSWER).correctAnswer(UPDATED_CORRECT_ANSWER);

        restCustomQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomQuestion))
            )
            .andExpect(status().isOk());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomQuestionToMatchAllProperties(updatedCustomQuestion);
    }

    @Test
    @Transactional
    void putNonExistingCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customQuestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomQuestionWithPatch() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customQuestion using partial update
        CustomQuestion partialUpdatedCustomQuestion = new CustomQuestion();
        partialUpdatedCustomQuestion.setId(customQuestion.getId());

        partialUpdatedCustomQuestion.answer(UPDATED_ANSWER).correctAnswer(UPDATED_CORRECT_ANSWER);

        restCustomQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomQuestion))
            )
            .andExpect(status().isOk());

        // Validate the CustomQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomQuestion, customQuestion),
            getPersistedCustomQuestion(customQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomQuestionWithPatch() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customQuestion using partial update
        CustomQuestion partialUpdatedCustomQuestion = new CustomQuestion();
        partialUpdatedCustomQuestion.setId(customQuestion.getId());

        partialUpdatedCustomQuestion.question(UPDATED_QUESTION).answer(UPDATED_ANSWER).correctAnswer(UPDATED_CORRECT_ANSWER);

        restCustomQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomQuestion))
            )
            .andExpect(status().isOk());

        // Validate the CustomQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomQuestionUpdatableFieldsEquals(partialUpdatedCustomQuestion, getPersistedCustomQuestion(partialUpdatedCustomQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customQuestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomQuestion() throws Exception {
        // Initialize the database
        customQuestionRepository.saveAndFlush(customQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customQuestion
        restCustomQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, customQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customQuestionRepository.count();
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

    protected CustomQuestion getPersistedCustomQuestion(CustomQuestion customQuestion) {
        return customQuestionRepository.findById(customQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedCustomQuestionToMatchAllProperties(CustomQuestion expectedCustomQuestion) {
        assertCustomQuestionAllPropertiesEquals(expectedCustomQuestion, getPersistedCustomQuestion(expectedCustomQuestion));
    }

    protected void assertPersistedCustomQuestionToMatchUpdatableProperties(CustomQuestion expectedCustomQuestion) {
        assertCustomQuestionAllUpdatablePropertiesEquals(expectedCustomQuestion, getPersistedCustomQuestion(expectedCustomQuestion));
    }
}
