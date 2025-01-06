package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TechCVSoftSkillsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVSoftSkillsAllPropertiesEquals(TechCVSoftSkills expected, TechCVSoftSkills actual) {
        assertTechCVSoftSkillsAutoGeneratedPropertiesEquals(expected, actual);
        assertTechCVSoftSkillsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVSoftSkillsAllUpdatablePropertiesEquals(TechCVSoftSkills expected, TechCVSoftSkills actual) {
        assertTechCVSoftSkillsUpdatableFieldsEquals(expected, actual);
        assertTechCVSoftSkillsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVSoftSkillsAutoGeneratedPropertiesEquals(TechCVSoftSkills expected, TechCVSoftSkills actual) {
        assertThat(expected)
            .as("Verify TechCVSoftSkills auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVSoftSkillsUpdatableFieldsEquals(TechCVSoftSkills expected, TechCVSoftSkills actual) {
        assertThat(expected)
            .as("Verify TechCVSoftSkills relevant properties")
            .satisfies(e -> assertThat(e.getSkills()).as("check skills").isEqualTo(actual.getSkills()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVSoftSkillsUpdatableRelationshipsEquals(TechCVSoftSkills expected, TechCVSoftSkills actual) {
        assertThat(expected)
            .as("Verify TechCVSoftSkills relationships")
            .satisfies(e -> assertThat(e.getTechnicalCV()).as("check technicalCV").isEqualTo(actual.getTechnicalCV()));
    }
}
