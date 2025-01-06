package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Admin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Admin entity.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    default Optional<Admin> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Admin> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Admin> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select admin from Admin admin left join fetch admin.relatedUser left join fetch admin.systemWallet",
        countQuery = "select count(admin) from Admin admin"
    )
    Page<Admin> findAllWithToOneRelationships(Pageable pageable);

    @Query("select admin from Admin admin left join fetch admin.relatedUser left join fetch admin.systemWallet")
    List<Admin> findAllWithToOneRelationships();

    @Query("select admin from Admin admin left join fetch admin.relatedUser left join fetch admin.systemWallet where admin.id =:id")
    Optional<Admin> findOneWithToOneRelationships(@Param("id") Long id);
}
