package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.DomainTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.NDATestSamples.*;
import static com.axone.hrsolution.domain.TemplateTestSamples.*;
import static com.axone.hrsolution.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employer.class);
        Employer employer1 = getEmployerSample1();
        Employer employer2 = new Employer();
        assertThat(employer1).isNotEqualTo(employer2);

        employer2.setId(employer1.getId());
        assertThat(employer1).isEqualTo(employer2);

        employer2 = getEmployerSample2();
        assertThat(employer1).isNotEqualTo(employer2);
    }

    @Test
    void walletTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        Wallet walletBack = getWalletRandomSampleGenerator();

        employer.setWallet(walletBack);
        assertThat(employer.getWallet()).isEqualTo(walletBack);

        employer.wallet(null);
        assertThat(employer.getWallet()).isNull();
    }

    @Test
    void operationalDomainTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        Domain domainBack = getDomainRandomSampleGenerator();

        employer.addOperationalDomain(domainBack);
        assertThat(employer.getOperationalDomains()).containsOnly(domainBack);
        assertThat(domainBack.getEmployer()).isEqualTo(employer);

        employer.removeOperationalDomain(domainBack);
        assertThat(employer.getOperationalDomains()).doesNotContain(domainBack);
        assertThat(domainBack.getEmployer()).isNull();

        employer.operationalDomains(new HashSet<>(Set.of(domainBack)));
        assertThat(employer.getOperationalDomains()).containsOnly(domainBack);
        assertThat(domainBack.getEmployer()).isEqualTo(employer);

        employer.setOperationalDomains(new HashSet<>());
        assertThat(employer.getOperationalDomains()).doesNotContain(domainBack);
        assertThat(domainBack.getEmployer()).isNull();
    }

    @Test
    void paymentAccountTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        employer.addPaymentAccount(appAccountBack);
        assertThat(employer.getPaymentAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getIfEmployer()).isEqualTo(employer);

        employer.removePaymentAccount(appAccountBack);
        assertThat(employer.getPaymentAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getIfEmployer()).isNull();

        employer.paymentAccounts(new HashSet<>(Set.of(appAccountBack)));
        assertThat(employer.getPaymentAccounts()).containsOnly(appAccountBack);
        assertThat(appAccountBack.getIfEmployer()).isEqualTo(employer);

        employer.setPaymentAccounts(new HashSet<>());
        assertThat(employer.getPaymentAccounts()).doesNotContain(appAccountBack);
        assertThat(appAccountBack.getIfEmployer()).isNull();
    }

    @Test
    void applicationTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        employer.addApplication(applicationBack);
        assertThat(employer.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getEmployer()).isEqualTo(employer);

        employer.removeApplication(applicationBack);
        assertThat(employer.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getEmployer()).isNull();

        employer.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(employer.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getEmployer()).isEqualTo(employer);

        employer.setApplications(new HashSet<>());
        assertThat(employer.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getEmployer()).isNull();
    }

    @Test
    void contractTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        employer.addContract(contractBack);
        assertThat(employer.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getEmployer()).isEqualTo(employer);

        employer.removeContract(contractBack);
        assertThat(employer.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getEmployer()).isNull();

        employer.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(employer.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getEmployer()).isEqualTo(employer);

        employer.setContracts(new HashSet<>());
        assertThat(employer.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getEmployer()).isNull();
    }

    @Test
    void templateTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        Template templateBack = getTemplateRandomSampleGenerator();

        employer.addTemplate(templateBack);
        assertThat(employer.getTemplates()).containsOnly(templateBack);
        assertThat(templateBack.getOwner()).isEqualTo(employer);

        employer.removeTemplate(templateBack);
        assertThat(employer.getTemplates()).doesNotContain(templateBack);
        assertThat(templateBack.getOwner()).isNull();

        employer.templates(new HashSet<>(Set.of(templateBack)));
        assertThat(employer.getTemplates()).containsOnly(templateBack);
        assertThat(templateBack.getOwner()).isEqualTo(employer);

        employer.setTemplates(new HashSet<>());
        assertThat(employer.getTemplates()).doesNotContain(templateBack);
        assertThat(templateBack.getOwner()).isNull();
    }

    @Test
    void ndaStatusTest() throws Exception {
        Employer employer = getEmployerRandomSampleGenerator();
        NDA nDABack = getNDARandomSampleGenerator();

        employer.addNdaStatus(nDABack);
        assertThat(employer.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getEmployer()).isEqualTo(employer);

        employer.removeNdaStatus(nDABack);
        assertThat(employer.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getEmployer()).isNull();

        employer.ndaStatuses(new HashSet<>(Set.of(nDABack)));
        assertThat(employer.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getEmployer()).isEqualTo(employer);

        employer.setNdaStatuses(new HashSet<>());
        assertThat(employer.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getEmployer()).isNull();
    }
}
