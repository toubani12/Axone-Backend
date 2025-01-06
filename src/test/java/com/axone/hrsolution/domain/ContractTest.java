package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static com.axone.hrsolution.domain.TemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void templateTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Template templateBack = getTemplateRandomSampleGenerator();

        contract.setTemplate(templateBack);
        assertThat(contract.getTemplate()).isEqualTo(templateBack);

        contract.template(null);
        assertThat(contract.getTemplate()).isNull();
    }

    @Test
    void candidateTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        contract.setCandidate(candidateBack);
        assertThat(contract.getCandidate()).isEqualTo(candidateBack);

        contract.candidate(null);
        assertThat(contract.getCandidate()).isNull();
    }

    @Test
    void recruiterTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        contract.setRecruiter(recruiterBack);
        assertThat(contract.getRecruiter()).isEqualTo(recruiterBack);

        contract.recruiter(null);
        assertThat(contract.getRecruiter()).isNull();
    }

    @Test
    void employerTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        contract.setEmployer(employerBack);
        assertThat(contract.getEmployer()).isEqualTo(employerBack);

        contract.employer(null);
        assertThat(contract.getEmployer()).isNull();
    }

    @Test
    void applicationTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        contract.setApplication(applicationBack);
        assertThat(contract.getApplication()).isEqualTo(applicationBack);

        contract.application(null);
        assertThat(contract.getApplication()).isNull();
    }
}
