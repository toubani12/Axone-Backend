package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.CustomQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomQuestionRepository extends JpaRepository<CustomQuestion, Long> {}
