package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.exception.ExchangeRateException;
import com.zetta.currencyexchange.mapper.ExchangeRateResponseMapper;
import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import com.zetta.currencyexchange.util.UriCreateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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
    private String accessKey;

    @Override
    public ExchangeRateResponseDTO exchangeRates(String from, String to) {
        log.info("Starting exchange rate operation");
        URI exchangeUri = UriCreateUtil.createExchangeUri(apiLayerUrl.concat("/live"), accessKey,
                from, to);
//        TODO Implement exception handling
        ResponseEntity<ExchangeRateResponse> exchangeRateResponseEntity = restTemplate.getForEntity(exchangeUri, ExchangeRateResponse.class);
        if (exchangeRateResponseEntity.getStatusCode().value() != 200) {
            throw new ExchangeRateException();
        }
        return exchangeRateResponseMapper.toDto(exchangeRateResponseEntity.getBody(), from, to);
    }
}
