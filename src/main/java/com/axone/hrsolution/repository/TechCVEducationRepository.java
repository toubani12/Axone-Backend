package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVEducation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVEducation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVEducationRepository extends JpaRepository<TechCVEducation, Long> {}
