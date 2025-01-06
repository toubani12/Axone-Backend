package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVEducationTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVEducationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVEducation.class);
        TechCVEducation techCVEducation1 = getTechCVEducationSample1();
        TechCVEducation techCVEducation2 = new TechCVEducation();
        assertThat(techCVEducation1).isNotEqualTo(techCVEducation2);

        techCVEducation2.setId(techCVEducation1.getId());
        assertThat(techCVEducation1).isEqualTo(techCVEducation2);

        techCVEducation2 = getTechCVEducationSample2();
        assertThat(techCVEducation1).isNotEqualTo(techCVEducation2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVEducation techCVEducation = getTechCVEducationRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVEducation.setTechnicalCV(technicalCVBack);
        assertThat(techCVEducation.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVEducation.technicalCV(null);
        assertThat(techCVEducation.getTechnicalCV()).isNull();
    }
}
