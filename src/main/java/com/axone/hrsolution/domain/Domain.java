package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Domain.
 */
@Entity
@Table(name = "domain")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Domain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "operationalDomains")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    private Set<Recruiter> recruiters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "domains")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    private Set<Candidate> candidates = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "domains")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Application> applications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    private Employer employer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Domain id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Domain name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Recruiter> getRecruiters() {
        return this.recruiters;
    }

    public void setRecruiters(Set<Recruiter> recruiters) {
        if (this.recruiters != null) {
            this.recruiters.forEach(i -> i.removeOperationalDomain(this));
        }
        if (recruiters != null) {
            recruiters.forEach(i -> i.addOperationalDomain(this));
        }
        this.recruiters = recruiters;
    }

    public Domain recruiters(Set<Recruiter> recruiters) {
        this.setRecruiters(recruiters);
        return this;
    }

    public Domain addRecruiter(Recruiter recruiter) {
        this.recruiters.add(recruiter);
        recruiter.getOperationalDomains().add(this);
        return this;
    }

    public Domain removeRecruiter(Recruiter recruiter) {
        this.recruiters.remove(recruiter);
        recruiter.getOperationalDomains().remove(this);
        return this;
    }

    public Set<Candidate> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(Set<Candidate> candidates) {
        if (this.candidates != null) {
            this.candidates.forEach(i -> i.removeDomain(this));
        }
        if (candidates != null) {
            candidates.forEach(i -> i.addDomain(this));
        }
        this.candidates = candidates;
    }

    public Domain candidates(Set<Candidate> candidates) {
        this.setCandidates(candidates);
        return this;
    }

    public Domain addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
        candidate.getDomains().add(this);
        return this;
    }

    public Domain removeCandidate(Candidate candidate) {
        this.candidates.remove(candidate);
        candidate.getDomains().remove(this);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeDomain(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addDomain(this));
        }
        this.applications = applications;
    }

    public Domain applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Domain addApplication(Application application) {
        this.applications.add(application);
        application.getDomains().add(this);
        return this;
    }

    public Domain removeApplication(Application application) {
        this.applications.remove(application);
        application.getDomains().remove(this);
        return this;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Domain employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domain)) {
            return false;
        }
        return getId() != null && getId().equals(((Domain) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Domain{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
