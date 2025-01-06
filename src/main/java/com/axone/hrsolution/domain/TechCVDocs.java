package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TechCVDocs.
 */
@Entity
@Table(name = "tech_cv_docs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TechCVDocs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "attached_doc", nullable = false)
    private byte[] attachedDoc;

    @NotNull
    @Column(name = "attached_doc_content_type", nullable = false)
    private String attachedDocContentType;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private TechnicalCV technicalCV;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TechCVDocs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getAttachedDoc() {
        return this.attachedDoc;
    }

    public TechCVDocs attachedDoc(byte[] attachedDoc) {
        this.setAttachedDoc(attachedDoc);
        return this;
    }

    public void setAttachedDoc(byte[] attachedDoc) {
        this.attachedDoc = attachedDoc;
    }

    public String getAttachedDocContentType() {
        return this.attachedDocContentType;
    }

    public TechCVDocs attachedDocContentType(String attachedDocContentType) {
        this.attachedDocContentType = attachedDocContentType;
        return this;
    }

    public void setAttachedDocContentType(String attachedDocContentType) {
        this.attachedDocContentType = attachedDocContentType;
    }

    public TechnicalCV getTechnicalCV() {
        return this.technicalCV;
    }

    public void setTechnicalCV(TechnicalCV technicalCV) {
        this.technicalCV = technicalCV;
    }

    public TechCVDocs technicalCV(TechnicalCV technicalCV) {
        this.setTechnicalCV(technicalCV);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TechCVDocs)) {
            return false;
        }
        return getId() != null && getId().equals(((TechCVDocs) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TechCVDocs{" +
            "id=" + getId() +
            ", attachedDoc='" + getAttachedDoc() + "'" +
            ", attachedDocContentType='" + getAttachedDocContentType() + "'" +
            "}";
    }
}
