package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Candidate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Candidate entity.
 *
 * When extending this class, extend CandidateRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CandidateRepository extends CandidateRepositoryWithBagRelationships, JpaRepository<Candidate, Long> {
    default Optional<Candidate> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Candidate> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Candidate> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select candidate from Candidate candidate left join fetch candidate.techCV",
        countQuery = "select count(candidate) from Candidate candidate"
    )
    Page<Candidate> findAllWithToOneRelationships(Pageable pageable);

    @Query("select candidate from Candidate candidate left join fetch candidate.techCV")
    List<Candidate> findAllWithToOneRelationships();

    @Query("select candidate from Candidate candidate left join fetch candidate.techCV where candidate.id =:id")
    Optional<Candidate> findOneWithToOneRelationships(@Param("id") Long id);
}
