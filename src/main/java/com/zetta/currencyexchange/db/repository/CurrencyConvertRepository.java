package com.zetta.currencyexchange.db.repository;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyConvertRepository extends JpaRepository<CurrencyConvertEntity, UUID> {
}
