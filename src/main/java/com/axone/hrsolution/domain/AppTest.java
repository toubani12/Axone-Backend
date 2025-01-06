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
 * A AppTest.
 */
@Entity
@Table(name = "app_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "invitation_link", nullable = false)
    private String invitationLink;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "status")
    private String status;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "test_id")
    private Long testID;

    @Column(name = "algorithm")
    private String algorithm;

    @Column(name = "is_code_test")
    private Boolean isCodeTest;

    @Column(name = "duration")
    private Integer duration;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appTest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appTest" }, allowSetters = true)
    private Set<CustomQuestion> customQuestions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_app_test__type",
        joinColumns = @JoinColumn(name = "app_test_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appTests" }, allowSetters = true)
    private Set<AppTestType> types = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AppTest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvitationLink() {
        return this.invitationLink;
    }

    public AppTest invitationLink(String invitationLink) {
        this.setInvitationLink(invitationLink);
        return this;
    }

    public void setInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public Integer getTotalScore() {
        return this.totalScore;
    }

    public AppTest totalScore(Integer totalScore) {
        this.setTotalScore(totalScore);
        return this;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getStatus() {
        return this.status;
    }

    public AppTest status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public AppTest completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getTestID() {
        return this.testID;
    }

    public AppTest testID(Long testID) {
        this.setTestID(testID);
        return this;
    }

    public void setTestID(Long testID) {
        this.testID = testID;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public AppTest algorithm(String algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Boolean getIsCodeTest() {
        return this.isCodeTest;
    }

    public AppTest isCodeTest(Boolean isCodeTest) {
        this.setIsCodeTest(isCodeTest);
        return this;
    }

    public void setIsCodeTest(Boolean isCodeTest) {
        this.isCodeTest = isCodeTest;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public AppTest duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<CustomQuestion> getCustomQuestions() {
        return this.customQuestions;
    }

    public void setCustomQuestions(Set<CustomQuestion> customQuestions) {
        if (this.customQuestions != null) {
            this.customQuestions.forEach(i -> i.setAppTest(null));
        }
        if (customQuestions != null) {
            customQuestions.forEach(i -> i.setAppTest(this));
        }
        this.customQuestions = customQuestions;
    }

    public AppTest customQuestions(Set<CustomQuestion> customQuestions) {
        this.setCustomQuestions(customQuestions);
        return this;
    }

    public AppTest addCustomQuestions(CustomQuestion customQuestion) {
        this.customQuestions.add(customQuestion);
        customQuestion.setAppTest(this);
        return this;
    }

    public AppTest removeCustomQuestions(CustomQuestion customQuestion) {
        this.customQuestions.remove(customQuestion);
        customQuestion.setAppTest(null);
        return this;
    }

    public Set<AppTestType> getTypes() {
        return this.types;
    }

    public void setTypes(Set<AppTestType> appTestTypes) {
        this.types = appTestTypes;
    }

    public AppTest types(Set<AppTestType> appTestTypes) {
        this.setTypes(appTestTypes);
        return this;
    }

    public AppTest addType(AppTestType appTestType) {
        this.types.add(appTestType);
        return this;
    }

    public AppTest removeType(AppTestType appTestType) {
        this.types.remove(appTestType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppTest)) {
            return false;
        }
        return getId() != null && getId().equals(((AppTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppTest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", invitationLink='" + getInvitationLink() + "'" +
            ", totalScore=" + getTotalScore() +
            ", status='" + getStatus() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", testID=" + getTestID() +
            ", algorithm='" + getAlgorithm() + "'" +
            ", isCodeTest='" + getIsCodeTest() + "'" +
            ", duration=" + getDuration() +
            "}";
    }
}
