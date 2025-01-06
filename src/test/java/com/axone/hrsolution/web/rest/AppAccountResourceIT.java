package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.AppAccountAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.AppAccount;
import com.axone.hrsolution.domain.AppAccountType;
import com.axone.hrsolution.domain.Provider;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.repository.AppAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AppAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppAccountResourceIT {

    private static final Long DEFAULT_ACCOUNT_NUMBER = 1L;
    private static final Long UPDATED_ACCOUNT_NUMBER = 2L;

    private static final Long DEFAULT_CARD_NUMBER = 1L;
    private static final Long UPDATED_CARD_NUMBER = 2L;

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CVV = 1;
    private static final Integer UPDATED_CVV = 2;

    private static final String ENTITY_API_URL = "/api/app-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppAccountRepository appAccountRepository;

    @Mock
    private AppAccountRepository appAccountRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppAccountMockMvc;

    private AppAccount appAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppAccount createEntity(EntityManager em) {
        AppAccount appAccount = new AppAccount()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .endDate(DEFAULT_END_DATE)
            .cvv(DEFAULT_CVV);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appAccount.setRelatedUser(user);
        // Add required entity
        AppAccountType appAccountType;
        if (TestUtil.findAll(em, AppAccountType.class).isEmpty()) {
            appAccountType = AppAccountTypeResourceIT.createEntity(em);
            em.persist(appAccountType);
            em.flush();
        } else {
            appAccountType = TestUtil.findAll(em, AppAccountType.class).get(0);
        }
        appAccount.getTypes().add(appAccountType);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        appAccount.getProviders().add(provider);
        return appAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppAccount createUpdatedEntity(EntityManager em) {
        AppAccount appAccount = new AppAccount()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .cardNumber(UPDATED_CARD_NUMBER)
            .endDate(UPDATED_END_DATE)
            .cvv(UPDATED_CVV);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appAccount.setRelatedUser(user);
        // Add required entity
        AppAccountType appAccountType;
        if (TestUtil.findAll(em, AppAccountType.class).isEmpty()) {
            appAccountType = AppAccountTypeResourceIT.createUpdatedEntity(em);
            em.persist(appAccountType);
            em.flush();
        } else {
            appAccountType = TestUtil.findAll(em, AppAccountType.class).get(0);
        }
        appAccount.getTypes().add(appAccountType);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        appAccount.getProviders().add(provider);
        return appAccount;
    }

    @BeforeEach
    public void initTest() {
        appAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createAppAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppAccount
        var returnedAppAccount = om.readValue(
            restAppAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppAccount.class
        );

        // Validate the AppAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppAccountUpdatableFieldsEquals(returnedAppAccount, getPersistedAppAccount(returnedAppAccount));
    }

    @Test
    @Transactional
    void createAppAccountWithExistingId() throws Exception {
        // Create the AppAccount with an existing ID
        appAccount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccount)))
            .andExpect(status().isBadRequest());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appAccount.setAccountNumber(null);

        // Create the AppAccount, which fails.

        restAppAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppAccounts() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        // Get all the appAccountList
        restAppAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(appAccountRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appAccountRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(appAccountRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(appAccountRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAppAccount() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        // Get the appAccount
        restAppAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, appAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.intValue()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER.intValue()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.cvv").value(DEFAULT_CVV));
    }

    @Test
    @Transactional
    void getNonExistingAppAccount() throws Exception {
        // Get the appAccount
        restAppAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppAccount() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccount
        AppAccount updatedAppAccount = appAccountRepository.findById(appAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppAccount are not directly saved in db
        em.detach(updatedAppAccount);
        updatedAppAccount.accountNumber(UPDATED_ACCOUNT_NUMBER).cardNumber(UPDATED_CARD_NUMBER).endDate(UPDATED_END_DATE).cvv(UPDATED_CVV);

        restAppAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppAccount))
            )
            .andExpect(status().isOk());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppAccountToMatchAllProperties(updatedAppAccount);
    }

    @Test
    @Transactional
    void putNonExistingAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appAccount.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppAccountWithPatch() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccount using partial update
        AppAccount partialUpdatedAppAccount = new AppAccount();
        partialUpdatedAppAccount.setId(appAccount.getId());

        partialUpdatedAppAccount.cardNumber(UPDATED_CARD_NUMBER);

        restAppAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppAccount))
            )
            .andExpect(status().isOk());

        // Validate the AppAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppAccount, appAccount),
            getPersistedAppAccount(appAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppAccountWithPatch() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appAccount using partial update
        AppAccount partialUpdatedAppAccount = new AppAccount();
        partialUpdatedAppAccount.setId(appAccount.getId());

        partialUpdatedAppAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .cardNumber(UPDATED_CARD_NUMBER)
            .endDate(UPDATED_END_DATE)
            .cvv(UPDATED_CVV);

        restAppAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppAccount))
            )
            .andExpect(status().isOk());

        // Validate the AppAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppAccountUpdatableFieldsEquals(partialUpdatedAppAccount, getPersistedAppAccount(partialUpdatedAppAccount));
    }

    @Test
    @Transactional
    void patchNonExistingAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppAccount() throws Exception {
        // Initialize the database
        appAccountRepository.saveAndFlush(appAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appAccount
        restAppAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, appAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appAccountRepository.count();
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

    protected AppAccount getPersistedAppAccount(AppAccount appAccount) {
        return appAccountRepository.findById(appAccount.getId()).orElseThrow();
    }

    protected void assertPersistedAppAccountToMatchAllProperties(AppAccount expectedAppAccount) {
        assertAppAccountAllPropertiesEquals(expectedAppAccount, getPersistedAppAccount(expectedAppAccount));
    }

    protected void assertPersistedAppAccountToMatchUpdatableProperties(AppAccount expectedAppAccount) {
        assertAppAccountAllUpdatablePropertiesEquals(expectedAppAccount, getPersistedAppAccount(expectedAppAccount));
    }
}
