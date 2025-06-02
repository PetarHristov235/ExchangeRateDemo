package com.zetta.currencyexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(
        scanBasePackages = {"com.zetta.currencyexchange", "com.zetta.currencyexchange.api", "com.zetta.currencyexchange.model"})
@EnableCaching
public class ExchangeRateDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateDemoApplication.class, args);
    }

}
