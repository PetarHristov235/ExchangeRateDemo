package com.zetta.currencyexchange.validation;

import com.zetta.currencyexchange.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_400;

@Component
public class ValidateCurrencyConversionHistoryRequestImpl implements ValidateCurrencyConvertHistoryRequest {

    @Override
    public void validateRequest(String transactionId, OffsetDateTime transactionDateFrom,
                                OffsetDateTime transactionDateTo) {

        boolean allParamsEmpty = (transactionId == null || transactionId.isEmpty())
                && transactionDateFrom == null
                && transactionDateTo == null;

        boolean invalidTransactionId = transactionId != null && !transactionId.isEmpty() && isInvalidUUID(transactionId);

        if (allParamsEmpty || invalidTransactionId) {
            throw new BadRequestException(CH_400.getDescription(), CH_400.getCode());
        }
    }

    public boolean isInvalidUUID(String id) {
        try {
            UUID.fromString(id);
            return false;
        } catch (IllegalArgumentException | NullPointerException e) {
            return true;
        }
    }
}