package com.zetta.currencyexchange.mapper;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyConvertMapperImplTest {

    private final CurrencyConvertMapper mapper = Mappers.getMapper(CurrencyConvertMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        CurrencyConvertEntity entity = new CurrencyConvertEntity();
        entity.setId(id);
        entity.setFromCurrency("USD");
        entity.setToCurrency("EUR");
        entity.setConvertedAmount(BigDecimal.valueOf(123.45));
        entity.setCreatedAt(OffsetDateTime.now());

        CurrencyConvertResponseDTO dto = mapper.toDto(entity);

        assertEquals("USD", dto.getSource());
        assertEquals("EUR", dto.getTarget());
        assertEquals(BigDecimal.valueOf(123.45), dto.getAmount());
        assertEquals(id.toString(), dto.getTransactionId());
        assertEquals(entity.getCreatedAt(), dto.getTimestamp());
    }

    @Test
    void uuidToString_shouldReturnNullForNullInput() {
        assertNull(CurrencyConvertMapper.uuidToString(null));
    }
}