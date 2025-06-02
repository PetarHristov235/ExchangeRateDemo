package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.mapper.ExchangeRateResponseMapper;
import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.util.UriCreateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
class ExchangeRateServiceImplCacheTest {

    @TestConfiguration
    static class CacheTestConfig {
        @Bean
        public CacheManager cacheManager() {
            return new CaffeineCacheManager();
        }
    }

    @Autowired
    @InjectMocks
    ExchangeRateServiceImpl exchangeRateService;

    @MockitoBean
    RestTemplate restTemplate;

    @MockitoBean
    ExchangeRateResponseMapper exchangeRateResponseMapper;

    @Autowired
    CacheManager cacheManager;

    private static final String API_URL = "https://apilayer.net/api";
    private static final String ACCESS_KEY = "f8d9cdca86979931d56c1b92a6edc586";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(exchangeRateService, "apiLayerUrl", API_URL);
        ReflectionTestUtils.setField(exchangeRateService, "accessKey", ACCESS_KEY);

        cacheManager.getCacheNames().forEach(name -> {
            if (cacheManager.getCache(name) != null) {
                cacheManager.getCache(name).clear();
            }
        });
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

    @Test
    void exchangeRates_shouldUseCache() {
        String from = "EUR";
        String to = "USD";
        ExchangeRateResponse response = new ExchangeRateResponse();
        ExchangeRateResponseDTO dto = new ExchangeRateResponseDTO();

        when(restTemplate.getForEntity(any(), eq(ExchangeRateResponse.class)))
                .thenReturn(ResponseEntity.ok(response));
        when(exchangeRateResponseMapper.toDto(response, from, to)).thenReturn(dto);

        // First call: should invoke dependencies
        ExchangeRateResponseDTO result1 = exchangeRateService.exchangeRates(from, to);
        // Second call: should hit cache, not invoke dependencies again
        ExchangeRateResponseDTO result2 = exchangeRateService.exchangeRates(from, to);

        assertThat(result1).isSameAs(result2);
        verify(restTemplate, times(1)).getForEntity(any(), eq(ExchangeRateResponse.class));
        verify(exchangeRateResponseMapper, times(1)).toDto(response, from, to);
    }
}
