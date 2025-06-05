package com.zetta.currencyexchange.validation;

import com.zetta.currencyexchange.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_400;
import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_401;

@Component
public class ValidateCurrencyConversionHistoryRequestImpl implements ValidateCurrencyConvertHistoryRequest {

    @Override
    public void validateRequest(String transactionId, OffsetDateTime transactionDateFrom,
                                OffsetDateTime transactionDateTo) {
        validateParametersPresence(transactionId, transactionDateFrom, transactionDateTo);
        validateTransactionIdFormat(transactionId);
        validateTransactionDateRange(transactionDateFrom, transactionDateTo);
    }

    private void validateParametersPresence(String transactionId, OffsetDateTime transactionDateFrom,
                                            OffsetDateTime transactionDateTo) {
        if ((transactionId == null || transactionId.isEmpty()) &&
                transactionDateFrom == null &&
                transactionDateTo == null) {
            throw new BadRequestException(CH_400.getDescription(), CH_400.name());
        }
    }

    private void validateTransactionIdFormat(String transactionId) {
        if (transactionId != null && !transactionId.isEmpty()) {
            try {
                UUID.fromString(transactionId);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(CH_400.getDescription(), CH_400.name());
            }
        }
    }

    private void validateTransactionDateRange(OffsetDateTime transactionDateFrom,
                                              OffsetDateTime transactionDateTo) {
        if (transactionDateFrom != null && transactionDateTo != null &&
                transactionDateFrom.isAfter(transactionDateTo)) {
            throw new BadRequestException(CH_401.getDescription(), CH_401.name());
        }
    }
}