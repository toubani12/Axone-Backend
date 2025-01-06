package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.WalletAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Wallet;
import com.axone.hrsolution.domain.enumeration.WalletStatus;
import com.axone.hrsolution.repository.WalletRepository;
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
 * Integration tests for the {@link WalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalletResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final WalletStatus DEFAULT_STATUS = WalletStatus.ACTIVE;
    private static final WalletStatus UPDATED_STATUS = WalletStatus.DEACTIVATED;

    private static final String ENTITY_API_URL = "/api/wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletMockMvc;

    private Wallet wallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createEntity(EntityManager em) {
        Wallet wallet = new Wallet().balance(DEFAULT_BALANCE).status(DEFAULT_STATUS);
        return wallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createUpdatedEntity(EntityManager em) {
        Wallet wallet = new Wallet().balance(UPDATED_BALANCE).status(UPDATED_STATUS);
        return wallet;
    }

    @BeforeEach
    public void initTest() {
        wallet = createEntity(em);
    }

    @Test
    @Transactional
    void createWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wallet
        var returnedWallet = om.readValue(
            restWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Wallet.class
        );

        // Validate the Wallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWalletUpdatableFieldsEquals(returnedWallet, getPersistedWallet(returnedWallet));
    }

    @Test
    @Transactional
    void createWalletWithExistingId() throws Exception {
        // Create the Wallet with an existing ID
        wallet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wallet.setBalance(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wallet.setStatus(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWallets() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get the wallet
        restWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wallet.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWallet() throws Exception {
        // Get the wallet
        restWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findById(wallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWallet are not directly saved in db
        em.detach(updatedWallet);
        updatedWallet.balance(UPDATED_BALANCE).status(UPDATED_STATUS);

        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletToMatchAllProperties(updatedWallet);
    }

    @Test
    @Transactional
    void putNonExistingWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(put(ENTITY_API_URL_ID, wallet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.balance(UPDATED_BALANCE).status(UPDATED_STATUS);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWallet, wallet), getPersistedWallet(wallet));
    }

    @Test
    @Transactional
    void fullUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.balance(UPDATED_BALANCE).status(UPDATED_STATUS);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletUpdatableFieldsEquals(partialUpdatedWallet, getPersistedWallet(partialUpdatedWallet));
    }

    @Test
    @Transactional
    void patchNonExistingWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wallet.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wallet
        restWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, wallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletRepository.count();
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

    protected Wallet getPersistedWallet(Wallet wallet) {
        return walletRepository.findById(wallet.getId()).orElseThrow();
    }

    protected void assertPersistedWalletToMatchAllProperties(Wallet expectedWallet) {
        assertWalletAllPropertiesEquals(expectedWallet, getPersistedWallet(expectedWallet));
    }

    protected void assertPersistedWalletToMatchUpdatableProperties(Wallet expectedWallet) {
        assertWalletAllUpdatablePropertiesEquals(expectedWallet, getPersistedWallet(expectedWallet));
    }
}
