package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "number_of_candidates", nullable = false)
    private Integer numberOfCandidates;

    @NotNull
    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @Column(name = "recruiter_income_rate")
    private Float recruiterIncomeRate;

    @Column(name = "candidate_income_rate")
    private Float candidateIncomeRate;

    @Column(name = "deadline")
    private LocalDate deadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "opened_at")
    private Instant openedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "done_at")
    private Instant doneAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "template", "candidate", "recruiter", "employer", "application" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attendee", "application" }, allowSetters = true)
    private Set<Interview> interviews = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_application__contract_type",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "contract_type_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private Set<ContractType> contractTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_application__contract_template",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "contract_template_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "owner", "applications" }, allowSetters = true)
    private Set<Template> contractTemplates = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_application__criteria",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "criteria_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private Set<Criteria> criteria = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_application__domain",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recruiters", "candidates", "applications", "employer" }, allowSetters = true)
    private Set<Domain> domains = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    private Employer employer;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "applications")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    private Set<Recruiter> recruiters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "applications")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    private Set<Candidate> candidates = new HashSet<>();

    @JsonIgnoreProperties(value = { "relatedApplication", "recruiter" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "relatedApplication")
    private Request request;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Application title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Application description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfCandidates() {
        return this.numberOfCandidates;
    }

    public Application numberOfCandidates(Integer numberOfCandidates) {
        this.setNumberOfCandidates(numberOfCandidates);
        return this;
    }

    public void setNumberOfCandidates(Integer numberOfCandidates) {
        this.numberOfCandidates = numberOfCandidates;
    }

    public Double getPaymentAmount() {
        return this.paymentAmount;
    }

    public Application paymentAmount(Double paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Float getRecruiterIncomeRate() {
        return this.recruiterIncomeRate;
    }

    public Application recruiterIncomeRate(Float recruiterIncomeRate) {
        this.setRecruiterIncomeRate(recruiterIncomeRate);
        return this;
    }

    public void setRecruiterIncomeRate(Float recruiterIncomeRate) {
        this.recruiterIncomeRate = recruiterIncomeRate;
    }

    public Float getCandidateIncomeRate() {
        return this.candidateIncomeRate;
    }

    public Application candidateIncomeRate(Float candidateIncomeRate) {
        this.setCandidateIncomeRate(candidateIncomeRate);
        return this;
    }

    public void setCandidateIncomeRate(Float candidateIncomeRate) {
        this.candidateIncomeRate = candidateIncomeRate;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public Application deadline(LocalDate deadline) {
        this.setDeadline(deadline);
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public ApplicationStatus getStatus() {
        return this.status;
    }

    public Application status(ApplicationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Application createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getOpenedAt() {
        return this.openedAt;
    }

    public Application openedAt(Instant openedAt) {
        this.setOpenedAt(openedAt);
        return this;
    }

    public void setOpenedAt(Instant openedAt) {
        this.openedAt = openedAt;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public Application closedAt(Instant closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getDoneAt() {
        return this.doneAt;
    }

    public Application doneAt(Instant doneAt) {
        this.setDoneAt(doneAt);
        return this;
    }

    public void setDoneAt(Instant doneAt) {
        this.doneAt = doneAt;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setApplication(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setApplication(this));
        }
        this.contracts = contracts;
    }

    public Application contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Application addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setApplication(this);
        return this;
    }

    public Application removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setApplication(null);
        return this;
    }

    public Set<Interview> getInterviews() {
        return this.interviews;
    }

    public void setInterviews(Set<Interview> interviews) {
        if (this.interviews != null) {
            this.interviews.forEach(i -> i.setApplication(null));
        }
        if (interviews != null) {
            interviews.forEach(i -> i.setApplication(this));
        }
        this.interviews = interviews;
    }

    public Application interviews(Set<Interview> interviews) {
        this.setInterviews(interviews);
        return this;
    }

    public Application addInterviews(Interview interview) {
        this.interviews.add(interview);
        interview.setApplication(this);
        return this;
    }

    public Application removeInterviews(Interview interview) {
        this.interviews.remove(interview);
        interview.setApplication(null);
        return this;
    }

    public Set<ContractType> getContractTypes() {
        return this.contractTypes;
    }

    public void setContractTypes(Set<ContractType> contractTypes) {
        this.contractTypes = contractTypes;
    }

    public Application contractTypes(Set<ContractType> contractTypes) {
        this.setContractTypes(contractTypes);
        return this;
    }

    public Application addContractType(ContractType contractType) {
        this.contractTypes.add(contractType);
        return this;
    }

    public Application removeContractType(ContractType contractType) {
        this.contractTypes.remove(contractType);
        return this;
    }

    public Set<Template> getContractTemplates() {
        return this.contractTemplates;
    }

    public void setContractTemplates(Set<Template> templates) {
        this.contractTemplates = templates;
    }

    public Application contractTemplates(Set<Template> templates) {
        this.setContractTemplates(templates);
        return this;
    }

    public Application addContractTemplate(Template template) {
        this.contractTemplates.add(template);
        return this;
    }

    public Application removeContractTemplate(Template template) {
        this.contractTemplates.remove(template);
        return this;
    }

    public Set<Criteria> getCriteria() {
        return this.criteria;
    }

    public void setCriteria(Set<Criteria> criteria) {
        this.criteria = criteria;
    }

    public Application criteria(Set<Criteria> criteria) {
        this.setCriteria(criteria);
        return this;
    }

    public Application addCriteria(Criteria criteria) {
        this.criteria.add(criteria);
        return this;
    }

    public Application removeCriteria(Criteria criteria) {
        this.criteria.remove(criteria);
        return this;
    }

    public Set<Domain> getDomains() {
        return this.domains;
    }

    public void setDomains(Set<Domain> domains) {
        this.domains = domains;
    }

    public Application domains(Set<Domain> domains) {
        this.setDomains(domains);
        return this;
    }

    public Application addDomain(Domain domain) {
        this.domains.add(domain);
        return this;
    }

    public Application removeDomain(Domain domain) {
        this.domains.remove(domain);
        return this;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Application employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    public Set<Recruiter> getRecruiters() {
        return this.recruiters;
    }

    public void setRecruiters(Set<Recruiter> recruiters) {
        if (this.recruiters != null) {
            this.recruiters.forEach(i -> i.removeApplications(this));
        }
        if (recruiters != null) {
            recruiters.forEach(i -> i.addApplications(this));
        }
        this.recruiters = recruiters;
    }

    public Application recruiters(Set<Recruiter> recruiters) {
        this.setRecruiters(recruiters);
        return this;
    }

    public Application addRecruiters(Recruiter recruiter) {
        this.recruiters.add(recruiter);
        recruiter.getApplications().add(this);
        return this;
    }

    public Application removeRecruiters(Recruiter recruiter) {
        this.recruiters.remove(recruiter);
        recruiter.getApplications().remove(this);
        return this;
    }

    public Set<Candidate> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(Set<Candidate> candidates) {
        if (this.candidates != null) {
            this.candidates.forEach(i -> i.removeApplications(this));
        }
        if (candidates != null) {
            candidates.forEach(i -> i.addApplications(this));
        }
        this.candidates = candidates;
    }

    public Application candidates(Set<Candidate> candidates) {
        this.setCandidates(candidates);
        return this;
    }

    public Application addCandidates(Candidate candidate) {
        this.candidates.add(candidate);
        candidate.getApplications().add(this);
        return this;
    }

    public Application removeCandidates(Candidate candidate) {
        this.candidates.remove(candidate);
        candidate.getApplications().remove(this);
        return this;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        if (this.request != null) {
            this.request.setRelatedApplication(null);
        }
        if (request != null) {
            request.setRelatedApplication(this);
        }
        this.request = request;
    }

    public Application request(Request request) {
        this.setRequest(request);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return getId() != null && getId().equals(((Application) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfCandidates=" + getNumberOfCandidates() +
            ", paymentAmount=" + getPaymentAmount() +
            ", recruiterIncomeRate=" + getRecruiterIncomeRate() +
            ", candidateIncomeRate=" + getCandidateIncomeRate() +
            ", deadline='" + getDeadline() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", openedAt='" + getOpenedAt() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", doneAt='" + getDoneAt() + "'" +
            "}";
    }
}
