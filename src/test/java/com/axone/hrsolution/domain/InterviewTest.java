package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.InterviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Interview.class);
        Interview interview1 = getInterviewSample1();
        Interview interview2 = new Interview();
        assertThat(interview1).isNotEqualTo(interview2);

        interview2.setId(interview1.getId());
        assertThat(interview1).isEqualTo(interview2);

        interview2 = getInterviewSample2();
        assertThat(interview1).isNotEqualTo(interview2);
    }

    @Test
    void attendeeTest() throws Exception {
        Interview interview = getInterviewRandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        interview.setAttendee(candidateBack);
        assertThat(interview.getAttendee()).isEqualTo(candidateBack);

        interview.attendee(null);
        assertThat(interview.getAttendee()).isNull();
    }

    @Test
    void applicationTest() throws Exception {
        Interview interview = getInterviewRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        interview.setApplication(applicationBack);
        assertThat(interview.getApplication()).isEqualTo(applicationBack);

        interview.application(null);
        assertThat(interview.getApplication()).isNull();
    }
}
