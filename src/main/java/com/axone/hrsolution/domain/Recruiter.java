package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recruiter.
 */
@Entity
@Table(name = "recruiter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recruiter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "profile_image_content_type")
    private String profileImageContentType;

    @Column(name = "address")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Column(name = "linkedin_url", nullable = false)
    private String linkedinUrl;

    @Column(name = "approved_experience")
    private Boolean approvedExperience;

    @Column(name = "score")
    private Float score;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User relatedUser;

    @JsonIgnoreProperties(value = { "relatedToAccount", "recruiter", "employer", "admin" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Wallet wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruiter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatedApplication", "recruiter" }, allowSetters = true)
    private Set<Request> requests = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_recruiter__applications",
        joinColumns = @JoinColumn(name = "recruiter_id"),
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

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_recruiter__operational_domain",
        joinColumns = @JoinColumn(name = "recruiter_id"),
        inverseJoinColumns = @JoinColumn(name = "operational_domain_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recruiters", "candidates", "applications", "employer" }, allowSetters = true)
    private Set<Domain> operationalDomains = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mediator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employer", "mediator", "candidate" }, allowSetters = true)
    private Set<NDA> ndaStatuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruiter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "template", "candidate", "recruiter", "employer", "application" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recruiter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Recruiter firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Recruiter lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getProfileImage() {
        return this.profileImage;
    }

    public Recruiter profileImage(byte[] profileImage) {
        this.setProfileImage(profileImage);
        return this;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageContentType() {
        return this.profileImageContentType;
    }

    public Recruiter profileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
        return this;
    }

    public void setProfileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
    }

    public String getAddress() {
        return this.address;
    }

    public Recruiter address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRole getRole() {
        return this.role;
    }

    public Recruiter role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public Recruiter status(UserStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public Recruiter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public Recruiter label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLinkedinUrl() {
        return this.linkedinUrl;
    }

    public Recruiter linkedinUrl(String linkedinUrl) {
        this.setLinkedinUrl(linkedinUrl);
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public Boolean getApprovedExperience() {
        return this.approvedExperience;
    }

    public Recruiter approvedExperience(Boolean approvedExperience) {
        this.setApprovedExperience(approvedExperience);
        return this;
    }

    public void setApprovedExperience(Boolean approvedExperience) {
        this.approvedExperience = approvedExperience;
    }

    public Float getScore() {
        return this.score;
    }

    public Recruiter score(Float score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public User getRelatedUser() {
        return this.relatedUser;
    }

    public void setRelatedUser(User user) {
        this.relatedUser = user;
    }

    public Recruiter relatedUser(User user) {
        this.setRelatedUser(user);
        return this;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Recruiter wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public Set<Request> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<Request> requests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setRecruiter(null));
        }
        if (requests != null) {
            requests.forEach(i -> i.setRecruiter(this));
        }
        this.requests = requests;
    }

    public Recruiter requests(Set<Request> requests) {
        this.setRequests(requests);
        return this;
    }

    public Recruiter addRequests(Request request) {
        this.requests.add(request);
        request.setRecruiter(this);
        return this;
    }

    public Recruiter removeRequests(Request request) {
        this.requests.remove(request);
        request.setRecruiter(null);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Recruiter applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Recruiter addApplications(Application application) {
        this.applications.add(application);
        return this;
    }

    public Recruiter removeApplications(Application application) {
        this.applications.remove(application);
        return this;
    }

    public Set<Domain> getOperationalDomains() {
        return this.operationalDomains;
    }

    public void setOperationalDomains(Set<Domain> domains) {
        this.operationalDomains = domains;
    }

    public Recruiter operationalDomains(Set<Domain> domains) {
        this.setOperationalDomains(domains);
        return this;
    }

    public Recruiter addOperationalDomain(Domain domain) {
        this.operationalDomains.add(domain);
        return this;
    }

    public Recruiter removeOperationalDomain(Domain domain) {
        this.operationalDomains.remove(domain);
        return this;
    }

    public Set<NDA> getNdaStatuses() {
        return this.ndaStatuses;
    }

    public void setNdaStatuses(Set<NDA> nDAS) {
        if (this.ndaStatuses != null) {
            this.ndaStatuses.forEach(i -> i.setMediator(null));
        }
        if (nDAS != null) {
            nDAS.forEach(i -> i.setMediator(this));
        }
        this.ndaStatuses = nDAS;
    }

    public Recruiter ndaStatuses(Set<NDA> nDAS) {
        this.setNdaStatuses(nDAS);
        return this;
    }

    public Recruiter addNdaStatus(NDA nDA) {
        this.ndaStatuses.add(nDA);
        nDA.setMediator(this);
        return this;
    }

    public Recruiter removeNdaStatus(NDA nDA) {
        this.ndaStatuses.remove(nDA);
        nDA.setMediator(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setRecruiter(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setRecruiter(this));
        }
        this.contracts = contracts;
    }

    public Recruiter contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Recruiter addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setRecruiter(this);
        return this;
    }

    public Recruiter removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setRecruiter(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recruiter)) {
            return false;
        }
        return getId() != null && getId().equals(((Recruiter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recruiter{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", profileImage='" + getProfileImage() + "'" +
            ", profileImageContentType='" + getProfileImageContentType() + "'" +
            ", address='" + getAddress() + "'" +
            ", role='" + getRole() + "'" +
            ", status='" + getStatus() + "'" +
            ", name='" + getName() + "'" +
            ", label='" + getLabel() + "'" +
            ", linkedinUrl='" + getLinkedinUrl() + "'" +
            ", approvedExperience='" + getApprovedExperience() + "'" +
            ", score=" + getScore() +
            "}";
    }
}
