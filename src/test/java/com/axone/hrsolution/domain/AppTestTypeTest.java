package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppTestTestSamples.*;
import static com.axone.hrsolution.domain.AppTestTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppTestTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppTestType.class);
        AppTestType appTestType1 = getAppTestTypeSample1();
        AppTestType appTestType2 = new AppTestType();
        assertThat(appTestType1).isNotEqualTo(appTestType2);

        appTestType2.setId(appTestType1.getId());
        assertThat(appTestType1).isEqualTo(appTestType2);

        appTestType2 = getAppTestTypeSample2();
        assertThat(appTestType1).isNotEqualTo(appTestType2);
    }

    @Test
    void appTestTest() throws Exception {
        AppTestType appTestType = getAppTestTypeRandomSampleGenerator();
        AppTest appTestBack = getAppTestRandomSampleGenerator();

        appTestType.addAppTest(appTestBack);
        assertThat(appTestType.getAppTests()).containsOnly(appTestBack);
        assertThat(appTestBack.getTypes()).containsOnly(appTestType);

        appTestType.removeAppTest(appTestBack);
        assertThat(appTestType.getAppTests()).doesNotContain(appTestBack);
        assertThat(appTestBack.getTypes()).doesNotContain(appTestType);

        appTestType.appTests(new HashSet<>(Set.of(appTestBack)));
        assertThat(appTestType.getAppTests()).containsOnly(appTestBack);
        assertThat(appTestBack.getTypes()).containsOnly(appTestType);

        appTestType.setAppTests(new HashSet<>());
        assertThat(appTestType.getAppTests()).doesNotContain(appTestBack);
        assertThat(appTestBack.getTypes()).doesNotContain(appTestType);
    }
}
