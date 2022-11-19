package com.bullish.electronicstore.service;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;

import java.util.UUID;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO productRequestDTO);

    boolean delete(UUID code);
}
