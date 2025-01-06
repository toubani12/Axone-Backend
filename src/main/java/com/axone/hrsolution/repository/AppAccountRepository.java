package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.AppAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppAccount entity.
 *
 * When extending this class, extend AppAccountRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AppAccountRepository extends AppAccountRepositoryWithBagRelationships, JpaRepository<AppAccount, Long> {
    default Optional<AppAccount> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<AppAccount> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<AppAccount> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select appAccount from AppAccount appAccount left join fetch appAccount.relatedUser",
        countQuery = "select count(appAccount) from AppAccount appAccount"
    )
    Page<AppAccount> findAllWithToOneRelationships(Pageable pageable);

    @Query("select appAccount from AppAccount appAccount left join fetch appAccount.relatedUser")
    List<AppAccount> findAllWithToOneRelationships();

    @Query("select appAccount from AppAccount appAccount left join fetch appAccount.relatedUser where appAccount.id =:id")
    Optional<AppAccount> findOneWithToOneRelationships(@Param("id") Long id);
}
