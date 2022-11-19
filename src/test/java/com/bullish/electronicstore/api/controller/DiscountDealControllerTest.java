package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.service.DiscountDealService;
import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static com.bullish.electronicstore.type.DealType.BUY_ONE_GET_ONE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.CREATED;

class DiscountDealControllerTest {
    @Mock
    private DiscountDealService discountDealService;

    @InjectMocks
    private DiscountDealController controller;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("should create new discount deal")
    void shouldCreateNewDiscountDeal() {
        var code = UUID.randomUUID();
        var productCode = UUID.randomUUID();
        var rate = 10.234;
        DiscountType percentageType = DiscountType.PERCENTAGE;
        DealType buyOneGetOne = DealType.BUY_ONE_GET_ONE;
        var discountDealRequestDTO = DiscountDealRequestDTO.builder()
                .productCode(productCode)
                .rate(rate)
                .discountType(percentageType)
                .dealType(buyOneGetOne)
                .build();

        var mockDealResponseDTO = DiscountDealResponseDTO.builder()
                .code(code)
                .productCode(productCode)
                .rate(rate)
                .discountType(percentageType)
                .dealType(buyOneGetOne)
                .build();

        when(discountDealService.create(discountDealRequestDTO)).thenReturn(mockDealResponseDTO);

        var response = controller.save(discountDealRequestDTO);

        assertEquals(CREATED, response.getStatusCode());

        var responseBody = response.getBody();

        assertAll(
                "Checking response body",
                () -> assertEquals(productCode, responseBody.getProductCode()),
                () -> assertEquals(DiscountType.PERCENTAGE, responseBody.getDiscountType()),
                () -> assertEquals(BUY_ONE_GET_ONE, responseBody.getDealType()),
                () -> assertEquals(rate, responseBody.getRate()),
                () -> assertEquals(code, responseBody.getCode())
        );
    }

}