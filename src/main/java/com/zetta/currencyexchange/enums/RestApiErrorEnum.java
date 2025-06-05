package com.zetta.currencyexchange.enums;


import lombok.Getter;

@Getter
public enum RestApiErrorEnum {
    ER_500("""
            An internal error occurred during fetching the exchange rate.
            Please try again shortly.
            If the issue persists, please contact the support team."""),
    ER_124("An internal error occurred. Please contact the support team."),
    ER_204("The request was valid, but the query returned no data."),
    ER_312("Invalid Source Currency provided."),
    ER_313("Invalid one or more currency codes provided."),
    CH_400("The request for currency conversion has missing query parameters. Please check " +
            "the request parameters and try again."),
    CH_401("Transaction date from cannot be after transaction date to. " +
            "Please check the request parameters and try again."),
    GE_400("Missing required parameter: %s."),
    GE_423("Malformed JSON request."),
    GE_424("Parameter %s should be of type %s."),
    GE_509("An internal error occurred."),
    GE_405("HTTP method not supported: %s."),
    GE_415("Media type not supported: %s."),
    GE_500("An unexpected error occurred: %s.");

    private final String description;

    RestApiErrorEnum(String description) {
        this.description = description;
    }

}