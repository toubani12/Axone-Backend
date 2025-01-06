package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVHardSkills;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVHardSkills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVHardSkillsRepository extends JpaRepository<TechCVHardSkills, Long> {}
