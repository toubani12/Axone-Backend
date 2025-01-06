package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AppAccountRepositoryWithBagRelationships {
    Optional<AppAccount> fetchBagRelationships(Optional<AppAccount> appAccount);

    List<AppAccount> fetchBagRelationships(List<AppAccount> appAccounts);

    Page<AppAccount> fetchBagRelationships(Page<AppAccount> appAccounts);
}
