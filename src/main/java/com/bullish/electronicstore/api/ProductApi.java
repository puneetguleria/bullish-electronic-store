package com.bullish.electronicstore.api;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface ProductApi {

    @PostMapping(value = "/v1/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponseDTO> save(@RequestBody ProductRequestDTO productRequestDTO);

    @DeleteMapping(value = "/v1/products/{product-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable("product-code") UUID productCode);

}
