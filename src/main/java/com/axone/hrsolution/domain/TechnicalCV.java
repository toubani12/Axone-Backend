package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.TechCVLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TechnicalCV.
 */
@Entity
@Table(name = "technical_cv")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TechnicalCV implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private TechCVLevel level;

    @Column(name = "profile_description")
    private String profileDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVHardSkills> hardSkills = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVSoftSkills> softSkills = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVEducation> educations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVEmployment> employments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVProject> projects = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVAchievement> achievements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVDocs> attachedDocs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "technicalCV")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicalCV" }, allowSetters = true)
    private Set<TechCVAltActivities> altActivities = new HashSet<>();

    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "techCV")
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TechnicalCV id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TechnicalCV name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TechCVLevel getLevel() {
        return this.level;
    }

    public TechnicalCV level(TechCVLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(TechCVLevel level) {
        this.level = level;
    }

    public String getProfileDescription() {
        return this.profileDescription;
    }

    public TechnicalCV profileDescription(String profileDescription) {
        this.setProfileDescription(profileDescription);
        return this;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public Set<TechCVHardSkills> getHardSkills() {
        return this.hardSkills;
    }

    public void setHardSkills(Set<TechCVHardSkills> techCVHardSkills) {
        if (this.hardSkills != null) {
            this.hardSkills.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVHardSkills != null) {
            techCVHardSkills.forEach(i -> i.setTechnicalCV(this));
        }
        this.hardSkills = techCVHardSkills;
    }

    public TechnicalCV hardSkills(Set<TechCVHardSkills> techCVHardSkills) {
        this.setHardSkills(techCVHardSkills);
        return this;
    }

    public TechnicalCV addHardSkills(TechCVHardSkills techCVHardSkills) {
        this.hardSkills.add(techCVHardSkills);
        techCVHardSkills.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeHardSkills(TechCVHardSkills techCVHardSkills) {
        this.hardSkills.remove(techCVHardSkills);
        techCVHardSkills.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVSoftSkills> getSoftSkills() {
        return this.softSkills;
    }

    public void setSoftSkills(Set<TechCVSoftSkills> techCVSoftSkills) {
        if (this.softSkills != null) {
            this.softSkills.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVSoftSkills != null) {
            techCVSoftSkills.forEach(i -> i.setTechnicalCV(this));
        }
        this.softSkills = techCVSoftSkills;
    }

    public TechnicalCV softSkills(Set<TechCVSoftSkills> techCVSoftSkills) {
        this.setSoftSkills(techCVSoftSkills);
        return this;
    }

    public TechnicalCV addSoftSkills(TechCVSoftSkills techCVSoftSkills) {
        this.softSkills.add(techCVSoftSkills);
        techCVSoftSkills.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeSoftSkills(TechCVSoftSkills techCVSoftSkills) {
        this.softSkills.remove(techCVSoftSkills);
        techCVSoftSkills.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVEducation> getEducations() {
        return this.educations;
    }

    public void setEducations(Set<TechCVEducation> techCVEducations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVEducations != null) {
            techCVEducations.forEach(i -> i.setTechnicalCV(this));
        }
        this.educations = techCVEducations;
    }

    public TechnicalCV educations(Set<TechCVEducation> techCVEducations) {
        this.setEducations(techCVEducations);
        return this;
    }

    public TechnicalCV addEducation(TechCVEducation techCVEducation) {
        this.educations.add(techCVEducation);
        techCVEducation.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeEducation(TechCVEducation techCVEducation) {
        this.educations.remove(techCVEducation);
        techCVEducation.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVEmployment> getEmployments() {
        return this.employments;
    }

    public void setEmployments(Set<TechCVEmployment> techCVEmployments) {
        if (this.employments != null) {
            this.employments.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVEmployments != null) {
            techCVEmployments.forEach(i -> i.setTechnicalCV(this));
        }
        this.employments = techCVEmployments;
    }

    public TechnicalCV employments(Set<TechCVEmployment> techCVEmployments) {
        this.setEmployments(techCVEmployments);
        return this;
    }

    public TechnicalCV addEmployments(TechCVEmployment techCVEmployment) {
        this.employments.add(techCVEmployment);
        techCVEmployment.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeEmployments(TechCVEmployment techCVEmployment) {
        this.employments.remove(techCVEmployment);
        techCVEmployment.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVProject> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<TechCVProject> techCVProjects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVProjects != null) {
            techCVProjects.forEach(i -> i.setTechnicalCV(this));
        }
        this.projects = techCVProjects;
    }

    public TechnicalCV projects(Set<TechCVProject> techCVProjects) {
        this.setProjects(techCVProjects);
        return this;
    }

    public TechnicalCV addProjects(TechCVProject techCVProject) {
        this.projects.add(techCVProject);
        techCVProject.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeProjects(TechCVProject techCVProject) {
        this.projects.remove(techCVProject);
        techCVProject.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVAchievement> getAchievements() {
        return this.achievements;
    }

    public void setAchievements(Set<TechCVAchievement> techCVAchievements) {
        if (this.achievements != null) {
            this.achievements.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVAchievements != null) {
            techCVAchievements.forEach(i -> i.setTechnicalCV(this));
        }
        this.achievements = techCVAchievements;
    }

    public TechnicalCV achievements(Set<TechCVAchievement> techCVAchievements) {
        this.setAchievements(techCVAchievements);
        return this;
    }

    public TechnicalCV addAchievements(TechCVAchievement techCVAchievement) {
        this.achievements.add(techCVAchievement);
        techCVAchievement.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeAchievements(TechCVAchievement techCVAchievement) {
        this.achievements.remove(techCVAchievement);
        techCVAchievement.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVDocs> getAttachedDocs() {
        return this.attachedDocs;
    }

    public void setAttachedDocs(Set<TechCVDocs> techCVDocs) {
        if (this.attachedDocs != null) {
            this.attachedDocs.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVDocs != null) {
            techCVDocs.forEach(i -> i.setTechnicalCV(this));
        }
        this.attachedDocs = techCVDocs;
    }

    public TechnicalCV attachedDocs(Set<TechCVDocs> techCVDocs) {
        this.setAttachedDocs(techCVDocs);
        return this;
    }

    public TechnicalCV addAttachedDocs(TechCVDocs techCVDocs) {
        this.attachedDocs.add(techCVDocs);
        techCVDocs.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeAttachedDocs(TechCVDocs techCVDocs) {
        this.attachedDocs.remove(techCVDocs);
        techCVDocs.setTechnicalCV(null);
        return this;
    }

    public Set<TechCVAltActivities> getAltActivities() {
        return this.altActivities;
    }

    public void setAltActivities(Set<TechCVAltActivities> techCVAltActivities) {
        if (this.altActivities != null) {
            this.altActivities.forEach(i -> i.setTechnicalCV(null));
        }
        if (techCVAltActivities != null) {
            techCVAltActivities.forEach(i -> i.setTechnicalCV(this));
        }
        this.altActivities = techCVAltActivities;
    }

    public TechnicalCV altActivities(Set<TechCVAltActivities> techCVAltActivities) {
        this.setAltActivities(techCVAltActivities);
        return this;
    }

    public TechnicalCV addAltActivities(TechCVAltActivities techCVAltActivities) {
        this.altActivities.add(techCVAltActivities);
        techCVAltActivities.setTechnicalCV(this);
        return this;
    }

    public TechnicalCV removeAltActivities(TechCVAltActivities techCVAltActivities) {
        this.altActivities.remove(techCVAltActivities);
        techCVAltActivities.setTechnicalCV(null);
        return this;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        if (this.candidate != null) {
            this.candidate.setTechCV(null);
        }
        if (candidate != null) {
            candidate.setTechCV(this);
        }
        this.candidate = candidate;
    }

    public TechnicalCV candidate(Candidate candidate) {
        this.setCandidate(candidate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TechnicalCV)) {
            return false;
        }
        return getId() != null && getId().equals(((TechnicalCV) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TechnicalCV{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level='" + getLevel() + "'" +
            ", profileDescription='" + getProfileDescription() + "'" +
            "}";
    }
}
