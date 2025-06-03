package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.db.repository.CurrencyConvertRepository;
import com.zetta.currencyexchange.mapper.CurrencyConvertMapper;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.model.PageCurrencyConvertHistoryResponseDTO;
import com.zetta.currencyexchange.validation.ValidateCurrencyConvertHistoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConvertHistoryServiceImplTest {

    private CurrencyConvertMapper mapper;
    private CurrencyConvertRepository repository;
    private ValidateCurrencyConvertHistoryRequest validator;
    private CurrencyConvertHistoryServiceImpl service;

    @BeforeEach
    void setUp() {
        mapper = mock(CurrencyConvertMapper.class);
        repository = mock(CurrencyConvertRepository.class);
        validator = mock(ValidateCurrencyConvertHistoryRequest.class);
        service = new CurrencyConvertHistoryServiceImpl(mapper, repository, validator);
    }

    @Test
    void getConversionHistory_shouldReturnMappedPage() {
        String transactionId = UUID.randomUUID().toString();
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();
        int pageCount = 1;
        int pageSize = 5;

        CurrencyConvertEntity entity = new CurrencyConvertEntity();
        CurrencyConvertResponseDTO dto = new CurrencyConvertResponseDTO();
        List<CurrencyConvertEntity> entities = List.of(entity);
        List<CurrencyConvertResponseDTO> dtos = List.of(dto);

        Page<CurrencyConvertEntity> page = new PageImpl<>(entities, PageRequest.of(pageCount,
                pageSize), 6);

        when(repository.findCurrencyConvertHistory(any(), any(), any(), any())).thenReturn(page);
        when(mapper.toDto(entity)).thenReturn(dto);

        PageCurrencyConvertHistoryResponseDTO result = service.getConversionHistory(transactionId, from, to, pageCount, pageSize);

        verify(validator).validateRequest(transactionId, from, to);
        verify(repository).findCurrencyConvertHistory(any(), eq(from), eq(to), any());
        verify(mapper).toDto(entity);

        assertEquals(dtos, result.getContent());
        assertEquals(pageCount, result.getPageCount());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(page.getTotalElements(), result.getTotalElements());
    }

    @Test
    void getConversionHistory_shouldHandleNullTransactionId() {
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();

        Page<CurrencyConvertEntity> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        when(repository.findCurrencyConvertHistory(isNull(), any(), any(), any())).thenReturn(page);

        PageCurrencyConvertHistoryResponseDTO result = service.getConversionHistory(null, from, to, null, null);

        verify(validator).validateRequest(null, from, to);
        verify(repository).findCurrencyConvertHistory(isNull(), eq(from), eq(to), any());

        assertNotNull(result);
        assertEquals(0, result.getPageCount());
        assertEquals(10, result.getPageSize());
        assertEquals(0L, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void getConversionHistory_shouldUseCustomPageCountAndPageSize() {
        String transactionId = UUID.randomUUID().toString();
        OffsetDateTime from = OffsetDateTime.now().minusDays(2);
        OffsetDateTime to = OffsetDateTime.now();
        int pageCount = 2;
        int pageSize = 15;

        Page<CurrencyConvertEntity> page = new PageImpl<>(List.of(), Pageable.ofSize(pageSize).withPage(pageCount), 0);
        when(repository.findCurrencyConvertHistory(any(), any(), any(), any())).thenReturn(page);

        service.getConversionHistory(transactionId, from, to, pageCount, pageSize);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findCurrencyConvertHistory(any(), any(), any(), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(pageCount, capturedPageable.getPageNumber());
        assertEquals(pageSize, capturedPageable.getPageSize());
    }

    @Test
    void getConversionHistory_shouldUseDefaultPageSizeWhenNull() {
        String transactionId = UUID.randomUUID().toString();
        OffsetDateTime from = OffsetDateTime.now().minusDays(2);
        OffsetDateTime to = OffsetDateTime.now();
        int pageCount = 3;

        Page<CurrencyConvertEntity> page = new PageImpl<>(List.of(), Pageable.ofSize(10).withPage(pageCount), 0);
        when(repository.findCurrencyConvertHistory(any(), any(), any(), any())).thenReturn(page);

        service.getConversionHistory(transactionId, from, to, pageCount, null);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findCurrencyConvertHistory(any(), any(), any(), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(pageCount, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
    }

    @Test
    void getConversionHistory_shouldUseDefaultPageCountWhenNull() {
        String transactionId = UUID.randomUUID().toString();
        OffsetDateTime from = OffsetDateTime.now().minusDays(2);
        OffsetDateTime to = OffsetDateTime.now();
        int pageSize = 7;

        Page<CurrencyConvertEntity> page = new PageImpl<>(List.of(), Pageable.ofSize(pageSize).withPage(0), 0);
        when(repository.findCurrencyConvertHistory(any(), any(), any(), any())).thenReturn(page);

        service.getConversionHistory(transactionId, from, to, null, pageSize);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findCurrencyConvertHistory(any(), any(), any(), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(pageSize, capturedPageable.getPageSize());
    }

    @Test
    void getConversionHistory_shouldUseDefaultsWhenBothNull() {
        String transactionId = UUID.randomUUID().toString();
        OffsetDateTime from = OffsetDateTime.now().minusDays(2);
        OffsetDateTime to = OffsetDateTime.now();

        Page<CurrencyConvertEntity> page = new PageImpl<>(List.of(), Pageable.ofSize(10).withPage(0), 0);
        when(repository.findCurrencyConvertHistory(any(), any(), any(), any())).thenReturn(page);

        service.getConversionHistory(transactionId, from, to, null, null);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findCurrencyConvertHistory(any(), any(), any(), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
    }
}