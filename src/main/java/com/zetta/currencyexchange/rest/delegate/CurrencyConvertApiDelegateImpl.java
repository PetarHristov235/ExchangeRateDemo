package com.zetta.currencyexchange.rest.delegate;

import com.zetta.currencyexchange.api.CurrencyConversionApiDelegate;
import com.zetta.currencyexchange.model.CurrencyConversionRateDTO;
import com.zetta.currencyexchange.service.CurrencyConvertService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyConvertApiDelegateImpl implements CurrencyConversionApiDelegate {

    CurrencyConvertService currencyConvertService;

    @Override
    public ResponseEntity<CurrencyConversionRateDTO> currencyConvert(String fromCurrency,
                                                                     String toCurrency,
                                                                     BigDecimal amount) {
        return ResponseEntity.ok(currencyConvertService.convert(fromCurrency, toCurrency, amount));
    }
}
