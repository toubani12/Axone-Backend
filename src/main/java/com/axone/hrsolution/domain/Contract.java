package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.ContractStatus;
import com.axone.hrsolution.domain.enumeration.TemplateContractType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TemplateContractType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @NotNull
    @Column(name = "direct_contract", nullable = false)
    private Boolean directContract;

    @NotNull
    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @NotNull
    @Column(name = "recruiter_income_rate", nullable = false)
    private Float recruiterIncomeRate;

    @NotNull
    @Column(name = "candidate_income_rate", nullable = false)
    private Float candidateIncomeRate;

    @JsonIgnoreProperties(value = { "contract", "owner", "applications" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Template template;

    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Candidate candidate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    private Recruiter recruiter;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    private Employer employer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "contracts",
            "interviews",
            "contractTypes",
            "contractTemplates",
            "criteria",
            "domains",
            "employer",
            "recruiters",
            "candidates",
            "request",
        },
        allowSetters = true
    )
    private Application application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Contract label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TemplateContractType getType() {
        return this.type;
    }

    public Contract type(TemplateContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(TemplateContractType type) {
        this.type = type;
    }

    public ContractStatus getStatus() {
        return this.status;
    }

    public Contract status(ContractStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public Boolean getDirectContract() {
        return this.directContract;
    }

    public Contract directContract(Boolean directContract) {
        this.setDirectContract(directContract);
        return this;
    }

    public void setDirectContract(Boolean directContract) {
        this.directContract = directContract;
    }

    public Double getPaymentAmount() {
        return this.paymentAmount;
    }

    public Contract paymentAmount(Double paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Float getRecruiterIncomeRate() {
        return this.recruiterIncomeRate;
    }

    public Contract recruiterIncomeRate(Float recruiterIncomeRate) {
        this.setRecruiterIncomeRate(recruiterIncomeRate);
        return this;
    }

    public void setRecruiterIncomeRate(Float recruiterIncomeRate) {
        this.recruiterIncomeRate = recruiterIncomeRate;
    }

    public Float getCandidateIncomeRate() {
        return this.candidateIncomeRate;
    }

    public Contract candidateIncomeRate(Float candidateIncomeRate) {
        this.setCandidateIncomeRate(candidateIncomeRate);
        return this;
    }

    public void setCandidateIncomeRate(Float candidateIncomeRate) {
        this.candidateIncomeRate = candidateIncomeRate;
    }

    public Template getTemplate() {
        return this.template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Contract template(Template template) {
        this.setTemplate(template);
        return this;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Contract candidate(Candidate candidate) {
        this.setCandidate(candidate);
        return this;
    }

    public Recruiter getRecruiter() {
        return this.recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public Contract recruiter(Recruiter recruiter) {
        this.setRecruiter(recruiter);
        return this;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Contract employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Contract application(Application application) {
        this.setApplication(application);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", directContract='" + getDirectContract() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", recruiterIncomeRate=" + getRecruiterIncomeRate() +
            ", candidateIncomeRate=" + getCandidateIncomeRate() +
            "}";
    }
}
