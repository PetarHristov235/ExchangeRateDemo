package com.zetta.currencyexchange.mapper;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.model.CurrencyConversionRateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CurrencyConvertMapper {

    @Mapping(source = "fromCurrency", target = "source")
    @Mapping(source = "toCurrency", target = "target")
    @Mapping(source = "convertedAmount", target = "amount")
    @Mapping(source = "id", target = "transactionId", qualifiedByName = "uuidToString")
    @Mapping(source = "createdAt", target = "timestamp")
    CurrencyConversionRateDTO toDto(CurrencyConvertEntity entity);

    @Named("uuidToString")
    static String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}
