package com.zetta.currencyexchange.rest.delegate;

import com.zetta.currencyexchange.api.CurrencyConversionApiDelegate;
import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.service.CurrencyConvertService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyConvertApiDelegateImpl implements CurrencyConversionApiDelegate {

    CurrencyConvertService currencyConvertService;

    @Override
    public ResponseEntity<CurrencyConvertResponseDTO> currencyConvert(CurrencyConvertRequestDTO requestDTO) {
        return ResponseEntity.ok(currencyConvertService.convert(requestDTO));
    }
}
