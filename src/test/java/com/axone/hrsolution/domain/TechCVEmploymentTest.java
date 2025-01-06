package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVEmploymentTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVEmploymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVEmployment.class);
        TechCVEmployment techCVEmployment1 = getTechCVEmploymentSample1();
        TechCVEmployment techCVEmployment2 = new TechCVEmployment();
        assertThat(techCVEmployment1).isNotEqualTo(techCVEmployment2);

        techCVEmployment2.setId(techCVEmployment1.getId());
        assertThat(techCVEmployment1).isEqualTo(techCVEmployment2);

        techCVEmployment2 = getTechCVEmploymentSample2();
        assertThat(techCVEmployment1).isNotEqualTo(techCVEmployment2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVEmployment techCVEmployment = getTechCVEmploymentRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVEmployment.setTechnicalCV(technicalCVBack);
        assertThat(techCVEmployment.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVEmployment.technicalCV(null);
        assertThat(techCVEmployment.getTechnicalCV()).isNull();
    }
}
