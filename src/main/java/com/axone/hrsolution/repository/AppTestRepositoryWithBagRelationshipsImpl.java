package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppTest;
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
public class AppTestRepositoryWithBagRelationshipsImpl implements AppTestRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String APPTESTS_PARAMETER = "appTests";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AppTest> fetchBagRelationships(Optional<AppTest> appTest) {
        return appTest.map(this::fetchTypes);
    }

    @Override
    public Page<AppTest> fetchBagRelationships(Page<AppTest> appTests) {
        return new PageImpl<>(fetchBagRelationships(appTests.getContent()), appTests.getPageable(), appTests.getTotalElements());
    }

    @Override
    public List<AppTest> fetchBagRelationships(List<AppTest> appTests) {
        return Optional.of(appTests).map(this::fetchTypes).orElse(Collections.emptyList());
    }

    AppTest fetchTypes(AppTest result) {
        return entityManager
            .createQuery("select appTest from AppTest appTest left join fetch appTest.types where appTest.id = :id", AppTest.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AppTest> fetchTypes(List<AppTest> appTests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, appTests.size()).forEach(index -> order.put(appTests.get(index).getId(), index));
        List<AppTest> result = entityManager
            .createQuery("select appTest from AppTest appTest left join fetch appTest.types where appTest in :appTests", AppTest.class)
            .setParameter(APPTESTS_PARAMETER, appTests)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
