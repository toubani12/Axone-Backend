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
 * A Provider.
 */
@Entity
@Table(name = "provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country")
    private String country;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "providers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatedUser", "types", "providers", "relatedWallet", "ifEmployer" }, allowSetters = true)
    private Set<AppAccount> appAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Provider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Provider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public Provider country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<AppAccount> getAppAccounts() {
        return this.appAccounts;
    }

    public void setAppAccounts(Set<AppAccount> appAccounts) {
        if (this.appAccounts != null) {
            this.appAccounts.forEach(i -> i.removeProvider(this));
        }
        if (appAccounts != null) {
            appAccounts.forEach(i -> i.addProvider(this));
        }
        this.appAccounts = appAccounts;
    }

    public Provider appAccounts(Set<AppAccount> appAccounts) {
        this.setAppAccounts(appAccounts);
        return this;
    }

    public Provider addAppAccount(AppAccount appAccount) {
        this.appAccounts.add(appAccount);
        appAccount.getProviders().add(this);
        return this;
    }

    public Provider removeAppAccount(AppAccount appAccount) {
        this.appAccounts.remove(appAccount);
        appAccount.getProviders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Provider)) {
            return false;
        }
        return getId() != null && getId().equals(((Provider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Provider{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
