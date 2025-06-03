package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;

public interface CurrencyConvertService {
    CurrencyConvertResponseDTO convert(CurrencyConvertRequestDTO requestDTO);
}
