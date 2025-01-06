package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppTestTestSamples.*;
import static com.axone.hrsolution.domain.CustomQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomQuestion.class);
        CustomQuestion customQuestion1 = getCustomQuestionSample1();
        CustomQuestion customQuestion2 = new CustomQuestion();
        assertThat(customQuestion1).isNotEqualTo(customQuestion2);

        customQuestion2.setId(customQuestion1.getId());
        assertThat(customQuestion1).isEqualTo(customQuestion2);

        customQuestion2 = getCustomQuestionSample2();
        assertThat(customQuestion1).isNotEqualTo(customQuestion2);
    }

    @Test
    void appTestTest() throws Exception {
        CustomQuestion customQuestion = getCustomQuestionRandomSampleGenerator();
        AppTest appTestBack = getAppTestRandomSampleGenerator();

        customQuestion.setAppTest(appTestBack);
        assertThat(customQuestion.getAppTest()).isEqualTo(appTestBack);

        customQuestion.appTest(null);
        assertThat(customQuestion.getAppTest()).isNull();
    }
}
