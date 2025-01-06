package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.NDAStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NDA.
 */
@Entity
@Table(name = "nda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NDA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "document", nullable = false)
    private byte[] document;

    @NotNull
    @Column(name = "document_content_type", nullable = false)
    private String documentContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NDAStatus status;

    @NotNull
    @Column(name = "period", nullable = false)
    private LocalDate period;

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
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    private Recruiter mediator;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NDA id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocument() {
        return this.document;
    }

    public NDA document(byte[] document) {
        this.setDocument(document);
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return this.documentContentType;
    }

    public NDA documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public NDAStatus getStatus() {
        return this.status;
    }

    public NDA status(NDAStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NDAStatus status) {
        this.status = status;
    }

    public LocalDate getPeriod() {
        return this.period;
    }

    public NDA period(LocalDate period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public NDA employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    public Recruiter getMediator() {
        return this.mediator;
    }

    public void setMediator(Recruiter recruiter) {
        this.mediator = recruiter;
    }

    public NDA mediator(Recruiter recruiter) {
        this.setMediator(recruiter);
        return this;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public NDA candidate(Candidate candidate) {
        this.setCandidate(candidate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NDA)) {
            return false;
        }
        return getId() != null && getId().equals(((NDA) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NDA{" +
            "id=" + getId() +
            ", document='" + getDocument() + "'" +
            ", documentContentType='" + getDocumentContentType() + "'" +
            ", status='" + getStatus() + "'" +
            ", period='" + getPeriod() + "'" +
            "}";
    }
}
