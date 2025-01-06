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
 * A AppTestType.
 */
@Entity
@Table(name = "app_test_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppTestType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "types")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customQuestions", "types" }, allowSetters = true)
    private Set<AppTest> appTests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppTestType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public AppTestType type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<AppTest> getAppTests() {
        return this.appTests;
    }

    public void setAppTests(Set<AppTest> appTests) {
        if (this.appTests != null) {
            this.appTests.forEach(i -> i.removeType(this));
        }
        if (appTests != null) {
            appTests.forEach(i -> i.addType(this));
        }
        this.appTests = appTests;
    }

    public AppTestType appTests(Set<AppTest> appTests) {
        this.setAppTests(appTests);
        return this;
    }

    public AppTestType addAppTest(AppTest appTest) {
        this.appTests.add(appTest);
        appTest.getTypes().add(this);
        return this;
    }

    public AppTestType removeAppTest(AppTest appTest) {
        this.appTests.remove(appTest);
        appTest.getTypes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppTestType)) {
            return false;
        }
        return getId() != null && getId().equals(((AppTestType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppTestType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
