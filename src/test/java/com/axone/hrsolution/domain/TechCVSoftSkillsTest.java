package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.TechCVSoftSkillsTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechCVSoftSkillsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechCVSoftSkills.class);
        TechCVSoftSkills techCVSoftSkills1 = getTechCVSoftSkillsSample1();
        TechCVSoftSkills techCVSoftSkills2 = new TechCVSoftSkills();
        assertThat(techCVSoftSkills1).isNotEqualTo(techCVSoftSkills2);

        techCVSoftSkills2.setId(techCVSoftSkills1.getId());
        assertThat(techCVSoftSkills1).isEqualTo(techCVSoftSkills2);

        techCVSoftSkills2 = getTechCVSoftSkillsSample2();
        assertThat(techCVSoftSkills1).isNotEqualTo(techCVSoftSkills2);
    }

    @Test
    void technicalCVTest() throws Exception {
        TechCVSoftSkills techCVSoftSkills = getTechCVSoftSkillsRandomSampleGenerator();
        TechnicalCV technicalCVBack = getTechnicalCVRandomSampleGenerator();

        techCVSoftSkills.setTechnicalCV(technicalCVBack);
        assertThat(techCVSoftSkills.getTechnicalCV()).isEqualTo(technicalCVBack);

        techCVSoftSkills.technicalCV(null);
        assertThat(techCVSoftSkills.getTechnicalCV()).isNull();
    }
}
