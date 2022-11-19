package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.service.DateTimeService;
import com.bullish.electronicstore.service.DiscountDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DiscountDealServiceImpl implements DiscountDealService {

    private final DiscountDealRepository discountDealRepository;

    private final CodeGenerator<UUID> codeGenerator;

    private final DateTimeService dateTimeService;

    @Autowired
    public DiscountDealServiceImpl(final CodeGenerator<UUID> codeGenerator, final DateTimeService dateTimeService,
                                   final DiscountDealRepository discountDealRepository) {
        this.codeGenerator = codeGenerator;
        this.dateTimeService = dateTimeService;
        this.discountDealRepository = discountDealRepository;
    }

    @Override
    public DiscountDealResponseDTO create(DiscountDealRequestDTO discountDealRequestDTO) {
        return toDiscountResponseDTO(
                discountDealRepository.save(toDiscountEntity(discountDealRequestDTO))
        );
    }

    private DiscountDealResponseDTO toDiscountResponseDTO(DiscountDealEntity discountDealEntity) {
        return DiscountDealResponseDTO.builder()
                .code(discountDealEntity.getCode())
                .discountType(discountDealEntity.getDiscountType())
                .dealType(discountDealEntity.getDealType())
                .rate(discountDealEntity.getRate())
                .productCode(discountDealEntity.getProductCode())
                .build();
    }

    private DiscountDealEntity toDiscountEntity(DiscountDealRequestDTO discountDealRequestDTO) {
        return DiscountDealEntity.builder()
                .code(codeGenerator.generate())
                .discountType(discountDealRequestDTO.getDiscountType())
                .dealType(discountDealRequestDTO.getDealType())
                .rate(discountDealRequestDTO.getRate())
                .productCode(discountDealRequestDTO.getProductCode())
                .lastModifiedAt(dateTimeService.now())
                .build();
    }
}
