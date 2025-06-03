package com.zetta.currencyexchange.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency_conversion")
@Builder
public class CurrencyConvertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "source_currency")
    private String fromCurrency;

    @Column(name = "target_currency")
    private String toCurrency;

    @Column(name = "conversion_rate")
    private BigDecimal conversionRate;

    @Column(name = "converted_amount", nullable = false)
    private BigDecimal convertedAmount;

    @CreationTimestamp
    @TimeZoneStorage(value = TimeZoneStorageType.NORMALIZE_UTC)
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

//    @PrePersist
//    public void onCreate() {
//        createdAt = OffsetDateTime.now();
//    }
}