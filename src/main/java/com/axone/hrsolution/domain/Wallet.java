package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.WalletStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Wallet.
 */
@Entity
@Table(name = "wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status;

    @JsonIgnoreProperties(value = { "relatedUser", "types", "providers", "relatedWallet", "ifEmployer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AppAccount relatedToAccount;

    @JsonIgnoreProperties(
        value = { "relatedUser", "wallet", "requests", "applications", "operationalDomains", "ndaStatuses", "contracts" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "wallet")
    private Recruiter recruiter;

    @JsonIgnoreProperties(
        value = {
            "relatedUser", "wallet", "operationalDomains", "paymentAccounts", "applications", "contracts", "templates", "ndaStatuses",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "wallet")
    private Employer employer;

    @JsonIgnoreProperties(value = { "relatedUser", "systemWallet" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "systemWallet")
    private Admin admin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wallet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Wallet balance(Double balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public WalletStatus getStatus() {
        return this.status;
    }

    public Wallet status(WalletStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public AppAccount getRelatedToAccount() {
        return this.relatedToAccount;
    }

    public void setRelatedToAccount(AppAccount appAccount) {
        this.relatedToAccount = appAccount;
    }

    public Wallet relatedToAccount(AppAccount appAccount) {
        this.setRelatedToAccount(appAccount);
        return this;
    }

    public Recruiter getRecruiter() {
        return this.recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        if (this.recruiter != null) {
            this.recruiter.setWallet(null);
        }
        if (recruiter != null) {
            recruiter.setWallet(this);
        }
        this.recruiter = recruiter;
    }

    public Wallet recruiter(Recruiter recruiter) {
        this.setRecruiter(recruiter);
        return this;
    }

    public Employer getEmployer() {
        return this.employer;
    }

    public void setEmployer(Employer employer) {
        if (this.employer != null) {
            this.employer.setWallet(null);
        }
        if (employer != null) {
            employer.setWallet(this);
        }
        this.employer = employer;
    }

    public Wallet employer(Employer employer) {
        this.setEmployer(employer);
        return this;
    }

    public Admin getAdmin() {
        return this.admin;
    }

    public void setAdmin(Admin admin) {
        if (this.admin != null) {
            this.admin.setSystemWallet(null);
        }
        if (admin != null) {
            admin.setSystemWallet(this);
        }
        this.admin = admin;
    }

    public Wallet admin(Admin admin) {
        this.setAdmin(admin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wallet)) {
            return false;
        }
        return getId() != null && getId().equals(((Wallet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
