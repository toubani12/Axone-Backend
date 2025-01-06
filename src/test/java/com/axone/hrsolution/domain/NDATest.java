package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.NDATestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NDATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NDA.class);
        NDA nDA1 = getNDASample1();
        NDA nDA2 = new NDA();
        assertThat(nDA1).isNotEqualTo(nDA2);

        nDA2.setId(nDA1.getId());
        assertThat(nDA1).isEqualTo(nDA2);

        nDA2 = getNDASample2();
        assertThat(nDA1).isNotEqualTo(nDA2);
    }

    @Test
    void employerTest() throws Exception {
        NDA nDA = getNDARandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        nDA.setEmployer(employerBack);
        assertThat(nDA.getEmployer()).isEqualTo(employerBack);

        nDA.employer(null);
        assertThat(nDA.getEmployer()).isNull();
    }

    @Test
    void mediatorTest() throws Exception {
        NDA nDA = getNDARandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        nDA.setMediator(recruiterBack);
        assertThat(nDA.getMediator()).isEqualTo(recruiterBack);

        nDA.mediator(null);
        assertThat(nDA.getMediator()).isNull();
    }

    @Test
    void candidateTest() throws Exception {
        NDA nDA = getNDARandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        nDA.setCandidate(candidateBack);
        assertThat(nDA.getCandidate()).isEqualTo(candidateBack);

        nDA.candidate(null);
        assertThat(nDA.getCandidate()).isNull();
    }
}
