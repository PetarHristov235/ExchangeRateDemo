package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.mapper.ExchangeRateResponseMapper;
import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.util.UriCreateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ExchangeRateServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    ExchangeRateResponseMapper exchangeRateResponseMapper;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    private static final String API_URL = "http://apilayer.net/api";
    private static final String ACCESS_KEY = "f8d9cdca86979931d56c1b92a6edc586";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(exchangeRateService, "apiLayerUrl", API_URL);
        ReflectionTestUtils.setField(exchangeRateService, "accessKey", ACCESS_KEY);
    }

    @Test
    void exchangeRates_returnsMappedDTO_whenResponseIsOk() {
        String from = "EUR";
        String to = "USD";
        URI uri = URI.create("http://api/live?access_key=key&source=EUR&currencies=USD");
        ExchangeRateResponse response = new ExchangeRateResponse();
        ExchangeRateResponseDTO dto = new ExchangeRateResponseDTO();

        try (MockedStatic<UriCreateUtil> mockedStatic = Mockito.mockStatic(UriCreateUtil.class)) {
            mockedStatic.when(() -> UriCreateUtil.createExchangeUri(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(uri);

            when(restTemplate.getForEntity(uri, ExchangeRateResponse.class))
                    .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
            when(exchangeRateResponseMapper.toDto(response, from, to)).thenReturn(dto);

            ExchangeRateResponseDTO result = exchangeRateService.exchangeRates(from, to);

            assertThat(result).isEqualTo(dto);
        }
    }

    @Test
    void exchangeRates_logsError_whenResponseBodyContainsError() {
        String from = "EUR";
        String to = "USD";
        URI uri = URI.create("http://api/live?access_key=key&source=EUR&currencies=USD");

        ExchangeRateResponse response = mock(ExchangeRateResponse.class);
        when(response.isSuccess()).thenReturn(false);
        ExchangeRateResponse.Error error = mock(ExchangeRateResponse.Error.class);
        when(response.getError()).thenReturn(error);
        when(error.getCode()).thenReturn(123);
        when(error.getInfo()).thenReturn("Invalid request");

        try (MockedStatic<UriCreateUtil> mockedStatic = Mockito.mockStatic(UriCreateUtil.class)) {
            mockedStatic.when(() -> UriCreateUtil.createExchangeUri(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(uri);

            when(restTemplate.getForEntity(uri, ExchangeRateResponse.class))
                    .thenReturn(ResponseEntity.ok(response));

            assertThatThrownBy(() -> exchangeRateService.exchangeRates(from, to))
                    .isInstanceOf(InternalServerErrorException.class);
        }
    }

    @Test
    void exchangeRates_throwsExchangeRateException_onRestClientException() {
        String from = "EUR";
        String to = "USD";
        URI uri = URI.create("http://api/live?access_key=key&source=EUR&currencies=USD");

        try (MockedStatic<UriCreateUtil> mockedStatic = mockStatic(UriCreateUtil.class)) {
            mockedStatic.when(() -> UriCreateUtil.createExchangeUri(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(uri);

            when(restTemplate.getForEntity(uri, ExchangeRateResponse.class))
                    .thenThrow(new RestClientException("Connection error"));

            assertThatThrownBy(() -> exchangeRateService.exchangeRates(from, to))
                    .isInstanceOf(InternalServerErrorException.class)
                    .hasMessage(RestApiErrorEnum.ER_500.getDescription());
        }
    }

    @Test
    void exchangeRates_throwsExchangeRateException_whenResponseBodyNotSuccess() {
        String from = "EUR";
        String to = "USD";
        URI uri = URI.create("http://api/live?access_key=key&source=EUR&currencies=USD");

        ExchangeRateResponse response = mock(ExchangeRateResponse.class);
        when(response.isSuccess()).thenReturn(false);
        ExchangeRateResponse.Error error = mock(ExchangeRateResponse.Error.class);
        when(response.getError()).thenReturn(error);

        try (MockedStatic<UriCreateUtil> mockedStatic = mockStatic(UriCreateUtil.class)) {
            mockedStatic.when(() -> UriCreateUtil.createExchangeUri(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(uri);

            when(restTemplate.getForEntity(uri, ExchangeRateResponse.class))
                    .thenReturn(ResponseEntity.ok(response));

            assertThatThrownBy(() -> exchangeRateService.exchangeRates(from, to))
                    .isInstanceOf(InternalServerErrorException.class)
                    .hasMessage(RestApiErrorEnum.IE_500.getDescription());
        }
    }
}