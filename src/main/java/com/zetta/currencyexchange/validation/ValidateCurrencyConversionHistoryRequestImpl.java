package com.zetta.currencyexchange.validation;

import com.zetta.currencyexchange.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_400;

@Component
public class ValidateCurrencyConversionHistoryRequestImpl implements ValidateCurrencyConvertHistoryRequest {

    @Override
    public void validateRequest(String transactionId, OffsetDateTime transactionDateFrom,
                                OffsetDateTime transactionDateTo) {
        if ((transactionId == null || transactionId.isEmpty())
                && transactionDateFrom == null && transactionDateTo == null) {
            throw new BadRequestException(CH_400.getDescription(), CH_400.getCode());
        }
    }
}
