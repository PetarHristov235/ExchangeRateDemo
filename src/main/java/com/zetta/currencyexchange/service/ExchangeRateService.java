package com.zetta.currencyexchange.service;


import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;

public interface ExchangeRateService {
    ExchangeRateResponseDTO exchangeRates(String from, String to);
}
