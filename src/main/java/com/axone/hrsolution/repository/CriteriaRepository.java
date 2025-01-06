package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Criteria;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Criteria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Long> {}
