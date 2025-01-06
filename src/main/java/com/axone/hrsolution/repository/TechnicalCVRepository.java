package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechnicalCV;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechnicalCV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicalCVRepository extends JpaRepository<TechnicalCV, Long> {}
