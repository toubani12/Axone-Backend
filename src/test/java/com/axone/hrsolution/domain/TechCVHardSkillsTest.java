package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVHardSkillsTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVHardSkillsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVHardSkills.class);
        TechCVHardSkills techCVHardSkills1 = getTechCVHardSkillsSample1();
        TechCVHardSkills techCVHardSkills2 = new TechCVHardSkills();
        assertThat(techCVHardSkills1).isNotEqualTo(techCVHardSkills2);

        techCVHardSkills2.setId(techCVHardSkills1.getId());
        assertThat(techCVHardSkills1).isEqualTo(techCVHardSkills2);

        techCVHardSkills2 = getTechCVHardSkillsSample2();
        assertThat(techCVHardSkills1).isNotEqualTo(techCVHardSkills2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVHardSkills techCVHardSkills = getTechCVHardSkillsRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVHardSkills.setTechnicalCV(technicalCVBack);
        assertThat(techCVHardSkills.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVHardSkills.technicalCV(null);
        assertThat(techCVHardSkills.getTechnicalCV()).isNull();
    }
}
