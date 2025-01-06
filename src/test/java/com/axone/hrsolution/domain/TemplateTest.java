package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ApplicationTestSamples.*;
import static com.axone.hrsolution.domain.ContractTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.TemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Template.class);
        Template template1 = getTemplateSample1();
        Template template2 = new Template();
        assertThat(template1).isNotEqualTo(template2);

        template2.setId(template1.getId());
        assertThat(template1).isEqualTo(template2);

        template2 = getTemplateSample2();
        assertThat(template1).isNotEqualTo(template2);
    }

    @Test
    void contractTest() throws Exception {
        Template template = getTemplateRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        template.setContract(contractBack);
        assertThat(template.getContract()).isEqualTo(contractBack);
        assertThat(contractBack.getTemplate()).isEqualTo(template);

        template.contract(null);
        assertThat(template.getContract()).isNull();
        assertThat(contractBack.getTemplate()).isNull();
    }

    @Test
    void ownerTest() throws Exception {
        Template template = getTemplateRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        template.setOwner(employerBack);
        assertThat(template.getOwner()).isEqualTo(employerBack);

        template.owner(null);
        assertThat(template.getOwner()).isNull();
    }

    @Test
    void applicationTest() throws Exception {
        Template template = getTemplateRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        template.addApplication(applicationBack);
        assertThat(template.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getContractTemplates()).containsOnly(template);

        template.removeApplication(applicationBack);
        assertThat(template.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getContractTemplates()).doesNotContain(template);

        template.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(template.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getContractTemplates()).containsOnly(template);

        template.setApplications(new HashSet<>());
        assertThat(template.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getContractTemplates()).doesNotContain(template);
    }
}
