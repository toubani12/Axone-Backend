package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Application;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApplicationRepositoryWithBagRelationships {
    Optional<Application> fetchBagRelationships(Optional<Application> application);

    List<Application> fetchBagRelationships(List<Application> applications);

    Page<Application> fetchBagRelationships(Page<Application> applications);
}
