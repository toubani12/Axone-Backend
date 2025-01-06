package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.RequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @NotNull
    @Column(name = "expression_of_interest", nullable = false)
    private String expressionOfInterest;

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
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Application relatedApplication;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    private Recruiter recruiter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Request id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public Request status(RequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getExpressionOfInterest() {
        return this.expressionOfInterest;
    }

    public Request expressionOfInterest(String expressionOfInterest) {
        this.setExpressionOfInterest(expressionOfInterest);
        return this;
    }

    public void setExpressionOfInterest(String expressionOfInterest) {
        this.expressionOfInterest = expressionOfInterest;
    }

    public Application getRelatedApplication() {
        return this.relatedApplication;
    }

    public void setRelatedApplication(Application application) {
        this.relatedApplication = application;
    }

    public Request relatedApplication(Application application) {
        this.setRelatedApplication(application);
        return this;
    }

    public Recruiter getRecruiter() {
        return this.recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public Request recruiter(Recruiter recruiter) {
        this.setRecruiter(recruiter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        return getId() != null && getId().equals(((Request) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", expressionOfInterest='" + getExpressionOfInterest() + "'" +
            "}";
    }
}
