package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppAccountType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppAccountType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppAccountTypeRepository extends JpaRepository<AppAccountType, Long> {}
