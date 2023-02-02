package com.sneddsy.bank.repository;

import com.sneddsy.bank.domain.BankTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BankTransfer entity.
 */
@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer, Long> {
    default Optional<BankTransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BankTransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BankTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct bankTransfer from BankTransfer bankTransfer left join fetch bankTransfer.fromAccount left join fetch bankTransfer.toAccount",
        countQuery = "select count(distinct bankTransfer) from BankTransfer bankTransfer"
    )
    Page<BankTransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct bankTransfer from BankTransfer bankTransfer left join fetch bankTransfer.fromAccount left join fetch bankTransfer.toAccount"
    )
    List<BankTransfer> findAllWithToOneRelationships();

    @Query(
        "select bankTransfer from BankTransfer bankTransfer left join fetch bankTransfer.fromAccount left join fetch bankTransfer.toAccount where bankTransfer.id =:id"
    )
    Optional<BankTransfer> findOneWithToOneRelationships(@Param("id") Long id);
}
