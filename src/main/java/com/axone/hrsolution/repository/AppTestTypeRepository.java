package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppTestType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppTestType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppTestTypeRepository extends JpaRepository<AppTestType, Long> {}
