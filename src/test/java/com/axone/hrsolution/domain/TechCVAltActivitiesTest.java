package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVAltActivitiesTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVAltActivitiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVAltActivities.class);
        TechCVAltActivities techCVAltActivities1 = getTechCVAltActivitiesSample1();
        TechCVAltActivities techCVAltActivities2 = new TechCVAltActivities();
        assertThat(techCVAltActivities1).isNotEqualTo(techCVAltActivities2);

        techCVAltActivities2.setId(techCVAltActivities1.getId());
        assertThat(techCVAltActivities1).isEqualTo(techCVAltActivities2);

        techCVAltActivities2 = getTechCVAltActivitiesSample2();
        assertThat(techCVAltActivities1).isNotEqualTo(techCVAltActivities2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVAltActivities techCVAltActivities = getTechCVAltActivitiesRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVAltActivities.setTechnicalCV(technicalCVBack);
        assertThat(techCVAltActivities.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVAltActivities.technicalCV(null);
        assertThat(techCVAltActivities.getTechnicalCV()).isNull();
    }
}
