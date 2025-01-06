package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.EmployerAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.domain.Wallet;
import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.axone.hrsolution.repository.EmployerRepository;
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
 * Integration tests for the {@link EmployerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployerRepository employerRepository;

    @Mock
    private EmployerRepository employerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployerMockMvc;

    private Employer employer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createEntity(EntityManager em) {
        Employer employer = new Employer()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .profileImageContentType(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)
            .address(DEFAULT_ADDRESS)
            .role(DEFAULT_ROLE)
            .status(DEFAULT_STATUS)
            .name(DEFAULT_NAME)
            .label(DEFAULT_LABEL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employer.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        employer.setWallet(wallet);
        return employer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createUpdatedEntity(EntityManager em) {
        Employer employer = new Employer()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employer.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createUpdatedEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        employer.setWallet(wallet);
        return employer;
    }

    @BeforeEach
    public void initTest() {
        employer = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employer
        var returnedEmployer = om.readValue(
            restEmployerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Employer.class
        );

        // Validate the Employer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployerUpdatableFieldsEquals(returnedEmployer, getPersistedEmployer(returnedEmployer));
    }

    @Test
    @Transactional
    void createEmployerWithExistingId() throws Exception {
        // Create the Employer with an existing ID
        employer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employer.setRole(null);

        // Create the Employer, which fails.

        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employer.setStatus(null);

        // Create the Employer, which fails.

        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employer.setLabel(null);

        // Create the Employer, which fails.

        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList
        restEmployerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployersWithEagerRelationshipsIsEnabled() throws Exception {
        when(employerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc
            .perform(get(ENTITY_API_URL_ID, employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.profileImageContentType").value(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profileImage").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer
        Employer updatedEmployer = employerRepository.findById(employer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployer are not directly saved in db
        em.detach(updatedEmployer);
        updatedEmployer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL);

        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployerToMatchAllProperties(updatedEmployer);
    }

    @Test
    @Transactional
    void putNonExistingEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployerWithPatch() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer using partial update
        Employer partialUpdatedEmployer = new Employer();
        partialUpdatedEmployer.setId(employer.getId());

        partialUpdatedEmployer.address(UPDATED_ADDRESS).role(UPDATED_ROLE).name(UPDATED_NAME).label(UPDATED_LABEL);

        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmployer, employer), getPersistedEmployer(employer));
    }

    @Test
    @Transactional
    void fullUpdateEmployerWithPatch() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer using partial update
        Employer partialUpdatedEmployer = new Employer();
        partialUpdatedEmployer.setId(employer.getId());

        partialUpdatedEmployer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL);

        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployerUpdatableFieldsEquals(partialUpdatedEmployer, getPersistedEmployer(partialUpdatedEmployer));
    }

    @Test
    @Transactional
    void patchNonExistingEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employer
        restEmployerMockMvc
            .perform(delete(ENTITY_API_URL_ID, employer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employerRepository.count();
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

    protected Employer getPersistedEmployer(Employer employer) {
        return employerRepository.findById(employer.getId()).orElseThrow();
    }

    protected void assertPersistedEmployerToMatchAllProperties(Employer expectedEmployer) {
        assertEmployerAllPropertiesEquals(expectedEmployer, getPersistedEmployer(expectedEmployer));
    }

    protected void assertPersistedEmployerToMatchUpdatableProperties(Employer expectedEmployer) {
        assertEmployerAllUpdatablePropertiesEquals(expectedEmployer, getPersistedEmployer(expectedEmployer));
    }
}
