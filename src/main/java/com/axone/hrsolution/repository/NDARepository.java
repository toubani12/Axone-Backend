package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.NDA;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NDA entity.
 */
@Repository
public interface NDARepository extends JpaRepository<NDA, Long> {
    default Optional<NDA> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NDA> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NDA> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nDA from NDA nDA left join fetch nDA.employer left join fetch nDA.mediator left join fetch nDA.candidate",
        countQuery = "select count(nDA) from NDA nDA"
    )
    Page<NDA> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nDA from NDA nDA left join fetch nDA.employer left join fetch nDA.mediator left join fetch nDA.candidate")
    List<NDA> findAllWithToOneRelationships();

    @Query(
        "select nDA from NDA nDA left join fetch nDA.employer left join fetch nDA.mediator left join fetch nDA.candidate where nDA.id =:id"
    )
    Optional<NDA> findOneWithToOneRelationships(@Param("id") Long id);
}
