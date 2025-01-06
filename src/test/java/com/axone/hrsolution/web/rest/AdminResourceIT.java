package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.AdminAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Admin;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.domain.Wallet;
import com.axone.hrsolution.repository.AdminRepository;
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
 * Integration tests for the {@link AdminResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdminResourceIT {

    private static final String DEFAULT_SYSTEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/admins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdminRepository adminRepository;

    @Mock
    private AdminRepository adminRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminMockMvc;

    private Admin admin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Admin createEntity(EntityManager em) {
        Admin admin = new Admin().systemName(DEFAULT_SYSTEM_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        admin.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        admin.setSystemWallet(wallet);
        return admin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Admin createUpdatedEntity(EntityManager em) {
        Admin admin = new Admin().systemName(UPDATED_SYSTEM_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        admin.setRelatedUser(user);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createUpdatedEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        admin.setSystemWallet(wallet);
        return admin;
    }

    @BeforeEach
    public void initTest() {
        admin = createEntity(em);
    }

    @Test
    @Transactional
    void createAdmin() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Admin
        var returnedAdmin = om.readValue(
            restAdminMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(admin)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Admin.class
        );

        // Validate the Admin in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAdminUpdatableFieldsEquals(returnedAdmin, getPersistedAdmin(returnedAdmin));
    }

    @Test
    @Transactional
    void createAdminWithExistingId() throws Exception {
        // Create the Admin with an existing ID
        admin.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(admin)))
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSystemNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        admin.setSystemName(null);

        // Create the Admin, which fails.

        restAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(admin)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdmins() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get all the adminList
        restAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admin.getId().intValue())))
            .andExpect(jsonPath("$.[*].systemName").value(hasItem(DEFAULT_SYSTEM_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdminsWithEagerRelationshipsIsEnabled() throws Exception {
        when(adminRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdminMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(adminRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdminsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(adminRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdminMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(adminRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        // Get the admin
        restAdminMockMvc
            .perform(get(ENTITY_API_URL_ID, admin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(admin.getId().intValue()))
            .andExpect(jsonPath("$.systemName").value(DEFAULT_SYSTEM_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAdmin() throws Exception {
        // Get the admin
        restAdminMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the admin
        Admin updatedAdmin = adminRepository.findById(admin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdmin are not directly saved in db
        em.detach(updatedAdmin);
        updatedAdmin.systemName(UPDATED_SYSTEM_NAME);

        restAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdmin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAdmin))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdminToMatchAllProperties(updatedAdmin);
    }

    @Test
    @Transactional
    void putNonExistingAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(put(ENTITY_API_URL_ID, admin.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(admin)))
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(admin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(admin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminWithPatch() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the admin using partial update
        Admin partialUpdatedAdmin = new Admin();
        partialUpdatedAdmin.setId(admin.getId());

        partialUpdatedAdmin.systemName(UPDATED_SYSTEM_NAME);

        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdmin))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAdmin, admin), getPersistedAdmin(admin));
    }

    @Test
    @Transactional
    void fullUpdateAdminWithPatch() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the admin using partial update
        Admin partialUpdatedAdmin = new Admin();
        partialUpdatedAdmin.setId(admin.getId());

        partialUpdatedAdmin.systemName(UPDATED_SYSTEM_NAME);

        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdmin))
            )
            .andExpect(status().isOk());

        // Validate the Admin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminUpdatableFieldsEquals(partialUpdatedAdmin, getPersistedAdmin(partialUpdatedAdmin));
    }

    @Test
    @Transactional
    void patchNonExistingAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, admin.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(admin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(admin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdmin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        admin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(admin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Admin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdmin() throws Exception {
        // Initialize the database
        adminRepository.saveAndFlush(admin);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the admin
        restAdminMockMvc
            .perform(delete(ENTITY_API_URL_ID, admin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adminRepository.count();
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

    protected Admin getPersistedAdmin(Admin admin) {
        return adminRepository.findById(admin.getId()).orElseThrow();
    }

    protected void assertPersistedAdminToMatchAllProperties(Admin expectedAdmin) {
        assertAdminAllPropertiesEquals(expectedAdmin, getPersistedAdmin(expectedAdmin));
    }

    protected void assertPersistedAdminToMatchUpdatableProperties(Admin expectedAdmin) {
        assertAdminAllUpdatablePropertiesEquals(expectedAdmin, getPersistedAdmin(expectedAdmin));
    }
}
