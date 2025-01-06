package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVProjectTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVProject.class);
        TechCVProject techCVProject1 = getTechCVProjectSample1();
        TechCVProject techCVProject2 = new TechCVProject();
        assertThat(techCVProject1).isNotEqualTo(techCVProject2);

        techCVProject2.setId(techCVProject1.getId());
        assertThat(techCVProject1).isEqualTo(techCVProject2);

        techCVProject2 = getTechCVProjectSample2();
        assertThat(techCVProject1).isNotEqualTo(techCVProject2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVProject techCVProject = getTechCVProjectRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVProject.setTechnicalCV(technicalCVBack);
        assertThat(techCVProject.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVProject.technicalCV(null);
        assertThat(techCVProject.getTechnicalCV()).isNull();
    }
}
