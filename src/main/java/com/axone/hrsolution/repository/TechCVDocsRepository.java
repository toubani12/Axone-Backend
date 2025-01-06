package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.TechCVDocs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TechCVDocs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechCVDocsRepository extends JpaRepository<TechCVDocs, Long> {}
