package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.db.repository.CurrencyConvertRepository;
import com.zetta.currencyexchange.mapper.CurrencyConvertMapper;
import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CurrencyConvertServiceImplTest {

    private ExchangeRateService exchangeRateService;
    private CurrencyConvertRepository currencyConvertRepository;
    private CurrencyConvertMapper currencyConvertMapper;
    private CurrencyConvertServiceImpl currencyConvertService;


    @BeforeEach
    void setUp() {
        exchangeRateService = mock(ExchangeRateService.class);
        currencyConvertRepository = mock(CurrencyConvertRepository.class);
        currencyConvertMapper = mock(CurrencyConvertMapper.class);
        currencyConvertService = new CurrencyConvertServiceImpl(
                exchangeRateService, currencyConvertRepository, currencyConvertMapper
        );
    }

    @Test
    void convert_shouldReturnResponseDTO_whenValidRequestProvided() {
        CurrencyConvertRequestDTO requestDTO = new CurrencyConvertRequestDTO();
        requestDTO.setFromCurrency("USD");
        requestDTO.setToCurrency("EUR");
        requestDTO.setAmount(new BigDecimal("100"));

        BigDecimal exchangeRate = new BigDecimal("0.85");
        ExchangeRateResponseDTO rateDTO = new ExchangeRateResponseDTO();
        rateDTO.setExchangeRate(exchangeRate);

        CurrencyConvertEntity savedEntity = CurrencyConvertEntity.builder()
                .fromCurrency("USD")
                .toCurrency("EUR")
                .conversionRate(exchangeRate)
                .convertedAmount(new BigDecimal("85.00"))
                .build();

        CurrencyConvertResponseDTO expectedResponse = new CurrencyConvertResponseDTO();
        expectedResponse.setAmount(new BigDecimal("85.00"));

        when(exchangeRateService.exchangeRates("USD", "EUR")).thenReturn(rateDTO);
        when(currencyConvertRepository.save(any(CurrencyConvertEntity.class))).thenReturn(savedEntity);
        when(currencyConvertMapper.toDto(savedEntity)).thenReturn(expectedResponse);

        CurrencyConvertResponseDTO actualResponse = currencyConvertService.convert(requestDTO);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getAmount()).isEqualTo(new BigDecimal("85.00"));

        verify(exchangeRateService).exchangeRates("USD", "EUR");
        verify(currencyConvertRepository).save(any(CurrencyConvertEntity.class));
        verify(currencyConvertMapper).toDto(savedEntity);
    }

    @Test
    void convert_shouldUseDefaultAmountWhenNull() {
        CurrencyConvertRequestDTO requestDTO = new CurrencyConvertRequestDTO();
        requestDTO.setFromCurrency("USD");
        requestDTO.setToCurrency("EUR");
        requestDTO.setAmount(null);

        BigDecimal exchangeRate = new BigDecimal("0.9");
        ExchangeRateResponseDTO rateDTO = new ExchangeRateResponseDTO();
        rateDTO.setExchangeRate(exchangeRate);

        CurrencyConvertEntity savedEntity = CurrencyConvertEntity.builder()
                .fromCurrency("USD")
                .toCurrency("EUR")
                .conversionRate(exchangeRate)
                .convertedAmount(new BigDecimal("0.9"))
                .build();

        CurrencyConvertResponseDTO expectedResponse = new CurrencyConvertResponseDTO();
        expectedResponse.setAmount(new BigDecimal("0.9"));

        when(exchangeRateService.exchangeRates("USD", "EUR")).thenReturn(rateDTO);
        when(currencyConvertRepository.save(any())).thenReturn(savedEntity);
        when(currencyConvertMapper.toDto(savedEntity)).thenReturn(expectedResponse);

        CurrencyConvertResponseDTO result = currencyConvertService.convert(requestDTO);

        assertThat(result.getAmount()).isEqualTo(new BigDecimal("0.9"));
    }

    @Test
    void convert_shouldThrowException_whenExchangeRateIsNull() {
        CurrencyConvertRequestDTO requestDTO = new CurrencyConvertRequestDTO();
        requestDTO.setFromCurrency("USD");
        requestDTO.setToCurrency("JPY");
        requestDTO.setAmount(new BigDecimal("50"));

        ExchangeRateResponseDTO rateDTO = new ExchangeRateResponseDTO();

        when(exchangeRateService.exchangeRates("USD", "JPY")).thenReturn(rateDTO);

        assertThatThrownBy(() -> currencyConvertService.convert(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exchange rate not found for USD to JPY");

        verify(currencyConvertRepository, never()).save(any());
        verify(currencyConvertMapper, never()).toDto(any());
    }
}