package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;
import com.bullish.electronicstore.entity.ProductEntity;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.ProductRepository;
import com.bullish.electronicstore.service.DateTimeService;
import com.bullish.electronicstore.service.ProductService;
import com.bullish.electronicstore.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {
    private final CodeGenerator<UUID> codeGenerator;

    private final DateTimeService dateTimeService;

    private final Validator<ProductRequestDTO> validator;

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(final CodeGenerator<UUID> codeGenerator, final DateTimeService dateTimeService,
                              final Validator<ProductRequestDTO> validator, final ProductRepository productRepository) {
        this.codeGenerator = codeGenerator;
        this.dateTimeService = dateTimeService;
        this.validator = validator;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponseDTO create(final ProductRequestDTO productRequestDTO) {
        validator.validate(productRequestDTO).reportIfAny();
        return toProductResponse(
                productRepository.save(toProductEntity(productRequestDTO))
        );
    }

    @Override
    @Transactional
    public boolean delete(final UUID code) {
        var productOptional = productRepository.findOneByCode(code);
        if (productOptional.isPresent()) {
            productRepository.delete(productOptional.get());
            return true;
        }
        return false;
    }

    private ProductEntity toProductEntity(final ProductRequestDTO productRequestDTO) {
        return ProductEntity.builder()
                .code(codeGenerator.generate())
                .name(productRequestDTO.getName())
                .price(productRequestDTO.getPrice())
                .lastModifiedAt(dateTimeService.now())
                .build();
    }

    private ProductResponseDTO toProductResponse(final ProductEntity productEntity) {
        return ProductResponseDTO.builder()
                .code(productEntity.getCode())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .build();
    }
}
