package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.service.DateTimeService;
import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.bullish.electronicstore.type.DealType.BUY_ONE_GET_ONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class DiscountDealServiceImplTest {

    @Mock
    private CodeGenerator<UUID> codeGenerator;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private DiscountDealRepository discountDealRepository;

    @InjectMocks
    private DiscountDealServiceImpl discountService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("should create new discount deal")
    void shouldCreateNewDiscountDeal() {
        var code = UUID.randomUUID();
        var productCode = UUID.randomUUID();
        var dateTimeNow = LocalDateTime.now();
        var rate = 10.234;

        when(codeGenerator.generate()).thenReturn(code);
        when(dateTimeService.now()).thenReturn(dateTimeNow);
        when(discountDealRepository.save(any(DiscountDealEntity.class)))
                .thenReturn(getMockDiscountEntity(code, productCode, DiscountType.PERCENTAGE, BUY_ONE_GET_ONE, rate, dateTimeNow));

        ArgumentCaptor<DiscountDealEntity> discountEntityArgumentCaptor = ArgumentCaptor.forClass(DiscountDealEntity.class);

        DiscountDealResponseDTO responseDTO = discountService.create(getMockDiscount(productCode, DiscountType.PERCENTAGE, BUY_ONE_GET_ONE, rate));

        verify(discountDealRepository, times(1)).save(discountEntityArgumentCaptor.capture());

        var savedEntity = discountEntityArgumentCaptor.getValue();

        assertAll(
                "Checking saved entity",
                () -> assertEquals(productCode, savedEntity.getProductCode()),
                () -> assertEquals(DiscountType.PERCENTAGE, savedEntity.getDiscountType()),
                () -> assertEquals(BUY_ONE_GET_ONE, savedEntity.getDealType()),
                () -> assertEquals(rate, savedEntity.getRate()),
                () -> assertEquals(code, savedEntity.getCode()),
                () -> assertEquals(dateTimeNow, savedEntity.getLastModifiedAt())
        );

        assertAll(
                "Checking response object",
                () -> assertEquals(productCode, responseDTO.getProductCode()),
                () -> assertEquals(DiscountType.PERCENTAGE, responseDTO.getDiscountType()),
                () -> assertEquals(BUY_ONE_GET_ONE, responseDTO.getDealType()),
                () -> assertEquals(rate, responseDTO.getRate()),
                () -> assertEquals(code, responseDTO.getCode())
        );

    }

    private DiscountDealRequestDTO getMockDiscount(UUID productCode, DiscountType discountType, DealType dealType, double rate) {
        return DiscountDealRequestDTO.builder()
                .productCode(productCode)
                .rate(rate)
                .discountType(discountType)
                .dealType(dealType)
                .build();
    }

    private DiscountDealEntity getMockDiscountEntity(UUID code, UUID productCode, DiscountType discountType, DealType dealType, double rate, LocalDateTime modifiedAt) {
        return DiscountDealEntity.builder()
                .code(code)
                .productCode(productCode)
                .rate(rate)
                .discountType(discountType)
                .dealType(dealType)
                .lastModifiedAt(modifiedAt)
                .build();
    }

}