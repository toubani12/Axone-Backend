package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.RecruiterAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Domain;
import com.axone.hrsolution.domain.Recruiter;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.domain.Wallet;
import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.axone.hrsolution.repository.RecruiterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link RecruiterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecruiterResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROFILE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final UserRole DEFAULT_ROLE = UserRole.ADMIN;
    private static final UserRole UPDATED_ROLE = UserRole.RECRUITER;

    private static final UserStatus DEFAULT_STATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_STATUS = UserStatus.DEACTIVATED;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN_URL = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_APPROVED_EXPERIENCE = false;
    private static final Boolean UPDATED_APPROVED_EXPERIENCE = true;

    private static final Float DEFAULT_SCORE = 1F;
    private static final Float UPDATED_SCORE = 2F;

    private static final String ENTITY_API_URL = "/api/recruiters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Mock
    private RecruiterRepository recruiterRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruiterMockMvc;

    private Recruiter recruiter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recruiter createEntity(EntityManager em) {
        Recruiter recruiter = new Recruiter()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .profileImageContentType(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)
            .address(DEFAULT_ADDRESS)
            .role(DEFAULT_ROLE)
            .status(DEFAULT_STATUS)
            .name(DEFAULT_NAME)
            .label(DEFAULT_LABEL)
            .linkedinUrl(DEFAULT_LINKEDIN_URL)
            .approvedExperience(DEFAULT_APPROVED_EXPERIENCE)
            .score(DEFAULT_SCORE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        recruiter.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        recruiter.setWallet(wallet);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        recruiter.getOperationalDomains().add(domain);
        return recruiter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recruiter createUpdatedEntity(EntityManager em) {
        Recruiter recruiter = new Recruiter()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .approvedExperience(UPDATED_APPROVED_EXPERIENCE)
            .score(UPDATED_SCORE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        recruiter.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createUpdatedEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        recruiter.setWallet(wallet);
        // Add required entity
        Domain domain;
        if (TestUtil.findAll(em, Domain.class).isEmpty()) {
            domain = DomainResourceIT.createUpdatedEntity(em);
            em.persist(domain);
            em.flush();
        } else {
            domain = TestUtil.findAll(em, Domain.class).get(0);
        }
        recruiter.getOperationalDomains().add(domain);
        return recruiter;
    }

    @BeforeEach
    public void initTest() {
        recruiter = createEntity(em);
    }

    @Test
    @Transactional
    void createRecruiter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recruiter
        var returnedRecruiter = om.readValue(
            restRecruiterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Recruiter.class
        );

        // Validate the Recruiter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecruiterUpdatableFieldsEquals(returnedRecruiter, getPersistedRecruiter(returnedRecruiter));
    }

    @Test
    @Transactional
    void createRecruiterWithExistingId() throws Exception {
        // Create the Recruiter with an existing ID
        recruiter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruiter.setRole(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruiter.setStatus(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruiter.setLabel(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLinkedinUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recruiter.setLinkedinUrl(null);

        // Create the Recruiter, which fails.

        restRecruiterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruiters() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get all the recruiterList
        restRecruiterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruiter.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].linkedinUrl").value(hasItem(DEFAULT_LINKEDIN_URL)))
            .andExpect(jsonPath("$.[*].approvedExperience").value(hasItem(DEFAULT_APPROVED_EXPERIENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecruitersWithEagerRelationshipsIsEnabled() throws Exception {
        when(recruiterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecruiterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recruiterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecruitersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recruiterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecruiterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recruiterRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecruiter() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        // Get the recruiter
        restRecruiterMockMvc
            .perform(get(ENTITY_API_URL_ID, recruiter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruiter.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.profileImageContentType").value(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profileImage").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.linkedinUrl").value(DEFAULT_LINKEDIN_URL))
            .andExpect(jsonPath("$.approvedExperience").value(DEFAULT_APPROVED_EXPERIENCE.booleanValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecruiter() throws Exception {
        // Get the recruiter
        restRecruiterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruiter() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruiter
        Recruiter updatedRecruiter = recruiterRepository.findById(recruiter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecruiter are not directly saved in db
        em.detach(updatedRecruiter);
        updatedRecruiter
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .approvedExperience(UPDATED_APPROVED_EXPERIENCE)
            .score(UPDATED_SCORE);

        restRecruiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecruiter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecruiter))
            )
            .andExpect(status().isOk());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecruiterToMatchAllProperties(updatedRecruiter);
    }

    @Test
    @Transactional
    void putNonExistingRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruiter.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recruiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruiterWithPatch() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruiter using partial update
        Recruiter partialUpdatedRecruiter = new Recruiter();
        partialUpdatedRecruiter.setId(recruiter.getId());

        partialUpdatedRecruiter
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .role(UPDATED_ROLE)
            .linkedinUrl(UPDATED_LINKEDIN_URL);

        restRecruiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruiter))
            )
            .andExpect(status().isOk());

        // Validate the Recruiter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruiterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecruiter, recruiter),
            getPersistedRecruiter(recruiter)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecruiterWithPatch() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recruiter using partial update
        Recruiter partialUpdatedRecruiter = new Recruiter();
        partialUpdatedRecruiter.setId(recruiter.getId());

        partialUpdatedRecruiter
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .approvedExperience(UPDATED_APPROVED_EXPERIENCE)
            .score(UPDATED_SCORE);

        restRecruiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecruiter))
            )
            .andExpect(status().isOk());

        // Validate the Recruiter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecruiterUpdatableFieldsEquals(partialUpdatedRecruiter, getPersistedRecruiter(partialUpdatedRecruiter));
    }

    @Test
    @Transactional
    void patchNonExistingRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruiter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recruiter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruiter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recruiter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruiterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recruiter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recruiter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruiter() throws Exception {
        // Initialize the database
        recruiterRepository.saveAndFlush(recruiter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recruiter
        restRecruiterMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruiter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recruiterRepository.count();
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

    protected Recruiter getPersistedRecruiter(Recruiter recruiter) {
        return recruiterRepository.findById(recruiter.getId()).orElseThrow();
    }

    protected void assertPersistedRecruiterToMatchAllProperties(Recruiter expectedRecruiter) {
        assertRecruiterAllPropertiesEquals(expectedRecruiter, getPersistedRecruiter(expectedRecruiter));
    }

    protected void assertPersistedRecruiterToMatchUpdatableProperties(Recruiter expectedRecruiter) {
        assertRecruiterAllUpdatablePropertiesEquals(expectedRecruiter, getPersistedRecruiter(expectedRecruiter));
    }
}
