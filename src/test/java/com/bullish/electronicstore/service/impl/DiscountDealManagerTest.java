package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static com.bullish.electronicstore.type.DealType.BUY_ONE_GET_ONE;
import static com.bullish.electronicstore.type.DealType.FLAT_OFF;
import static com.bullish.electronicstore.type.DiscountType.PERCENTAGE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DiscountDealManagerTest {

    @Mock
    private DiscountDealRepository dealRepository;

    private DiscountDealManager dealManager;

    @BeforeEach
    void setup() {
        openMocks(this);
        dealManager = new DiscountDealManager(dealRepository);
    }

    @Test
    @DisplayName("Should return discount alternatively when buy one get one type")
    void shouldReturnDiscountAlternativelyForBuyOneGetOne() {
        var code = UUID.randomUUID();
        var productCode = UUID.randomUUID();
        var rate = 10.234;
        DiscountDealEntity mockDiscountEntity = getMockDiscountEntity(code, productCode, PERCENTAGE, BUY_ONE_GET_ONE, rate);
        when(dealRepository.findOneByProductCode(productCode)).thenReturn(Optional.of(mockDiscountEntity));

        for (int i = 1; i < 5; i++) {
            if (i % 2 == 0) {
                assertTrue(dealManager.applyAndGetAvailableDeal(productCode).isPresent(), "Should apply buy one get one on second/forth/sixth product");
            } else {
                assertTrue(dealManager.applyAndGetAvailableDeal(productCode).isEmpty(), "Cannot apply buy one get one on first/third/fifth product");
            }
        }
    }

    @Test
    @DisplayName("Should always return discount when flat type")
    void shouldAlwaysReturnDiscountForFlatType() {
        var code = UUID.randomUUID();
        var productCode = UUID.randomUUID();
        var rate = 10.234;

        DiscountDealEntity mockDiscountEntity = getMockDiscountEntity(code, productCode, PERCENTAGE, FLAT_OFF, rate);
        when(dealRepository.findOneByProductCode(productCode)).thenReturn(Optional.of(mockDiscountEntity));

        for (int i = 1; i < 5; i++) {
            assertTrue(dealManager.applyAndGetAvailableDeal(productCode).isPresent());
        }
    }

    @Test
    @DisplayName("Should return no discount when no deal configured for product")
    void shouldReturnNoDiscountForProduct() {

        var productCode = UUID.randomUUID();
        when(dealRepository.findOneByProductCode(productCode)).thenReturn(Optional.empty());

        assertTrue(dealManager.applyAndGetAvailableDeal(productCode).isEmpty());

    }

    private DiscountDealEntity getMockDiscountEntity(UUID code, UUID productCode, DiscountType discountType, DealType dealType, double rate) {
        return DiscountDealEntity.builder()
                .code(code)
                .productCode(productCode)
                .rate(rate)
                .discountType(discountType)
                .dealType(dealType)
                .build();
    }
}