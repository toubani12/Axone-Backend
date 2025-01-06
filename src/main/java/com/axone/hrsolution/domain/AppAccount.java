package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppAccount.
 */
@Entity
@Table(name = "app_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "cvv")
    private Integer cvv;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User relatedUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_app_account__type",
        joinColumns = @JoinColumn(name = "app_account_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appAccounts" }, allowSetters = true)
    private Set<AppAccountType> types = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_app_account__provider",
        joinColumns = @JoinColumn(name = "app_account_id"),
        inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appAccounts" }, allowSetters = true)
    private Set<Provider> providers = new HashSet<>();

    @JsonIgnoreProperties(value = { "relatedToAccount", "recruiter", "employer", "admin" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "relatedToAccount")
    private Wallet relatedWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    private Employer ifEmployer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountNumber() {
        return this.accountNumber;
    }

    public AppAccount accountNumber(Long accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getCardNumber() {
        return this.cardNumber;
    }

    public AppAccount cardNumber(Long cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public AppAccount endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCvv() {
        return this.cvv;
    }

    public AppAccount cvv(Integer cvv) {
        this.setCvv(cvv);
        return this;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public User getRelatedUser() {
        return this.relatedUser;
    }

    public void setRelatedUser(User user) {
        this.relatedUser = user;
    }

    public AppAccount relatedUser(User user) {
        this.setRelatedUser(user);
        return this;
    }

    public Set<AppAccountType> getTypes() {
        return this.types;
    }

    public void setTypes(Set<AppAccountType> appAccountTypes) {
        this.types = appAccountTypes;
    }

    public AppAccount types(Set<AppAccountType> appAccountTypes) {
        this.setTypes(appAccountTypes);
        return this;
    }

    public AppAccount addType(AppAccountType appAccountType) {
        this.types.add(appAccountType);
        return this;
    }

    public AppAccount removeType(AppAccountType appAccountType) {
        this.types.remove(appAccountType);
        return this;
    }

    public Set<Provider> getProviders() {
        return this.providers;
    }

    public void setProviders(Set<Provider> providers) {
        this.providers = providers;
    }

    public AppAccount providers(Set<Provider> providers) {
        this.setProviders(providers);
        return this;
    }

    public AppAccount addProvider(Provider provider) {
        this.providers.add(provider);
        return this;
    }

    public AppAccount removeProvider(Provider provider) {
        this.providers.remove(provider);
        return this;
    }

    public Wallet getRelatedWallet() {
        return this.relatedWallet;
    }

    public void setRelatedWallet(Wallet wallet) {
        if (this.relatedWallet != null) {
            this.relatedWallet.setRelatedToAccount(null);
        }
        if (wallet != null) {
            wallet.setRelatedToAccount(this);
        }
        this.relatedWallet = wallet;
    }

    public AppAccount relatedWallet(Wallet wallet) {
        this.setRelatedWallet(wallet);
        return this;
    }

    public Employer getIfEmployer() {
        return this.ifEmployer;
    }

    public void setIfEmployer(Employer employer) {
        this.ifEmployer = employer;
    }

    public AppAccount ifEmployer(Employer employer) {
        this.setIfEmployer(employer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((AppAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppAccount{" +
            "id=" + getId() +
            ", accountNumber=" + getAccountNumber() +
            ", cardNumber=" + getCardNumber() +
            ", endDate='" + getEndDate() + "'" +
            ", cvv=" + getCvv() +
            "}";
    }
}
