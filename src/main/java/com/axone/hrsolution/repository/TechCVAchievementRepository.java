package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVAchievement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVAchievement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVAchievementRepository extends JpaRepository<TechCVAchievement, Long> {}
