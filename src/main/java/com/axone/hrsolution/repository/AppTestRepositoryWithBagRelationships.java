package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppTest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AppTestRepositoryWithBagRelationships {
    Optional<AppTest> fetchBagRelationships(Optional<AppTest> appTest);

    List<AppTest> fetchBagRelationships(List<AppTest> appTests);

    Page<AppTest> fetchBagRelationships(Page<AppTest> appTests);
}
