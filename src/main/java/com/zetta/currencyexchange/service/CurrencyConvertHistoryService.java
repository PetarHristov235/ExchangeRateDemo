package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.model.PageCurrencyConvertHistoryResponseDTO;

import java.time.OffsetDateTime;

public interface CurrencyConvertHistoryService {
    PageCurrencyConvertHistoryResponseDTO getConversionHistory(String transactionId,
                                                               OffsetDateTime transactionDateFrom,
                                                               OffsetDateTime transactionDateTo,
                                                               Integer pageCount,
                                                               Integer pageSize);

}
