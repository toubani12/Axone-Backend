package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AdminTestSamples.*;
import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static com.axone.hrsolution.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wallet.class);
        Wallet wallet1 = getWalletSample1();
        Wallet wallet2 = new Wallet();
        assertThat(wallet1).isNotEqualTo(wallet2);

        wallet2.setId(wallet1.getId());
        assertThat(wallet1).isEqualTo(wallet2);

        wallet2 = getWalletSample2();
        assertThat(wallet1).isNotEqualTo(wallet2);
    }

    @Test
    void relatedToAccountTest() throws Exception {
        Wallet wallet = getWalletRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        wallet.setRelatedToAccount(appAccountBack);
        assertThat(wallet.getRelatedToAccount()).isEqualTo(appAccountBack);

        wallet.relatedToAccount(null);
        assertThat(wallet.getRelatedToAccount()).isNull();
    }

    @Test
    void recruiterTest() throws Exception {
        Wallet wallet = getWalletRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        wallet.setRecruiter(recruiterBack);
        assertThat(wallet.getRecruiter()).isEqualTo(recruiterBack);
        assertThat(recruiterBack.getWallet()).isEqualTo(wallet);

        wallet.recruiter(null);
        assertThat(wallet.getRecruiter()).isNull();
        assertThat(recruiterBack.getWallet()).isNull();
    }

    @Test
    void employerTest() throws Exception {
        Wallet wallet = getWalletRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        wallet.setEmployer(employerBack);
        assertThat(wallet.getEmployer()).isEqualTo(employerBack);
        assertThat(employerBack.getWallet()).isEqualTo(wallet);

        wallet.employer(null);
        assertThat(wallet.getEmployer()).isNull();
        assertThat(employerBack.getWallet()).isNull();
    }

    @Test
    void adminTest() throws Exception {
        Wallet wallet = getWalletRandomSampleGenerator();
        Admin adminBack = getAdminRandomSampleGenerator();

        wallet.setAdmin(adminBack);
        assertThat(wallet.getAdmin()).isEqualTo(adminBack);
        assertThat(adminBack.getSystemWallet()).isEqualTo(wallet);

        wallet.admin(null);
        assertThat(wallet.getAdmin()).isNull();
        assertThat(adminBack.getSystemWallet()).isNull();
    }
}
