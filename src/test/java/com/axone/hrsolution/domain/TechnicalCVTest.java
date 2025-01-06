package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.CandidateTestSamples.*;
import static com.axone.hrsolution.domain.TechCVAchievementTestSamples.*;
import static com.axone.hrsolution.domain.TechCVAltActivitiesTestSamples.*;
import static com.axone.hrsolution.domain.TechCVDocsTestSamples.*;
import static com.axone.hrsolution.domain.TechCVEducationTestSamples.*;
import static com.axone.hrsolution.domain.TechCVEmploymentTestSamples.*;
import static com.axone.hrsolution.domain.TechCVHardSkillsTestSamples.*;
import static com.axone.hrsolution.domain.TechCVProjectTestSamples.*;
import static com.axone.hrsolution.domain.TechCVSoftSkillsTestSamples.*;
import static com.axone.hrsolution.domain.TechnicalCVTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TechnicalCVTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechnicalCV.class);
        TechnicalCV technicalCV1 = getTechnicalCVSample1();
        TechnicalCV technicalCV2 = new TechnicalCV();
        assertThat(technicalCV1).isNotEqualTo(technicalCV2);

        technicalCV2.setId(technicalCV1.getId());
        assertThat(technicalCV1).isEqualTo(technicalCV2);

        technicalCV2 = getTechnicalCVSample2();
        assertThat(technicalCV1).isNotEqualTo(technicalCV2);
    }

    @Test
    void hardSkillsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVHardSkills techCVHardSkillsBack = getTechCVHardSkillsRandomSampleGenerator();

        technicalCV.addHardSkills(techCVHardSkillsBack);
        assertThat(technicalCV.getHardSkills()).containsOnly(techCVHardSkillsBack);
        assertThat(techCVHardSkillsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeHardSkills(techCVHardSkillsBack);
        assertThat(technicalCV.getHardSkills()).doesNotContain(techCVHardSkillsBack);
        assertThat(techCVHardSkillsBack.getTechnicalCV()).isNull();

        technicalCV.hardSkills(new HashSet<>(Set.of(techCVHardSkillsBack)));
        assertThat(technicalCV.getHardSkills()).containsOnly(techCVHardSkillsBack);
        assertThat(techCVHardSkillsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setHardSkills(new HashSet<>());
        assertThat(technicalCV.getHardSkills()).doesNotContain(techCVHardSkillsBack);
        assertThat(techCVHardSkillsBack.getTechnicalCV()).isNull();
    }

    @Test
    void softSkillsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVSoftSkills techCVSoftSkillsBack = getTechCVSoftSkillsRandomSampleGenerator();

        technicalCV.addSoftSkills(techCVSoftSkillsBack);
        assertThat(technicalCV.getSoftSkills()).containsOnly(techCVSoftSkillsBack);
        assertThat(techCVSoftSkillsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeSoftSkills(techCVSoftSkillsBack);
        assertThat(technicalCV.getSoftSkills()).doesNotContain(techCVSoftSkillsBack);
        assertThat(techCVSoftSkillsBack.getTechnicalCV()).isNull();

        technicalCV.softSkills(new HashSet<>(Set.of(techCVSoftSkillsBack)));
        assertThat(technicalCV.getSoftSkills()).containsOnly(techCVSoftSkillsBack);
        assertThat(techCVSoftSkillsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setSoftSkills(new HashSet<>());
        assertThat(technicalCV.getSoftSkills()).doesNotContain(techCVSoftSkillsBack);
        assertThat(techCVSoftSkillsBack.getTechnicalCV()).isNull();
    }

    @Test
    void educationTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVEducation techCVEducationBack = getTechCVEducationRandomSampleGenerator();

        technicalCV.addEducation(techCVEducationBack);
        assertThat(technicalCV.getEducations()).containsOnly(techCVEducationBack);
        assertThat(techCVEducationBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeEducation(techCVEducationBack);
        assertThat(technicalCV.getEducations()).doesNotContain(techCVEducationBack);
        assertThat(techCVEducationBack.getTechnicalCV()).isNull();

        technicalCV.educations(new HashSet<>(Set.of(techCVEducationBack)));
        assertThat(technicalCV.getEducations()).containsOnly(techCVEducationBack);
        assertThat(techCVEducationBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setEducations(new HashSet<>());
        assertThat(technicalCV.getEducations()).doesNotContain(techCVEducationBack);
        assertThat(techCVEducationBack.getTechnicalCV()).isNull();
    }

    @Test
    void employmentsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVEmployment techCVEmploymentBack = getTechCVEmploymentRandomSampleGenerator();

        technicalCV.addEmployments(techCVEmploymentBack);
        assertThat(technicalCV.getEmployments()).containsOnly(techCVEmploymentBack);
        assertThat(techCVEmploymentBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeEmployments(techCVEmploymentBack);
        assertThat(technicalCV.getEmployments()).doesNotContain(techCVEmploymentBack);
        assertThat(techCVEmploymentBack.getTechnicalCV()).isNull();

        technicalCV.employments(new HashSet<>(Set.of(techCVEmploymentBack)));
        assertThat(technicalCV.getEmployments()).containsOnly(techCVEmploymentBack);
        assertThat(techCVEmploymentBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setEmployments(new HashSet<>());
        assertThat(technicalCV.getEmployments()).doesNotContain(techCVEmploymentBack);
        assertThat(techCVEmploymentBack.getTechnicalCV()).isNull();
    }

    @Test
    void projectsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVProject techCVProjectBack = getTechCVProjectRandomSampleGenerator();

        technicalCV.addProjects(techCVProjectBack);
        assertThat(technicalCV.getProjects()).containsOnly(techCVProjectBack);
        assertThat(techCVProjectBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeProjects(techCVProjectBack);
        assertThat(technicalCV.getProjects()).doesNotContain(techCVProjectBack);
        assertThat(techCVProjectBack.getTechnicalCV()).isNull();

        technicalCV.projects(new HashSet<>(Set.of(techCVProjectBack)));
        assertThat(technicalCV.getProjects()).containsOnly(techCVProjectBack);
        assertThat(techCVProjectBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setProjects(new HashSet<>());
        assertThat(technicalCV.getProjects()).doesNotContain(techCVProjectBack);
        assertThat(techCVProjectBack.getTechnicalCV()).isNull();
    }

    @Test
    void achievementsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVAchievement techCVAchievementBack = getTechCVAchievementRandomSampleGenerator();

        technicalCV.addAchievements(techCVAchievementBack);
        assertThat(technicalCV.getAchievements()).containsOnly(techCVAchievementBack);
        assertThat(techCVAchievementBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeAchievements(techCVAchievementBack);
        assertThat(technicalCV.getAchievements()).doesNotContain(techCVAchievementBack);
        assertThat(techCVAchievementBack.getTechnicalCV()).isNull();

        technicalCV.achievements(new HashSet<>(Set.of(techCVAchievementBack)));
        assertThat(technicalCV.getAchievements()).containsOnly(techCVAchievementBack);
        assertThat(techCVAchievementBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setAchievements(new HashSet<>());
        assertThat(technicalCV.getAchievements()).doesNotContain(techCVAchievementBack);
        assertThat(techCVAchievementBack.getTechnicalCV()).isNull();
    }

    @Test
    void attachedDocsTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVDocs techCVDocsBack = getTechCVDocsRandomSampleGenerator();

        technicalCV.addAttachedDocs(techCVDocsBack);
        assertThat(technicalCV.getAttachedDocs()).containsOnly(techCVDocsBack);
        assertThat(techCVDocsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeAttachedDocs(techCVDocsBack);
        assertThat(technicalCV.getAttachedDocs()).doesNotContain(techCVDocsBack);
        assertThat(techCVDocsBack.getTechnicalCV()).isNull();

        technicalCV.attachedDocs(new HashSet<>(Set.of(techCVDocsBack)));
        assertThat(technicalCV.getAttachedDocs()).containsOnly(techCVDocsBack);
        assertThat(techCVDocsBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setAttachedDocs(new HashSet<>());
        assertThat(technicalCV.getAttachedDocs()).doesNotContain(techCVDocsBack);
        assertThat(techCVDocsBack.getTechnicalCV()).isNull();
    }

    @Test
    void altActivitiesTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        TechCVAltActivities techCVAltActivitiesBack = getTechCVAltActivitiesRandomSampleGenerator();

        technicalCV.addAltActivities(techCVAltActivitiesBack);
        assertThat(technicalCV.getAltActivities()).containsOnly(techCVAltActivitiesBack);
        assertThat(techCVAltActivitiesBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.removeAltActivities(techCVAltActivitiesBack);
        assertThat(technicalCV.getAltActivities()).doesNotContain(techCVAltActivitiesBack);
        assertThat(techCVAltActivitiesBack.getTechnicalCV()).isNull();

        technicalCV.altActivities(new HashSet<>(Set.of(techCVAltActivitiesBack)));
        assertThat(technicalCV.getAltActivities()).containsOnly(techCVAltActivitiesBack);
        assertThat(techCVAltActivitiesBack.getTechnicalCV()).isEqualTo(technicalCV);

        technicalCV.setAltActivities(new HashSet<>());
        assertThat(technicalCV.getAltActivities()).doesNotContain(techCVAltActivitiesBack);
        assertThat(techCVAltActivitiesBack.getTechnicalCV()).isNull();
    }

    @Test
    void candidateTest() throws Exception {
        TechnicalCV technicalCV = getTechnicalCVRandomSampleGenerator();
        Candidate candidateBack = getCandidateRandomSampleGenerator();

        technicalCV.setCandidate(candidateBack);
        assertThat(technicalCV.getCandidate()).isEqualTo(candidateBack);
        assertThat(candidateBack.getTechCV()).isEqualTo(technicalCV);

        technicalCV.candidate(null);
        assertThat(technicalCV.getCandidate()).isNull();
        assertThat(candidateBack.getTechCV()).isNull();
    }
}
