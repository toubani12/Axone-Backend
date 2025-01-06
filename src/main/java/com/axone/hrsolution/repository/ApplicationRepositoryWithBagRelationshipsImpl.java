package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Application;
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
public class ApplicationRepositoryWithBagRelationshipsImpl implements ApplicationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String APPLICATIONS_PARAMETER = "applications";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Application> fetchBagRelationships(Optional<Application> application) {
        return application.map(this::fetchContractTypes).map(this::fetchContractTemplates).map(this::fetchCriteria).map(this::fetchDomains);
    }

    @Override
    public Page<Application> fetchBagRelationships(Page<Application> applications) {
        return new PageImpl<>(
            fetchBagRelationships(applications.getContent()),
            applications.getPageable(),
            applications.getTotalElements()
        );
    }

    @Override
    public List<Application> fetchBagRelationships(List<Application> applications) {
        return Optional.of(applications)
            .map(this::fetchContractTypes)
            .map(this::fetchContractTemplates)
            .map(this::fetchCriteria)
            .map(this::fetchDomains)
            .orElse(Collections.emptyList());
    }

    Application fetchContractTypes(Application result) {
        return entityManager
            .createQuery(
                "select application from Application application left join fetch application.contractTypes where application.id = :id",
                Application.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Application> fetchContractTypes(List<Application> applications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applications.size()).forEach(index -> order.put(applications.get(index).getId(), index));
        List<Application> result = entityManager
            .createQuery(
                "select application from Application application left join fetch application.contractTypes where application in :applications",
                Application.class
            )
            .setParameter(APPLICATIONS_PARAMETER, applications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Application fetchContractTemplates(Application result) {
        return entityManager
            .createQuery(
                "select application from Application application left join fetch application.contractTemplates where application.id = :id",
                Application.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Application> fetchContractTemplates(List<Application> applications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applications.size()).forEach(index -> order.put(applications.get(index).getId(), index));
        List<Application> result = entityManager
            .createQuery(
                "select application from Application application left join fetch application.contractTemplates where application in :applications",
                Application.class
            )
            .setParameter(APPLICATIONS_PARAMETER, applications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Application fetchCriteria(Application result) {
        return entityManager
            .createQuery(
                "select application from Application application left join fetch application.criteria where application.id = :id",
                Application.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Application> fetchCriteria(List<Application> applications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applications.size()).forEach(index -> order.put(applications.get(index).getId(), index));
        List<Application> result = entityManager
            .createQuery(
                "select application from Application application left join fetch application.criteria where application in :applications",
                Application.class
            )
            .setParameter(APPLICATIONS_PARAMETER, applications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Application fetchDomains(Application result) {
        return entityManager
            .createQuery(
                "select application from Application application left join fetch application.domains where application.id = :id",
                Application.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Application> fetchDomains(List<Application> applications) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, applications.size()).forEach(index -> order.put(applications.get(index).getId(), index));
        List<Application> result = entityManager
            .createQuery(
                "select application from Application application left join fetch application.domains where application in :applications",
                Application.class
            )
            .setParameter(APPLICATIONS_PARAMETER, applications)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
