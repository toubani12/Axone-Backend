package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.AppAccountTypeTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.ProviderTestSamples.*;
import static com.axone.hrsolution.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppAccount.class);
        AppAccount appAccount1 = getAppAccountSample1();
        AppAccount appAccount2 = new AppAccount();
        assertThat(appAccount1).isNotEqualTo(appAccount2);

        appAccount2.setId(appAccount1.getId());
        assertThat(appAccount1).isEqualTo(appAccount2);

        appAccount2 = getAppAccountSample2();
        assertThat(appAccount1).isNotEqualTo(appAccount2);
    }

    @Test
    void typeTest() throws Exception {
        AppAccount appAccount = getAppAccountRandomSampleGenerator();
        AppAccountType appAccountTypeBack = getAppAccountTypeRandomSampleGenerator();

        appAccount.addType(appAccountTypeBack);
        assertThat(appAccount.getTypes()).containsOnly(appAccountTypeBack);

        appAccount.removeType(appAccountTypeBack);
        assertThat(appAccount.getTypes()).doesNotContain(appAccountTypeBack);

        appAccount.types(new HashSet<>(Set.of(appAccountTypeBack)));
        assertThat(appAccount.getTypes()).containsOnly(appAccountTypeBack);

        appAccount.setTypes(new HashSet<>());
        assertThat(appAccount.getTypes()).doesNotContain(appAccountTypeBack);
    }

    @Test
    void providerTest() throws Exception {
        AppAccount appAccount = getAppAccountRandomSampleGenerator();
        Provider providerBack = getProviderRandomSampleGenerator();

        appAccount.addProvider(providerBack);
        assertThat(appAccount.getProviders()).containsOnly(providerBack);

        appAccount.removeProvider(providerBack);
        assertThat(appAccount.getProviders()).doesNotContain(providerBack);

        appAccount.providers(new HashSet<>(Set.of(providerBack)));
        assertThat(appAccount.getProviders()).containsOnly(providerBack);

        appAccount.setProviders(new HashSet<>());
        assertThat(appAccount.getProviders()).doesNotContain(providerBack);
    }

    @Test
    void relatedWalletTest() throws Exception {
        AppAccount appAccount = getAppAccountRandomSampleGenerator();
        Wallet walletBack = getWalletRandomSampleGenerator();

        appAccount.setRelatedWallet(walletBack);
        assertThat(appAccount.getRelatedWallet()).isEqualTo(walletBack);
        assertThat(walletBack.getRelatedToAccount()).isEqualTo(appAccount);

        appAccount.relatedWallet(null);
        assertThat(appAccount.getRelatedWallet()).isNull();
        assertThat(walletBack.getRelatedToAccount()).isNull();
    }

    @Test
    void ifEmployerTest() throws Exception {
        AppAccount appAccount = getAppAccountRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        appAccount.setIfEmployer(employerBack);
        assertThat(appAccount.getIfEmployer()).isEqualTo(employerBack);

        appAccount.ifEmployer(null);
        assertThat(appAccount.getIfEmployer()).isNull();
    }
}
