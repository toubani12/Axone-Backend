package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.CriteriaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CriteriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Criteria.class);
        Criteria criteria1 = getCriteriaSample1();
        Criteria criteria2 = new Criteria();
        assertThat(criteria1).isNotEqualTo(criteria2);

        criteria2.setId(criteria1.getId());
        assertThat(criteria1).isEqualTo(criteria2);

        criteria2 = getCriteriaSample2();
        assertThat(criteria1).isNotEqualTo(criteria2);
    }

    @Test
    void applicationTest() throws Exception {
        Criteria criteria = getCriteriaRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        criteria.addApplication(applicationBack);
        assertThat(criteria.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getCriteria()).containsOnly(criteria);

        criteria.removeApplication(applicationBack);
        assertThat(criteria.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getCriteria()).doesNotContain(criteria);

        criteria.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(criteria.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getCriteria()).containsOnly(criteria);

        criteria.setApplications(new HashSet<>());
        assertThat(criteria.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getCriteria()).doesNotContain(criteria);
    }
}
