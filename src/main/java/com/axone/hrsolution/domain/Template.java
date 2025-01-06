package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.TemplateContractType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Template.
 */
@Entity
@Table(name = "template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Template implements Serializable {

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

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "standard", nullable = false)
    private Boolean standard;

    @Lob
    @Column(name = "doc_link", nullable = false)
    private byte[] docLink;

    @NotNull
    @Column(name = "doc_link_content_type", nullable = false)
    private String docLinkContentType;

    @JsonIgnoreProperties(value = { "template", "candidate", "recruiter", "employer", "application" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "template")
    private Contract contract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    private Employer owner;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "contractTemplates")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Template id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Template label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TemplateContractType getType() {
        return this.type;
    }

    public Template type(TemplateContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(TemplateContractType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Template description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStandard() {
        return this.standard;
    }

    public Template standard(Boolean standard) {
        this.setStandard(standard);
        return this;
    }

    public void setStandard(Boolean standard) {
        this.standard = standard;
    }

    public byte[] getDocLink() {
        return this.docLink;
    }

    public Template docLink(byte[] docLink) {
        this.setDocLink(docLink);
        return this;
    }

    public void setDocLink(byte[] docLink) {
        this.docLink = docLink;
    }

    public String getDocLinkContentType() {
        return this.docLinkContentType;
    }

    public Template docLinkContentType(String docLinkContentType) {
        this.docLinkContentType = docLinkContentType;
        return this;
    }

    public void setDocLinkContentType(String docLinkContentType) {
        this.docLinkContentType = docLinkContentType;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        if (this.contract != null) {
            this.contract.setTemplate(null);
        }
        if (contract != null) {
            contract.setTemplate(this);
        }
        this.contract = contract;
    }

    public Template contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Employer getOwner() {
        return this.owner;
    }

    public void setOwner(Employer employer) {
        this.owner = employer;
    }

    public Template owner(Employer employer) {
        this.setOwner(employer);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeContractTemplate(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addContractTemplate(this));
        }
        this.applications = applications;
    }

    public Template applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Template addApplication(Application application) {
        this.applications.add(application);
        application.getContractTemplates().add(this);
        return this;
    }

    public Template removeApplication(Application application) {
        this.applications.remove(application);
        application.getContractTemplates().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Template)) {
            return false;
        }
        return getId() != null && getId().equals(((Template) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Template{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", standard='" + getStandard() + "'" +
            ", docLink='" + getDocLink() + "'" +
            ", docLinkContentType='" + getDocLinkContentType() + "'" +
            "}";
    }
}
