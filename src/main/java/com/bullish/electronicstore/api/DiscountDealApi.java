package com.bullish.electronicstore.api;

import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DiscountDealApi {

    @PostMapping(value = "/v1/discount-deals", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DiscountDealResponseDTO> save(@RequestBody DiscountDealRequestDTO discountDealRequestDTO);

}
