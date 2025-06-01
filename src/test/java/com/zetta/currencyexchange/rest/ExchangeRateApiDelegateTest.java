package com.zetta.currencyexchange.rest;

import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.rest.delegate.ExchangeRateApiDelegateImpl;
import com.zetta.currencyexchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateApiDelegateImplTest {

    @Mock
    ExchangeRateService exchangeRateService;

    @InjectMocks
    ExchangeRateApiDelegateImpl exchangeRateApiDelegate;

    @Test
    void exchangeRate_returnsResponseEntityWithDTO() {
        String from = "EUR";
        String to = "USD";
        ExchangeRateResponseDTO dto = new ExchangeRateResponseDTO();
        dto.setSource(from);
        dto.setTarget(to);
        dto.setTimestamp(123456L);
        dto.setExchangeRate(BigDecimal.TEN);

        when(exchangeRateService.exchangeRates(from, to)).thenReturn(dto);

        ResponseEntity<ExchangeRateResponseDTO> response = exchangeRateApiDelegate.exchangeRate(from, to);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);
    }
}