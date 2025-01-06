package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Interview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Interview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {}
