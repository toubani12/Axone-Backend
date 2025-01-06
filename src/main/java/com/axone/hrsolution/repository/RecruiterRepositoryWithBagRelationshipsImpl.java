package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Recruiter;
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
public class RecruiterRepositoryWithBagRelationshipsImpl implements RecruiterRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RECRUITERS_PARAMETER = "recruiters";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Recruiter> fetchBagRelationships(Optional<Recruiter> recruiter) {
        return recruiter.map(this::fetchApplications).map(this::fetchOperationalDomains);
    }

    @Override
    public Page<Recruiter> fetchBagRelationships(Page<Recruiter> recruiters) {
        return new PageImpl<>(fetchBagRelationships(recruiters.getContent()), recruiters.getPageable(), recruiters.getTotalElements());
    }

    @Override
    public List<Recruiter> fetchBagRelationships(List<Recruiter> recruiters) {
        return Optional.of(recruiters).map(this::fetchApplications).map(this::fetchOperationalDomains).orElse(Collections.emptyList());
    }

    Recruiter fetchApplications(Recruiter result) {
        return entityManager
            .createQuery(
                "select recruiter from Recruiter recruiter left join fetch recruiter.applications where recruiter.id = :id",
                Recruiter.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Recruiter> fetchApplications(List<Recruiter> recruiters) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, recruiters.size()).forEach(index -> order.put(recruiters.get(index).getId(), index));
        List<Recruiter> result = entityManager
            .createQuery(
                "select recruiter from Recruiter recruiter left join fetch recruiter.applications where recruiter in :recruiters",
                Recruiter.class
            )
            .setParameter(RECRUITERS_PARAMETER, recruiters)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Recruiter fetchOperationalDomains(Recruiter result) {
        return entityManager
            .createQuery(
                "select recruiter from Recruiter recruiter left join fetch recruiter.operationalDomains where recruiter.id = :id",
                Recruiter.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Recruiter> fetchOperationalDomains(List<Recruiter> recruiters) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, recruiters.size()).forEach(index -> order.put(recruiters.get(index).getId(), index));
        List<Recruiter> result = entityManager
            .createQuery(
                "select recruiter from Recruiter recruiter left join fetch recruiter.operationalDomains where recruiter in :recruiters",
                Recruiter.class
            )
            .setParameter(RECRUITERS_PARAMETER, recruiters)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
