package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVProjectRepository extends JpaRepository<TechCVProject, Long> {}
