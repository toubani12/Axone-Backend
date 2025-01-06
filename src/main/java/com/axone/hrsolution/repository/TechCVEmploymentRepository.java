package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVEmployment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVEmployment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVEmploymentRepository extends JpaRepository<TechCVEmployment, Long> {}
