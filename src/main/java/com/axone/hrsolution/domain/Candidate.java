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
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "current_salary")
    private Double currentSalary;

    @Column(name = "desired_salary")
    private Double desiredSalary;

    @NotNull
    @Column(name = "has_contract", nullable = false)
    private Boolean hasContract;

    @NotNull
    @Column(name = "hired", nullable = false)
    private Boolean hired;

    @Column(name = "rate")
    private Float rate;

    @JsonIgnoreProperties(
        value = {
            "hardSkills",
            "softSkills",
            "educations",
            "employments",
            "projects",
            "achievements",
            "attachedDocs",
            "altActivities",
            "candidate",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private TechnicalCV techCV;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attendee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attendee", "application" }, allowSetters = true)
    private Set<Interview> interviewResults = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Set<Resume> candidateResumes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_candidate__domain",
        joinColumns = @JoinColumn(name = "candidate_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recruiters", "candidates", "applications", "employer" }, allowSetters = true)
    private Set<Domain> domains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_candidate__applications",
        joinColumns = @JoinColumn(name = "candidate_id"),
        inverseJoinColumns = @JoinColumn(name = "applications_id")
    )
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

    @JsonIgnoreProperties(value = { "template", "candidate", "recruiter", "employer", "application" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "candidate")
    private Contract contract;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employer", "mediator", "candidate" }, allowSetters = true)
    private Set<NDA> ndaStatuses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Candidate firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Candidate lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLinkedinUrl() {
        return this.linkedinUrl;
    }

    public Candidate linkedinUrl(String linkedinUrl) {
        this.setLinkedinUrl(linkedinUrl);
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Candidate fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public Candidate yearsOfExperience(Integer yearsOfExperience) {
        this.setYearsOfExperience(yearsOfExperience);
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Double getCurrentSalary() {
        return this.currentSalary;
    }

    public Candidate currentSalary(Double currentSalary) {
        this.setCurrentSalary(currentSalary);
        return this;
    }

    public void setCurrentSalary(Double currentSalary) {
        this.currentSalary = currentSalary;
    }

    public Double getDesiredSalary() {
        return this.desiredSalary;
    }

    public Candidate desiredSalary(Double desiredSalary) {
        this.setDesiredSalary(desiredSalary);
        return this;
    }

    public void setDesiredSalary(Double desiredSalary) {
        this.desiredSalary = desiredSalary;
    }

    public Boolean getHasContract() {
        return this.hasContract;
    }

    public Candidate hasContract(Boolean hasContract) {
        this.setHasContract(hasContract);
        return this;
    }

    public void setHasContract(Boolean hasContract) {
        this.hasContract = hasContract;
    }

    public Boolean getHired() {
        return this.hired;
    }

    public Candidate hired(Boolean hired) {
        this.setHired(hired);
        return this;
    }

    public void setHired(Boolean hired) {
        this.hired = hired;
    }

    public Float getRate() {
        return this.rate;
    }

    public Candidate rate(Float rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public TechnicalCV getTechCV() {
        return this.techCV;
    }

    public void setTechCV(TechnicalCV technicalCV) {
        this.techCV = technicalCV;
    }

    public Candidate techCV(TechnicalCV technicalCV) {
        this.setTechCV(technicalCV);
        return this;
    }

    public Set<Interview> getInterviewResults() {
        return this.interviewResults;
    }

    public void setInterviewResults(Set<Interview> interviews) {
        if (this.interviewResults != null) {
            this.interviewResults.forEach(i -> i.setAttendee(null));
        }
        if (interviews != null) {
            interviews.forEach(i -> i.setAttendee(this));
        }
        this.interviewResults = interviews;
    }

    public Candidate interviewResults(Set<Interview> interviews) {
        this.setInterviewResults(interviews);
        return this;
    }

    public Candidate addInterviewResult(Interview interview) {
        this.interviewResults.add(interview);
        interview.setAttendee(this);
        return this;
    }

    public Candidate removeInterviewResult(Interview interview) {
        this.interviewResults.remove(interview);
        interview.setAttendee(null);
        return this;
    }

    public Set<Resume> getCandidateResumes() {
        return this.candidateResumes;
    }

    public void setCandidateResumes(Set<Resume> resumes) {
        if (this.candidateResumes != null) {
            this.candidateResumes.forEach(i -> i.setOwner(null));
        }
        if (resumes != null) {
            resumes.forEach(i -> i.setOwner(this));
        }
        this.candidateResumes = resumes;
    }

    public Candidate candidateResumes(Set<Resume> resumes) {
        this.setCandidateResumes(resumes);
        return this;
    }

    public Candidate addCandidateResume(Resume resume) {
        this.candidateResumes.add(resume);
        resume.setOwner(this);
        return this;
    }

    public Candidate removeCandidateResume(Resume resume) {
        this.candidateResumes.remove(resume);
        resume.setOwner(null);
        return this;
    }

    public Set<Domain> getDomains() {
        return this.domains;
    }

    public void setDomains(Set<Domain> domains) {
        this.domains = domains;
    }

    public Candidate domains(Set<Domain> domains) {
        this.setDomains(domains);
        return this;
    }

    public Candidate addDomain(Domain domain) {
        this.domains.add(domain);
        return this;
    }

    public Candidate removeDomain(Domain domain) {
        this.domains.remove(domain);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Candidate applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Candidate addApplications(Application application) {
        this.applications.add(application);
        return this;
    }

    public Candidate removeApplications(Application application) {
        this.applications.remove(application);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        if (this.contract != null) {
            this.contract.setCandidate(null);
        }
        if (contract != null) {
            contract.setCandidate(this);
        }
        this.contract = contract;
    }

    public Candidate contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Set<NDA> getNdaStatuses() {
        return this.ndaStatuses;
    }

    public void setNdaStatuses(Set<NDA> nDAS) {
        if (this.ndaStatuses != null) {
            this.ndaStatuses.forEach(i -> i.setCandidate(null));
        }
        if (nDAS != null) {
            nDAS.forEach(i -> i.setCandidate(this));
        }
        this.ndaStatuses = nDAS;
    }

    public Candidate ndaStatuses(Set<NDA> nDAS) {
        this.setNdaStatuses(nDAS);
        return this;
    }

    public Candidate addNdaStatus(NDA nDA) {
        this.ndaStatuses.add(nDA);
        nDA.setCandidate(this);
        return this;
    }

    public Candidate removeNdaStatus(NDA nDA) {
        this.ndaStatuses.remove(nDA);
        nDA.setCandidate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidate)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", linkedinUrl='" + getLinkedinUrl() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", yearsOfExperience=" + getYearsOfExperience() +
            ", currentSalary=" + getCurrentSalary() +
            ", desiredSalary=" + getDesiredSalary() +
            ", hasContract='" + getHasContract() + "'" +
            ", hired='" + getHired() + "'" +
            ", rate=" + getRate() +
            "}";
    }
}
