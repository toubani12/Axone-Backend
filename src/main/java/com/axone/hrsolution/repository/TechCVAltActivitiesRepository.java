package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVAltActivities;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVAltActivities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVAltActivitiesRepository extends JpaRepository<TechCVAltActivities, Long> {}
