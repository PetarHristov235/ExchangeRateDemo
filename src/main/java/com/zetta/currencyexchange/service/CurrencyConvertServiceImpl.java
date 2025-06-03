package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.db.repository.CurrencyConvertRepository;
import com.zetta.currencyexchange.mapper.CurrencyConvertMapper;
import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
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
    public CurrencyConvertResponseDTO convert(CurrencyConvertRequestDTO requestDTO) {
        BigDecimal conversionAmount =
                Optional.ofNullable(requestDTO.getAmount()).orElse(BigDecimal.ONE);
        BigDecimal exchangeRate =
                Optional.ofNullable(exchangeRateService.exchangeRates(requestDTO.getFromCurrency(),
                                requestDTO.getToCurrency()).getExchangeRate())
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Exchange rate not found for %s to %s",
                                        requestDTO.getFromCurrency(), requestDTO.getToCurrency())));

        BigDecimal convertedAmount = exchangeRate.multiply(conversionAmount);

        CurrencyConvertEntity entity = buildCurrencyConvertEntity(requestDTO.getFromCurrency(), requestDTO.getToCurrency(), exchangeRate,
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