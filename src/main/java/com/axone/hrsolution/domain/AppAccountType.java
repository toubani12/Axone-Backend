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
 * A AppAccountType.
 */
@Entity
@Table(name = "app_account_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppAccountType implements Serializable {

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
    @JsonIgnoreProperties(value = { "relatedUser", "types", "providers", "relatedWallet", "ifEmployer" }, allowSetters = true)
    private Set<AppAccount> appAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppAccountType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public AppAccountType type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<AppAccount> getAppAccounts() {
        return this.appAccounts;
    }

    public void setAppAccounts(Set<AppAccount> appAccounts) {
        if (this.appAccounts != null) {
            this.appAccounts.forEach(i -> i.removeType(this));
        }
        if (appAccounts != null) {
            appAccounts.forEach(i -> i.addType(this));
        }
        this.appAccounts = appAccounts;
    }

    public AppAccountType appAccounts(Set<AppAccount> appAccounts) {
        this.setAppAccounts(appAccounts);
        return this;
    }

    public AppAccountType addAppAccount(AppAccount appAccount) {
        this.appAccounts.add(appAccount);
        appAccount.getTypes().add(this);
        return this;
    }

    public AppAccountType removeAppAccount(AppAccount appAccount) {
        this.appAccounts.remove(appAccount);
        appAccount.getTypes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppAccountType)) {
            return false;
        }
        return getId() != null && getId().equals(((AppAccountType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppAccountType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
