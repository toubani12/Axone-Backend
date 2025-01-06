package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.DomainTestSamples.*;
import static com.axone.hrsolution.domain.NDATestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static com.axone.hrsolution.domain.RequestTestSamples.*;
import static com.axone.hrsolution.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecruiterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recruiter.class);
        Recruiter recruiter1 = getRecruiterSample1();
        Recruiter recruiter2 = new Recruiter();
        assertThat(recruiter1).isNotEqualTo(recruiter2);

        recruiter2.setId(recruiter1.getId());
        assertThat(recruiter1).isEqualTo(recruiter2);

        recruiter2 = getRecruiterSample2();
        assertThat(recruiter1).isNotEqualTo(recruiter2);
    }

    @Test
    void walletTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        Wallet walletBack = getWalletRandomSampleGenerator();

        recruiter.setWallet(walletBack);
        assertThat(recruiter.getWallet()).isEqualTo(walletBack);

        recruiter.wallet(null);
        assertThat(recruiter.getWallet()).isNull();
    }

    @Test
    void requestsTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        Request requestBack = getRequestRandomSampleGenerator();

        recruiter.addRequests(requestBack);
        assertThat(recruiter.getRequests()).containsOnly(requestBack);
        assertThat(requestBack.getRecruiter()).isEqualTo(recruiter);

        recruiter.removeRequests(requestBack);
        assertThat(recruiter.getRequests()).doesNotContain(requestBack);
        assertThat(requestBack.getRecruiter()).isNull();

        recruiter.requests(new HashSet<>(Set.of(requestBack)));
        assertThat(recruiter.getRequests()).containsOnly(requestBack);
        assertThat(requestBack.getRecruiter()).isEqualTo(recruiter);

        recruiter.setRequests(new HashSet<>());
        assertThat(recruiter.getRequests()).doesNotContain(requestBack);
        assertThat(requestBack.getRecruiter()).isNull();
    }

    @Test
    void applicationsTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        recruiter.addApplications(applicationBack);
        assertThat(recruiter.getApplications()).containsOnly(applicationBack);

        recruiter.removeApplications(applicationBack);
        assertThat(recruiter.getApplications()).doesNotContain(applicationBack);

        recruiter.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(recruiter.getApplications()).containsOnly(applicationBack);

        recruiter.setApplications(new HashSet<>());
        assertThat(recruiter.getApplications()).doesNotContain(applicationBack);
    }

    @Test
    void operationalDomainTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        Domain domainBack = getDomainRandomSampleGenerator();

        recruiter.addOperationalDomain(domainBack);
        assertThat(recruiter.getOperationalDomains()).containsOnly(domainBack);

        recruiter.removeOperationalDomain(domainBack);
        assertThat(recruiter.getOperationalDomains()).doesNotContain(domainBack);

        recruiter.operationalDomains(new HashSet<>(Set.of(domainBack)));
        assertThat(recruiter.getOperationalDomains()).containsOnly(domainBack);

        recruiter.setOperationalDomains(new HashSet<>());
        assertThat(recruiter.getOperationalDomains()).doesNotContain(domainBack);
    }

    @Test
    void ndaStatusTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        NDA nDABack = getNDARandomSampleGenerator();

        recruiter.addNdaStatus(nDABack);
        assertThat(recruiter.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getMediator()).isEqualTo(recruiter);

        recruiter.removeNdaStatus(nDABack);
        assertThat(recruiter.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getMediator()).isNull();

        recruiter.ndaStatuses(new HashSet<>(Set.of(nDABack)));
        assertThat(recruiter.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getMediator()).isEqualTo(recruiter);

        recruiter.setNdaStatuses(new HashSet<>());
        assertThat(recruiter.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getMediator()).isNull();
    }

    @Test
    void contractsTest() throws Exception {
        Recruiter recruiter = getRecruiterRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        recruiter.addContracts(contractBack);
        assertThat(recruiter.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getRecruiter()).isEqualTo(recruiter);

        recruiter.removeContracts(contractBack);
        assertThat(recruiter.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getRecruiter()).isNull();

        recruiter.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(recruiter.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getRecruiter()).isEqualTo(recruiter);

        recruiter.setContracts(new HashSet<>());
        assertThat(recruiter.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getRecruiter()).isNull();
    }
}
