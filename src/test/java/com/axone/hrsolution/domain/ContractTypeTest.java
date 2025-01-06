package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.ContractTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractType.class);
        ContractType contractType1 = getContractTypeSample1();
        ContractType contractType2 = new ContractType();
        assertThat(contractType1).isNotEqualTo(contractType2);

        contractType2.setId(contractType1.getId());
        assertThat(contractType1).isEqualTo(contractType2);

        contractType2 = getContractTypeSample2();
        assertThat(contractType1).isNotEqualTo(contractType2);
    }

    @Test
    void applicationTest() throws Exception {
        ContractType contractType = getContractTypeRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        contractType.addApplication(applicationBack);
        assertThat(contractType.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getContractTypes()).containsOnly(contractType);

        contractType.removeApplication(applicationBack);
        assertThat(contractType.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getContractTypes()).doesNotContain(contractType);

        contractType.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(contractType.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getContractTypes()).containsOnly(contractType);

        contractType.setApplications(new HashSet<>());
        assertThat(contractType.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getContractTypes()).doesNotContain(contractType);
    }
}
