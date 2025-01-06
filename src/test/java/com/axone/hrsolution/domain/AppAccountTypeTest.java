package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.AppAccountTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppAccountTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppAccountType.class);
        AppAccountType appAccountType1 = getAppAccountTypeSample1();
        AppAccountType appAccountType2 = new AppAccountType();
        assertThat(appAccountType1).isNotEqualTo(appAccountType2);

        appAccountType2.setId(appAccountType1.getId());
        assertThat(appAccountType1).isEqualTo(appAccountType2);

        appAccountType2 = getAppAccountTypeSample2();
        assertThat(appAccountType1).isNotEqualTo(appAccountType2);
    }

    @Test
    void appAccountTest() throws Exception {
        AppAccountType appAccountType = getAppAccountTypeRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        appAccountType.addAppAccount(appAccountBack);
        assertThat(appAccountType.getAppAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getTypes()).containsOnly(appAccountType);

        appAccountType.removeAppAccount(appAccountBack);
        assertThat(appAccountType.getAppAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getTypes()).doesNotContain(appAccountType);

        appAccountType.appAccounts(new HashSet<>(Set.of(appAccountBack)));
        assertThat(appAccountType.getAppAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getTypes()).containsOnly(appAccountType);

        appAccountType.setAppAccounts(new HashSet<>());
        assertThat(appAccountType.getAppAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getTypes()).doesNotContain(appAccountType);
    }
}
