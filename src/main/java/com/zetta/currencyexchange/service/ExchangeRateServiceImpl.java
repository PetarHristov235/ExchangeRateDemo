package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.mapper.ExchangeRateResponseMapper;
import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.util.ApiErrorStrategyContext;
import com.zetta.currencyexchange.util.UriCreateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_500;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExchangeRateServiceImpl implements ExchangeRateService {
    RestTemplate restTemplate;
    ExchangeRateResponseMapper exchangeRateResponseMapper;

    @Value("${apiLayer.url}")
    @NonFinal
    String apiLayerUrl;

    @Value("${apiLayer.access-key}")
    @NonFinal
    String accessKey;

    static String EXCHANGE_RATE_ENDPOINT_SUFFIX = "/live";

    @Override
    @Cacheable(value = "exchangeRates", key = "#from.concat('-').concat(#to)", unless = "#result == null")
    public ExchangeRateResponseDTO exchangeRates(String from, String to) {
        String exchangeRateUrl = apiLayerUrl.concat(EXCHANGE_RATE_ENDPOINT_SUFFIX);
        log.info("Starting exchange rate operation: from='{}', to='{}', uri='{}'",
                from, to, exchangeRateUrl);

        URI exchangeUri = UriCreateUtil.createExchangeUri(exchangeRateUrl, accessKey, from, to);

        ResponseEntity<ExchangeRateResponse> exchangeRateResponseEntity;
        try {
            exchangeRateResponseEntity = restTemplate.getForEntity(exchangeUri, ExchangeRateResponse.class);
            log.info("Starting exchange rate operation fetched data successfully");
            log.debug("exchangeResponseEntity fetched: {}", exchangeRateResponseEntity);
        } catch (RestClientException ex) {
            log.error("An RestClientException was thrown during fetching the exchange rates with:" +
                    "{}", ex.getMessage());

            throw new InternalServerErrorException(ER_500.getDescription(), ER_500.name());
        }

        if (exchangeRateResponseEntity.getBody() != null &&
                exchangeRateResponseEntity.getBody().getError() != null &&
                !exchangeRateResponseEntity.getBody().isSuccess()) {
            ExchangeRateResponse.Error error = exchangeRateResponseEntity.getBody().getError();
            log.error("An error occurred when fetching the exchange rates with error code {} and description {}",
                    error.getCode(), error.getInfo());
            ApiErrorStrategyContext.handleError(error.getCode(), exchangeUri.toString());
        }

        return exchangeRateResponseMapper.toDto(exchangeRateResponseEntity.getBody(), from, to);
    }
}