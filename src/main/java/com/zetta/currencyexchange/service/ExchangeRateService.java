package com.zetta.currencyexchange.service;


import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;

import java.math.BigDecimal;

public interface ExchangeRateService {
    ExchangeRateResponseDTO exchangeRates(String from, String to);
}
