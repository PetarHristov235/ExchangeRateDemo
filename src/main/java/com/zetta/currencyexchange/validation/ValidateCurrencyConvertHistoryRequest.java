package com.zetta.currencyexchange.validation;

import java.time.OffsetDateTime;

public interface ValidateCurrencyConvertHistoryRequest {

    void validateRequest(String transactionId, OffsetDateTime transactionDateFrom,
                         OffsetDateTime transactionDateTo);
}
