package com.zetta.currencyexchange.rest;

import com.zetta.currencyexchange.model.CurrencyConvertRequestDTO;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.model.PageCurrencyConvertHistoryResponseDTO;
import com.zetta.currencyexchange.rest.delegate.CurrencyConvertApiDelegateImpl;
import com.zetta.currencyexchange.service.CurrencyConvertHistoryService;
import com.zetta.currencyexchange.service.CurrencyConvertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CurrencyConvertApiDelegateImplTest {

    private CurrencyConvertService convertService;
    private CurrencyConvertHistoryService historyService;
    private CurrencyConvertApiDelegateImpl delegate;

    @BeforeEach
    void setUp() {
        convertService = mock(CurrencyConvertService.class);
        historyService = mock(CurrencyConvertHistoryService.class);
        delegate = new CurrencyConvertApiDelegateImpl(convertService, historyService);
    }

    @Test
    void currencyConvert_shouldReturnResponse() {
        CurrencyConvertRequestDTO requestDTO = new CurrencyConvertRequestDTO();
        CurrencyConvertResponseDTO responseDTO = new CurrencyConvertResponseDTO();

        when(convertService.convert(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<CurrencyConvertResponseDTO> response = delegate.currencyConvert(requestDTO);

        assertEquals(responseDTO, response.getBody());
        verify(convertService).convert(requestDTO);
    }

    @Test
    void currencyConvertHistory_shouldReturnResponse() {
        String transactionId = "test-id";
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();
        int pageCount = 0;
        int pageSize = 10;
        PageCurrencyConvertHistoryResponseDTO pageDTO = new PageCurrencyConvertHistoryResponseDTO();

        when(historyService.getConversionHistory(transactionId, from, to, pageCount, pageSize)).thenReturn(pageDTO);

        ResponseEntity<PageCurrencyConvertHistoryResponseDTO> response = delegate.currencyConvertHistory(
                transactionId, from, to, pageCount, pageSize);

        assertEquals(pageDTO, response.getBody());
        verify(historyService).getConversionHistory(transactionId, from, to, pageCount, pageSize);
    }
}