package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.ContractTypeTestSamples.*;
import static com.axone.hrsolution.domain.CriteriaTestSamples.*;
import static com.axone.hrsolution.domain.DomainTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.InterviewTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static com.axone.hrsolution.domain.RequestTestSamples.*;
import static com.axone.hrsolution.domain.TemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application.class);
        Application application1 = getApplicationSample1();
        Application application2 = new Application();
        assertThat(application1).isNotEqualTo(application2);

        application2.setId(application1.getId());
        assertThat(application1).isEqualTo(application2);

        application2 = getApplicationSample2();
        assertThat(application1).isNotEqualTo(application2);
    }

    @Test
    void contractsTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        application.addContracts(contractBack);
        assertThat(application.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getApplication()).isEqualTo(application);

        application.removeContracts(contractBack);
        assertThat(application.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getApplication()).isNull();

        application.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(application.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getApplication()).isEqualTo(application);

        application.setContracts(new HashSet<>());
        assertThat(application.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getApplication()).isNull();
    }

    @Test
    void interviewsTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Interview interviewBack = getInterviewRandomSampleGenerator();

        application.addInterviews(interviewBack);
        assertThat(application.getInterviews()).containsOnly(interviewBack);
        assertThat(interviewBack.getApplication()).isEqualTo(application);

        application.removeInterviews(interviewBack);
        assertThat(application.getInterviews()).doesNotContain(interviewBack);
        assertThat(interviewBack.getApplication()).isNull();

        application.interviews(new HashSet<>(Set.of(interviewBack)));
        assertThat(application.getInterviews()).containsOnly(interviewBack);
        assertThat(interviewBack.getApplication()).isEqualTo(application);

        application.setInterviews(new HashSet<>());
        assertThat(application.getInterviews()).doesNotContain(interviewBack);
        assertThat(interviewBack.getApplication()).isNull();
    }

    @Test
    void contractTypeTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        ContractType contractTypeBack = getContractTypeRandomSampleGenerator();

        application.addContractType(contractTypeBack);
        assertThat(application.getContractTypes()).containsOnly(contractTypeBack);

        application.removeContractType(contractTypeBack);
        assertThat(application.getContractTypes()).doesNotContain(contractTypeBack);

        application.contractTypes(new HashSet<>(Set.of(contractTypeBack)));
        assertThat(application.getContractTypes()).containsOnly(contractTypeBack);

        application.setContractTypes(new HashSet<>());
        assertThat(application.getContractTypes()).doesNotContain(contractTypeBack);
    }

    @Test
    void contractTemplateTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Template templateBack = getTemplateRandomSampleGenerator();

        application.addContractTemplate(templateBack);
        assertThat(application.getContractTemplates()).containsOnly(templateBack);

        application.removeContractTemplate(templateBack);
        assertThat(application.getContractTemplates()).doesNotContain(templateBack);

        application.contractTemplates(new HashSet<>(Set.of(templateBack)));
        assertThat(application.getContractTemplates()).containsOnly(templateBack);

        application.setContractTemplates(new HashSet<>());
        assertThat(application.getContractTemplates()).doesNotContain(templateBack);
    }

    @Test
    void criteriaTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Criteria criteriaBack = getCriteriaRandomSampleGenerator();

        application.addCriteria(criteriaBack);
        assertThat(application.getCriteria()).containsOnly(criteriaBack);

        application.removeCriteria(criteriaBack);
        assertThat(application.getCriteria()).doesNotContain(criteriaBack);

        application.criteria(new HashSet<>(Set.of(criteriaBack)));
        assertThat(application.getCriteria()).containsOnly(criteriaBack);

        application.setCriteria(new HashSet<>());
        assertThat(application.getCriteria()).doesNotContain(criteriaBack);
    }

    @Test
    void domainTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Domain domainBack = getDomainRandomSampleGenerator();

        application.addDomain(domainBack);
        assertThat(application.getDomains()).containsOnly(domainBack);

        application.removeDomain(domainBack);
        assertThat(application.getDomains()).doesNotContain(domainBack);

        application.domains(new HashSet<>(Set.of(domainBack)));
        assertThat(application.getDomains()).containsOnly(domainBack);

        application.setDomains(new HashSet<>());
        assertThat(application.getDomains()).doesNotContain(domainBack);
    }

    @Test
    void employerTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        application.setEmployer(employerBack);
        assertThat(application.getEmployer()).isEqualTo(employerBack);

        application.employer(null);
        assertThat(application.getEmployer()).isNull();
    }

    @Test
    void recruitersTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        application.addRecruiters(recruiterBack);
        assertThat(application.getRecruiters()).containsOnly(recruiterBack);
        assertThat(recruiterBack.getApplications()).containsOnly(application);

        application.removeRecruiters(recruiterBack);
        assertThat(application.getRecruiters()).doesNotContain(recruiterBack);
        assertThat(recruiterBack.getApplications()).doesNotContain(application);

        application.recruiters(new HashSet<>(Set.of(recruiterBack)));
        assertThat(application.getRecruiters()).containsOnly(recruiterBack);
        assertThat(recruiterBack.getApplications()).containsOnly(application);

        application.setRecruiters(new HashSet<>());
        assertThat(application.getRecruiters()).doesNotContain(recruiterBack);
        assertThat(recruiterBack.getApplications()).doesNotContain(application);
    }

    @Test
    void candidatesTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        application.addCandidates(candidateBack);
        assertThat(application.getCandidates()).containsOnly(candidateBack);
        assertThat(candidateBack.getApplications()).containsOnly(application);

        application.removeCandidates(candidateBack);
        assertThat(application.getCandidates()).doesNotContain(candidateBack);
        assertThat(candidateBack.getApplications()).doesNotContain(application);

        application.candidates(new HashSet<>(Set.of(candidateBack)));
        assertThat(application.getCandidates()).containsOnly(candidateBack);
        assertThat(candidateBack.getApplications()).containsOnly(application);

        application.setCandidates(new HashSet<>());
        assertThat(application.getCandidates()).doesNotContain(candidateBack);
        assertThat(candidateBack.getApplications()).doesNotContain(application);
    }

    @Test
    void requestTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        Request requestBack = getRequestRandomSampleGenerator();

        application.setRequest(requestBack);
        assertThat(application.getRequest()).isEqualTo(requestBack);
        assertThat(requestBack.getRelatedApplication()).isEqualTo(application);

        application.request(null);
        assertThat(application.getRequest()).isNull();
        assertThat(requestBack.getRelatedApplication()).isNull();
    }
}
