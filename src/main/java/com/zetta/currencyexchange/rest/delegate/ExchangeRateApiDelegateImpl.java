package com.zetta.currencyexchange.rest.delegate;

import com.zetta.currencyexchange.api.ExchangeRateApiDelegate;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.service.ExchangeRateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExchangeRateApiDelegateImpl implements ExchangeRateApiDelegate {

    ExchangeRateService exchangeRateService;

    public ResponseEntity<ExchangeRateResponseDTO> exchangeRate(String from, String to) {
        return ResponseEntity.ok(exchangeRateService.exchangeRates(from, to));
    }
}