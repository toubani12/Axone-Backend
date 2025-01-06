package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static com.axone.hrsolution.domain.RequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Request.class);
        Request request1 = getRequestSample1();
        Request request2 = new Request();
        assertThat(request1).isNotEqualTo(request2);

        request2.setId(request1.getId());
        assertThat(request1).isEqualTo(request2);

        request2 = getRequestSample2();
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void relatedApplicationTest() throws Exception {
        Request request = getRequestRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        request.setRelatedApplication(applicationBack);
        assertThat(request.getRelatedApplication()).isEqualTo(applicationBack);

        request.relatedApplication(null);
        assertThat(request.getRelatedApplication()).isNull();
    }

    @Test
    void recruiterTest() throws Exception {
        Request request = getRequestRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        request.setRecruiter(recruiterBack);
        assertThat(request.getRecruiter()).isEqualTo(recruiterBack);

        request.recruiter(null);
        assertThat(request.getRecruiter()).isNull();
    }
}
