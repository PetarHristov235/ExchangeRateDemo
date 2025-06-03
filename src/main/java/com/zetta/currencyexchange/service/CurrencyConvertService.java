package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.model.PageCurrencyConvertHistoryResponseDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface CurrencyConvertService {
    CurrencyConvertResponseDTO convert(CurrencyConvertRequestDTO requestDTO);
}
