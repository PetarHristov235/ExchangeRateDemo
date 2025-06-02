package com.zetta.currencyexchange.enums;


import lombok.Getter;

@Getter
public enum RestApiErrorEnum {
    ER_500(511, """
            An internal error occurred during fetching the exchange rate.
            Please try again shortly.
            If the issue persists, please contact the support team."""),
    IE_124(124, "An internal error occurred. Please contact the support team."),
    IE_500(2342, """
            An internal error occurred""");

    private final int code;
    private final String description;

    RestApiErrorEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

}