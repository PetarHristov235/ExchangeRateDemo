package com.zetta.currencyexchange.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@UtilityClass
public class UriCreateUtil {

    public URI createExchangeUri(String apiLayerUrl, String accessKey, String from, String to) {
        log.info("About to create Exchange uri for the external request");

        URI exchangeURI =
                UriComponentsBuilder.fromUriString(apiLayerUrl).queryParam(
                        "access_key", accessKey).queryParam("source",
                        from).queryParam(
                        "currencies", to).build().toUri();

        log.debug("Exchange uri created {}", exchangeURI);
        return exchangeURI;
    }
}
