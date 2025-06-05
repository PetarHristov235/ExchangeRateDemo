package com.zetta.currencyexchange.service;

import com.zetta.currencyexchange.db.entity.CurrencyConvertEntity;
import com.zetta.currencyexchange.db.repository.CurrencyConvertRepository;
import com.zetta.currencyexchange.mapper.CurrencyConvertMapper;
import com.zetta.currencyexchange.model.CurrencyConvertResponseDTO;
import com.zetta.currencyexchange.model.PageCurrencyConvertHistoryResponseDTO;
import com.zetta.currencyexchange.validation.ValidateCurrencyConvertHistoryRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyConvertHistoryServiceImpl implements CurrencyConvertHistoryService {
    CurrencyConvertMapper currencyConvertMapper;
    CurrencyConvertRepository currencyConvertRepository;
    ValidateCurrencyConvertHistoryRequest validateCurrencyConvertHistoryRequest;

    @Override
    public PageCurrencyConvertHistoryResponseDTO getConversionHistory(String transactionId,
                                                                      OffsetDateTime transactionDateFrom, OffsetDateTime transactionDateTo,
                                                                      Integer pageCount, Integer pageSize) {
        validateCurrencyConvertHistoryRequest.validateRequest(transactionId, transactionDateFrom, transactionDateTo);
        Pageable pageable = PageRequest.of(pageCount != null ? pageCount : 0, pageSize != null ? pageSize : 10);
        UUID transactionUUID = (transactionId != null && !transactionId.isEmpty()) ? UUID.fromString(transactionId) : null;

        Page<CurrencyConvertEntity> currencyConvertHistoryPage =
                currencyConvertRepository.findCurrencyConvertHistory(transactionUUID, transactionDateFrom, transactionDateTo, pageable);

        return wrapFromPageToDTO(currencyConvertHistoryPage);
    }

    private PageCurrencyConvertHistoryResponseDTO wrapFromPageToDTO(Page<CurrencyConvertEntity> currencyConvertHistoryPage) {
        PageCurrencyConvertHistoryResponseDTO pageCurrencyConvertHistoryResponseDTO = new PageCurrencyConvertHistoryResponseDTO();
        List<CurrencyConvertResponseDTO> responseDTOList = currencyConvertHistoryPage.getContent().stream().map(currencyConvertMapper::toDto).toList();
        pageCurrencyConvertHistoryResponseDTO.setContent(responseDTOList);
        pageCurrencyConvertHistoryResponseDTO.setPageCount(currencyConvertHistoryPage.getNumber());
        pageCurrencyConvertHistoryResponseDTO.setPageSize(currencyConvertHistoryPage.getSize());
        pageCurrencyConvertHistoryResponseDTO.setTotalElements(currencyConvertHistoryPage.getTotalElements());
        pageCurrencyConvertHistoryResponseDTO.setTotalPages(currencyConvertHistoryPage.getTotalPages());
        return pageCurrencyConvertHistoryResponseDTO;
    }
}
