package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Employer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employer entity.
 */
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    default Optional<Employer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Employer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Employer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select employer from Employer employer left join fetch employer.relatedUser left join fetch employer.wallet",
        countQuery = "select count(employer) from Employer employer"
    )
    Page<Employer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select employer from Employer employer left join fetch employer.relatedUser left join fetch employer.wallet")
    List<Employer> findAllWithToOneRelationships();

    @Query(
        "select employer from Employer employer left join fetch employer.relatedUser left join fetch employer.wallet where employer.id =:id"
    )
    Optional<Employer> findOneWithToOneRelationships(@Param("id") Long id);
}
