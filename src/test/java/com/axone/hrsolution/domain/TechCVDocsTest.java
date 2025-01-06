package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVDocsTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVDocsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVDocs.class);
        TechCVDocs techCVDocs1 = getTechCVDocsSample1();
        TechCVDocs techCVDocs2 = new TechCVDocs();
        assertThat(techCVDocs1).isNotEqualTo(techCVDocs2);

        techCVDocs2.setId(techCVDocs1.getId());
        assertThat(techCVDocs1).isEqualTo(techCVDocs2);

        techCVDocs2 = getTechCVDocsSample2();
        assertThat(techCVDocs1).isNotEqualTo(techCVDocs2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVDocs techCVDocs = getTechCVDocsRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVDocs.setTechnicalCV(technicalCVBack);
        assertThat(techCVDocs.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVDocs.technicalCV(null);
        assertThat(techCVDocs.getTechnicalCV()).isNull();
    }
}
