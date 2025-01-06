package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Recruiter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recruiter entity.
 *
 * When extending this class, extend RecruiterRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface RecruiterRepository extends RecruiterRepositoryWithBagRelationships, JpaRepository<Recruiter, Long> {
    default Optional<Recruiter> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Recruiter> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Recruiter> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select recruiter from Recruiter recruiter left join fetch recruiter.relatedUser left join fetch recruiter.wallet",
        countQuery = "select count(recruiter) from Recruiter recruiter"
    )
    Page<Recruiter> findAllWithToOneRelationships(Pageable pageable);

    @Query("select recruiter from Recruiter recruiter left join fetch recruiter.relatedUser left join fetch recruiter.wallet")
    List<Recruiter> findAllWithToOneRelationships();

    @Query(
        "select recruiter from Recruiter recruiter left join fetch recruiter.relatedUser left join fetch recruiter.wallet where recruiter.id =:id"
    )
    Optional<Recruiter> findOneWithToOneRelationships(@Param("id") Long id);
}
