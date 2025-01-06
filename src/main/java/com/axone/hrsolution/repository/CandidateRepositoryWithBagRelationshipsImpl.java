package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Candidate;
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
public class CandidateRepositoryWithBagRelationshipsImpl implements CandidateRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CANDIDATES_PARAMETER = "candidates";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Candidate> fetchBagRelationships(Optional<Candidate> candidate) {
        return candidate.map(this::fetchDomains).map(this::fetchApplications);
    }

    @Override
    public Page<Candidate> fetchBagRelationships(Page<Candidate> candidates) {
        return new PageImpl<>(fetchBagRelationships(candidates.getContent()), candidates.getPageable(), candidates.getTotalElements());
    }

    @Override
    public List<Candidate> fetchBagRelationships(List<Candidate> candidates) {
        return Optional.of(candidates).map(this::fetchDomains).map(this::fetchApplications).orElse(Collections.emptyList());
    }

    Candidate fetchDomains(Candidate result) {
        return entityManager
            .createQuery(
                "select candidate from Candidate candidate left join fetch candidate.domains where candidate.id = :id",
                Candidate.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Candidate> fetchDomains(List<Candidate> candidates) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, candidates.size()).forEach(index -> order.put(candidates.get(index).getId(), index));
        List<Candidate> result = entityManager
            .createQuery(
                "select candidate from Candidate candidate left join fetch candidate.domains where candidate in :candidates",
                Candidate.class
            )
            .setParameter(CANDIDATES_PARAMETER, candidates)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Candidate fetchApplications(Candidate result) {
        return entityManager
            .createQuery(
                "select candidate from Candidate candidate left join fetch candidate.applications where candidate.id = :id",
                Candidate.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Candidate> fetchApplications(List<Candidate> candidates) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, candidates.size()).forEach(index -> order.put(candidates.get(index).getId(), index));
        List<Candidate> result = entityManager
            .createQuery(
                "select candidate from Candidate candidate left join fetch candidate.applications where candidate in :candidates",
                Candidate.class
            )
            .setParameter(CANDIDATES_PARAMETER, candidates)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
