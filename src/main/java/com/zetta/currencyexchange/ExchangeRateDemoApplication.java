package com.zetta.currencyexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"com.zetta.currencyexchange", "com.zetta.currencyexchange.api", "com.zetta.currencyexchange.model"})
public class ExchangeRateDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateDemoApplication.class, args);
    }

}
