package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Domain;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Domain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {}
