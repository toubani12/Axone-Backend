package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVAchievementTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVAchievementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVAchievement.class);
        TechCVAchievement techCVAchievement1 = getTechCVAchievementSample1();
        TechCVAchievement techCVAchievement2 = new TechCVAchievement();
        assertThat(techCVAchievement1).isNotEqualTo(techCVAchievement2);

        techCVAchievement2.setId(techCVAchievement1.getId());
        assertThat(techCVAchievement1).isEqualTo(techCVAchievement2);

        techCVAchievement2 = getTechCVAchievementSample2();
        assertThat(techCVAchievement1).isNotEqualTo(techCVAchievement2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVAchievement techCVAchievement = getTechCVAchievementRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVAchievement.setTechnicalCV(technicalCVBack);
        assertThat(techCVAchievement.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVAchievement.technicalCV(null);
        assertThat(techCVAchievement.getTechnicalCV()).isNull();
    }
}
