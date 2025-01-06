package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppTestTestSamples.*;
import static com.axone.hrsolution.domain.AppTestTypeTestSamples.*;
import static com.axone.hrsolution.domain.CustomQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppTest.class);
        AppTest appTest1 = getAppTestSample1();
        AppTest appTest2 = new AppTest();
        assertThat(appTest1).isNotEqualTo(appTest2);

        appTest2.setId(appTest1.getId());
        assertThat(appTest1).isEqualTo(appTest2);

        appTest2 = getAppTestSample2();
        assertThat(appTest1).isNotEqualTo(appTest2);
    }

    @Test
    void customQuestionsTest() throws Exception {
        AppTest appTest = getAppTestRandomSampleGenerator();
        CustomQuestion customQuestionBack = getCustomQuestionRandomSampleGenerator();

        appTest.addCustomQuestions(customQuestionBack);
        assertThat(appTest.getCustomQuestions()).containsOnly(customQuestionBack);
        assertThat(customQuestionBack.getAppTest()).isEqualTo(appTest);

        appTest.removeCustomQuestions(customQuestionBack);
        assertThat(appTest.getCustomQuestions()).doesNotContain(customQuestionBack);
        assertThat(customQuestionBack.getAppTest()).isNull();

        appTest.customQuestions(new HashSet<>(Set.of(customQuestionBack)));
        assertThat(appTest.getCustomQuestions()).containsOnly(customQuestionBack);
        assertThat(customQuestionBack.getAppTest()).isEqualTo(appTest);

        appTest.setCustomQuestions(new HashSet<>());
        assertThat(appTest.getCustomQuestions()).doesNotContain(customQuestionBack);
        assertThat(customQuestionBack.getAppTest()).isNull();
    }

    @Test
    void typeTest() throws Exception {
        AppTest appTest = getAppTestRandomSampleGenerator();
        AppTestType appTestTypeBack = getAppTestTypeRandomSampleGenerator();

        appTest.addType(appTestTypeBack);
        assertThat(appTest.getTypes()).containsOnly(appTestTypeBack);

        appTest.removeType(appTestTypeBack);
        assertThat(appTest.getTypes()).doesNotContain(appTestTypeBack);

        appTest.types(new HashSet<>(Set.of(appTestTypeBack)));
        assertThat(appTest.getTypes()).containsOnly(appTestTypeBack);

        appTest.setTypes(new HashSet<>());
        assertThat(appTest.getTypes()).doesNotContain(appTestTypeBack);
    }
}
