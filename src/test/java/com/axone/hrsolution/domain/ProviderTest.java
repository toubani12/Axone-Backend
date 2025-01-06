package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.ProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Provider.class);
        Provider provider1 = getProviderSample1();
        Provider provider2 = new Provider();
        assertThat(provider1).isNotEqualTo(provider2);

        provider2.setId(provider1.getId());
        assertThat(provider1).isEqualTo(provider2);

        provider2 = getProviderSample2();
        assertThat(provider1).isNotEqualTo(provider2);
    }

    @Test
    void appAccountTest() throws Exception {
        Provider provider = getProviderRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        provider.addAppAccount(appAccountBack);
        assertThat(provider.getAppAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getProviders()).containsOnly(provider);

        provider.removeAppAccount(appAccountBack);
        assertThat(provider.getAppAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getProviders()).doesNotContain(provider);

        provider.appAccounts(new HashSet<>(Set.of(appAccountBack)));
        assertThat(provider.getAppAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getProviders()).containsOnly(provider);

        provider.setAppAccounts(new HashSet<>());
        assertThat(provider.getAppAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getProviders()).doesNotContain(provider);
    }
}
