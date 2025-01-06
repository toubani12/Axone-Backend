package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TechCVAchievement.
 */
@Entity
@Table(name = "tech_cv_achievement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TechCVAchievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "achievement", nullable = false)
    private String achievement;

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

    public TechCVAchievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAchievement() {
        return this.achievement;
    }

    public TechCVAchievement achievement(String achievement) {
        this.setAchievement(achievement);
        return this;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public TechnicalCV getTechnicalCV() {
        return this.technicalCV;
    }

    public void setTechnicalCV(TechnicalCV technicalCV) {
        this.technicalCV = technicalCV;
    }

    public TechCVAchievement technicalCV(TechnicalCV technicalCV) {
        this.setTechnicalCV(technicalCV);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TechCVAchievement)) {
            return false;
        }
        return getId() != null && getId().equals(((TechCVAchievement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TechCVAchievement{" +
            "id=" + getId() +
            ", achievement='" + getAchievement() + "'" +
            "}";
    }
}
