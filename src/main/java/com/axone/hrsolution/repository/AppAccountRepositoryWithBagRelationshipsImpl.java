package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AppAccountRepositoryWithBagRelationshipsImpl implements AppAccountRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String APPACCOUNTS_PARAMETER = "appAccounts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AppAccount> fetchBagRelationships(Optional<AppAccount> appAccount) {
        return appAccount.map(this::fetchTypes).map(this::fetchProviders);
    }

    @Override
    public Page<AppAccount> fetchBagRelationships(Page<AppAccount> appAccounts) {
        return new PageImpl<>(fetchBagRelationships(appAccounts.getContent()), appAccounts.getPageable(), appAccounts.getTotalElements());
    }

    @Override
    public List<AppAccount> fetchBagRelationships(List<AppAccount> appAccounts) {
        return Optional.of(appAccounts).map(this::fetchTypes).map(this::fetchProviders).orElse(Collections.emptyList());
    }

    AppAccount fetchTypes(AppAccount result) {
        return entityManager
            .createQuery(
                "select appAccount from AppAccount appAccount left join fetch appAccount.types where appAccount.id = :id",
                AppAccount.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AppAccount> fetchTypes(List<AppAccount> appAccounts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, appAccounts.size()).forEach(index -> order.put(appAccounts.get(index).getId(), index));
        List<AppAccount> result = entityManager
            .createQuery(
                "select appAccount from AppAccount appAccount left join fetch appAccount.types where appAccount in :appAccounts",
                AppAccount.class
            )
            .setParameter(APPACCOUNTS_PARAMETER, appAccounts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    AppAccount fetchProviders(AppAccount result) {
        return entityManager
            .createQuery(
                "select appAccount from AppAccount appAccount left join fetch appAccount.providers where appAccount.id = :id",
                AppAccount.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AppAccount> fetchProviders(List<AppAccount> appAccounts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, appAccounts.size()).forEach(index -> order.put(appAccounts.get(index).getId(), index));
        List<AppAccount> result = entityManager
            .createQuery(
                "select appAccount from AppAccount appAccount left join fetch appAccount.providers where appAccount in :appAccounts",
                AppAccount.class
            )
            .setParameter(APPACCOUNTS_PARAMETER, appAccounts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
