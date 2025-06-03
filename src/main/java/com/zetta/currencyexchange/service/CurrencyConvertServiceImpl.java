package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.db.repository.CurrencyConvertRepository;
import com.zetta.currencyexchange.mapper.CurrencyConvertMapper;
import com.zetta.currencyexchange.model.CurrencyConversionRateDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CurrencyConvertServiceImpl implements CurrencyConvertService {

    ExchangeRateService exchangeRateService;
    CurrencyConvertRepository currencyConvertRepository;
    CurrencyConvertMapper currencyConvertMapper;

    @Override
    public CurrencyConversionRateDTO convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal conversionAmount = Optional.ofNullable(amount).orElse(BigDecimal.ONE);
        BigDecimal exchangeRate =
                Optional.ofNullable(exchangeRateService.exchangeRates(fromCurrency, toCurrency).getExchangeRate())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Exchange rate not found for %s to %s", fromCurrency, toCurrency)));

        BigDecimal convertedAmount = exchangeRate.multiply(conversionAmount);

        CurrencyConvertEntity entity = buildCurrencyConvertEntity(fromCurrency, toCurrency, exchangeRate,
                convertedAmount);
        CurrencyConvertEntity savedEntity = currencyConvertRepository.save(entity);

        return currencyConvertMapper.toDto(savedEntity);
    }

    private CurrencyConvertEntity buildCurrencyConvertEntity(String fromCurrency, String toCurrency, BigDecimal conversionRate, BigDecimal convertedAmount) {
        return CurrencyConvertEntity.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .conversionRate(conversionRate)
                .convertedAmount(convertedAmount)
                .build();
    }
}