package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.model.CurrencyConversionRateDTO;

import java.math.BigDecimal;

public interface CurrencyConvertService {
    CurrencyConversionRateDTO convert(String fromCurrency, String toCurrency, BigDecimal amount);
}
