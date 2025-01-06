package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.DomainTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DomainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domain.class);
        Domain domain1 = getDomainSample1();
        Domain domain2 = new Domain();
        assertThat(domain1).isNotEqualTo(domain2);

        domain2.setId(domain1.getId());
        assertThat(domain1).isEqualTo(domain2);

        domain2 = getDomainSample2();
        assertThat(domain1).isNotEqualTo(domain2);
    }

    @Test
    void recruiterTest() throws Exception {
        Domain domain = getDomainRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        domain.addRecruiter(recruiterBack);
        assertThat(domain.getRecruiters()).containsOnly(recruiterBack);
        assertThat(recruiterBack.getOperationalDomains()).containsOnly(domain);

        domain.removeRecruiter(recruiterBack);
        assertThat(domain.getRecruiters()).doesNotContain(recruiterBack);
        assertThat(recruiterBack.getOperationalDomains()).doesNotContain(domain);

        domain.recruiters(new HashSet<>(Set.of(recruiterBack)));
        assertThat(domain.getRecruiters()).containsOnly(recruiterBack);
        assertThat(recruiterBack.getOperationalDomains()).containsOnly(domain);

        domain.setRecruiters(new HashSet<>());
        assertThat(domain.getRecruiters()).doesNotContain(recruiterBack);
        assertThat(recruiterBack.getOperationalDomains()).doesNotContain(domain);
    }

    @Test
    void candidateTest() throws Exception {
        Domain domain = getDomainRandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        domain.addCandidate(candidateBack);
        assertThat(domain.getCandidates()).containsOnly(candidateBack);
        assertThat(candidateBack.getDomains()).containsOnly(domain);

        domain.removeCandidate(candidateBack);
        assertThat(domain.getCandidates()).doesNotContain(candidateBack);
        assertThat(candidateBack.getDomains()).doesNotContain(domain);

        domain.candidates(new HashSet<>(Set.of(candidateBack)));
        assertThat(domain.getCandidates()).containsOnly(candidateBack);
        assertThat(candidateBack.getDomains()).containsOnly(domain);

        domain.setCandidates(new HashSet<>());
        assertThat(domain.getCandidates()).doesNotContain(candidateBack);
        assertThat(candidateBack.getDomains()).doesNotContain(domain);
    }

    @Test
    void applicationTest() throws Exception {
        Domain domain = getDomainRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        domain.addApplication(applicationBack);
        assertThat(domain.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getDomains()).containsOnly(domain);

        domain.removeApplication(applicationBack);
        assertThat(domain.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getDomains()).doesNotContain(domain);

        domain.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(domain.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getDomains()).containsOnly(domain);

        domain.setApplications(new HashSet<>());
        assertThat(domain.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getDomains()).doesNotContain(domain);
    }

    @Test
    void employerTest() throws Exception {
        Domain domain = getDomainRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        domain.setEmployer(employerBack);
        assertThat(domain.getEmployer()).isEqualTo(employerBack);

        domain.employer(null);
        assertThat(domain.getEmployer()).isNull();
    }
}
