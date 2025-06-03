package com.zetta.currencyexchange.db.repository;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

import java.util.UUID;

@Repository
public interface CurrencyConvertRepository extends JpaRepository<CurrencyConvertEntity, UUID> {
    @Query("""
            SELECT c FROM CurrencyConvertEntity c
            WHERE (:transactionId IS NULL or c.id = :transactionId)
              AND (:transactionDateFrom IS NULL or c.createdAt >= :transactionDateFrom)
              AND (:transactionDateTo IS NULL or c.createdAt <= :transactionDateTo)""")
    Page<CurrencyConvertEntity> findCurrencyConvertHistory(@Param("transactionId") UUID transactionId,
                                                           @Param("transactionDateFrom") OffsetDateTime transactionDateFrom,
                                                           @Param("transactionDateTo") OffsetDateTime transactionDateTo,
                                                           Pageable pageable);

}
