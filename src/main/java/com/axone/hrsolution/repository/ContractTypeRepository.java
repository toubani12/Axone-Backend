package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.ContractType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContractType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {}
