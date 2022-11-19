package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.api.ProductApi;
import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;
import com.bullish.electronicstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<ProductResponseDTO> save(final ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequestDTO));
    }

    @Override
    public ResponseEntity<Void> delete(final UUID productCode) {
        if (productService.delete(productCode)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found, may have been already deleted"
        );
    }
}
