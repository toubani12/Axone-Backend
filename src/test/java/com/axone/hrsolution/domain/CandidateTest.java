package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.DomainTestSamples.*;
import static com.axone.hrsolution.domain.InterviewTestSamples.*;
import static com.axone.hrsolution.domain.NDATestSamples.*;
import static com.axone.hrsolution.domain.ResumeTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CandidateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidate.class);
        Candidate candidate1 = getCandidateSample1();
        Candidate candidate2 = new Candidate();
        assertThat(candidate1).isNotEqualTo(candidate2);

        candidate2.setId(candidate1.getId());
        assertThat(candidate1).isEqualTo(candidate2);

        candidate2 = getCandidateSample2();
        assertThat(candidate1).isNotEqualTo(candidate2);
    }

    @Test
    void techCVTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        candidate.setTechCV(technicalCVBack);
        assertThat(candidate.getTechCV()).isEqualTo(technicalCVBack);

        candidate.techCV(null);
        assertThat(candidate.getTechCV()).isNull();
    }

    @Test
    void interviewResultTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        Interview interviewBack = getInterviewRandomSampleGenerator();

        candidate.addInterviewResult(interviewBack);
        assertThat(candidate.getInterviewResults()).containsOnly(interviewBack);
        assertThat(interviewBack.getAttendee()).isEqualTo(candidate);

        candidate.removeInterviewResult(interviewBack);
        assertThat(candidate.getInterviewResults()).doesNotContain(interviewBack);
        assertThat(interviewBack.getAttendee()).isNull();

        candidate.interviewResults(new HashSet<>(Set.of(interviewBack)));
        assertThat(candidate.getInterviewResults()).containsOnly(interviewBack);
        assertThat(interviewBack.getAttendee()).isEqualTo(candidate);

        candidate.setInterviewResults(new HashSet<>());
        assertThat(candidate.getInterviewResults()).doesNotContain(interviewBack);
        assertThat(interviewBack.getAttendee()).isNull();
    }

    @Test
    void candidateResumeTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        Resume resumeBack = getResumeRandomSampleGenerator();

        candidate.addCandidateResume(resumeBack);
        assertThat(candidate.getCandidateResumes()).containsOnly(resumeBack);
        assertThat(resumeBack.getOwner()).isEqualTo(candidate);

        candidate.removeCandidateResume(resumeBack);
        assertThat(candidate.getCandidateResumes()).doesNotContain(resumeBack);
        assertThat(resumeBack.getOwner()).isNull();

        candidate.candidateResumes(new HashSet<>(Set.of(resumeBack)));
        assertThat(candidate.getCandidateResumes()).containsOnly(resumeBack);
        assertThat(resumeBack.getOwner()).isEqualTo(candidate);

        candidate.setCandidateResumes(new HashSet<>());
        assertThat(candidate.getCandidateResumes()).doesNotContain(resumeBack);
        assertThat(resumeBack.getOwner()).isNull();
    }

    @Test
    void domainTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        Domain domainBack = getDomainRandomSampleGenerator();

        candidate.addDomain(domainBack);
        assertThat(candidate.getDomains()).containsOnly(domainBack);

        candidate.removeDomain(domainBack);
        assertThat(candidate.getDomains()).doesNotContain(domainBack);

        candidate.domains(new HashSet<>(Set.of(domainBack)));
        assertThat(candidate.getDomains()).containsOnly(domainBack);

        candidate.setDomains(new HashSet<>());
        assertThat(candidate.getDomains()).doesNotContain(domainBack);
    }

    @Test
    void applicationsTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        candidate.addApplications(applicationBack);
        assertThat(candidate.getApplications()).containsOnly(applicationBack);

        candidate.removeApplications(applicationBack);
        assertThat(candidate.getApplications()).doesNotContain(applicationBack);

        candidate.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(candidate.getApplications()).containsOnly(applicationBack);

        candidate.setApplications(new HashSet<>());
        assertThat(candidate.getApplications()).doesNotContain(applicationBack);
    }

    @Test
    void contractTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        candidate.setContract(contractBack);
        assertThat(candidate.getContract()).isEqualTo(contractBack);
        assertThat(contractBack.getCandidate()).isEqualTo(candidate);

        candidate.contract(null);
        assertThat(candidate.getContract()).isNull();
        assertThat(contractBack.getCandidate()).isNull();
    }

    @Test
    void ndaStatusTest() throws Exception {
        Candidate candidate = getCandidateRandomSampleGenerator();
        NDA nDABack = getNDARandomSampleGenerator();

        candidate.addNdaStatus(nDABack);
        assertThat(candidate.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getCandidate()).isEqualTo(candidate);

        candidate.removeNdaStatus(nDABack);
        assertThat(candidate.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getCandidate()).isNull();

        candidate.ndaStatuses(new HashSet<>(Set.of(nDABack)));
        assertThat(candidate.getNdaStatuses()).containsOnly(nDABack);
        assertThat(nDABack.getCandidate()).isEqualTo(candidate);

        candidate.setNdaStatuses(new HashSet<>());
        assertThat(candidate.getNdaStatuses()).doesNotContain(nDABack);
        assertThat(nDABack.getCandidate()).isNull();
    }
}
