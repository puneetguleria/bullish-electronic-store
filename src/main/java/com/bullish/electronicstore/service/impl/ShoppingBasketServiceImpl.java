package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketItemResponseDTO;
import com.bullish.electronicstore.dto.ShoppingBasketResponseDTO;
import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.entity.ProductEntity;
import com.bullish.electronicstore.entity.ShoppingBasketEntity;
import com.bullish.electronicstore.entity.ShoppingBasketItemEntity;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.repository.ProductRepository;
import com.bullish.electronicstore.repository.ShoppingBasketRepository;
import com.bullish.electronicstore.service.DateTimeService;
import com.bullish.electronicstore.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShoppingBasketServiceImpl implements ShoppingBasketService {

    private final CodeGenerator<UUID> codeGenerator;

    private final DateTimeService dateTimeService;

    private final DiscountDealRepository discountDealRepository;

    private final ProductRepository productRepository;

    private final ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    public ShoppingBasketServiceImpl(final CodeGenerator<UUID> codeGenerator, final DateTimeService dateTimeService,
                                      final DiscountDealRepository discountDealRepository, final ProductRepository productRepository,
                                      final ShoppingBasketRepository shoppingBasketRepository) {
        this.codeGenerator = codeGenerator;
        this.dateTimeService = dateTimeService;
        this.discountDealRepository = discountDealRepository;
        this.productRepository = productRepository;
        this.shoppingBasketRepository = shoppingBasketRepository;
    }

    @Override
    @Transactional
    public ShoppingBasketResponseDTO createNewBasket() {
        UUID code = codeGenerator.generate();
        ShoppingBasketEntity basketEntity = ShoppingBasketEntity.builder()
                .code(code)
                .items(new ArrayList<>())
                .lastModifiedAt(dateTimeService.now())
                .build();
        shoppingBasketRepository.save(basketEntity);
        return toShoppingBasketResponseDTO(basketEntity);
    }

    @Override
    public Optional<ShoppingBasketResponseDTO> getBasket(final UUID basketCode) {
        Optional<ShoppingBasketEntity> basketEntityOptional = shoppingBasketRepository.findOneByCode(basketCode);
        if (basketEntityOptional.isPresent()) {
            return Optional.of(toShoppingBasketResponseDTO(basketEntityOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<ShoppingBasketResponseDTO> addProductToBasket(final UUID basketCode, final ShoppingBasketItemRequestDTO itemRequestDTO) {
        Optional<ShoppingBasketEntity> basket = shoppingBasketRepository.findOneByCode(basketCode);
        if (basket.isPresent()) {
            ShoppingBasketEntity basketEntity = basket.get();
            List<ShoppingBasketItemEntity> basketItemEntities = basketEntity.getItems();
            int items = 0;
            while (items < itemRequestDTO.getQuantity()) {
                basketItemEntities.add(
                        ShoppingBasketItemEntity.builder()
                                .shoppingBasket(basketEntity)
                                .productCode(itemRequestDTO.getProductCode())
                                .lastModifiedAt(dateTimeService.now())
                                .build()
                );
                items++;
            }
            shoppingBasketRepository.save(basketEntity);
            return Optional.of(toShoppingBasketResponseDTO(basketEntity));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<ShoppingBasketResponseDTO> deleteProductsFromBasket(final UUID basketCode, final ShoppingBasketItemRequestDTO itemRequestDTO) {
        Optional<ShoppingBasketEntity> basket = shoppingBasketRepository.findOneByCode(basketCode);
        if (basket.isPresent()) {
            ShoppingBasketEntity basketEntity = basket.get();
            List<ShoppingBasketItemEntity> basketItemEntities = basketEntity.getItems();
            int items = itemRequestDTO.getQuantity();
            while (items > 0) {
                Optional<ShoppingBasketItemEntity> match = basketItemEntities.stream()
                        .filter(item -> item.getProductCode().equals(itemRequestDTO.getProductCode()))
                        .findFirst();
                if (match.isPresent())
                    basketItemEntities.remove(match.get());
                items--;
            }
            shoppingBasketRepository.save(basketEntity);
            return Optional.of(toShoppingBasketResponseDTO(basketEntity));
        }
        return Optional.empty();
    }

    private ShoppingBasketResponseDTO toShoppingBasketResponseDTO(final ShoppingBasketEntity basketEntity) {
        List<ShoppingBasketItemResponseDTO> shoppingItems = new ArrayList<>();
        DiscountDealManager discountDealManager = new DiscountDealManager(this.discountDealRepository);
        double totalPrice = 0;
        for (ShoppingBasketItemEntity item : basketEntity.getItems()) {
            Optional<ProductEntity> productEntityOptional = productRepository.findOneByCode(item.getProductCode());
            if (productEntityOptional.isPresent()) {
                ProductEntity productEntity = productEntityOptional.get();
                ShoppingBasketItemResponseDTO basketItemResponseDTO = toShoppingItemResponseDTO(productEntity);
                this.applyDiscount(discountDealManager, basketItemResponseDTO);
                shoppingItems.add(basketItemResponseDTO);
                totalPrice += basketItemResponseDTO.getPriceAfterDiscount();
            } else {
                throw new InvalidInvocationException("Requested product doesn't exist");
            }
        }
        return ShoppingBasketResponseDTO.builder()
                .code(basketEntity.getCode())
                .items(shoppingItems)
                .totalPrice(totalPrice)
                .build();

    }

    private static ShoppingBasketItemResponseDTO toShoppingItemResponseDTO(final ProductEntity productEntity) {
        return ShoppingBasketItemResponseDTO.builder()
                .productCode(productEntity.getCode())
                .actualPrice(productEntity.getPrice())
                .priceAfterDiscount(productEntity.getPrice())
                .name(productEntity.getName())
                .build();
    }

    private void applyDiscount(final DiscountDealManager discountDealManager, final ShoppingBasketItemResponseDTO basketItemResponseDTO) {
        UUID productCode = basketItemResponseDTO.getProductCode();
        Optional<DiscountDealEntity> discountDealOptional = discountDealManager.applyAndGetAvailableDeal(productCode);
        if (discountDealOptional.isPresent()) {
            DiscountDealEntity discountDealEntity = discountDealOptional.get();
            double discountedPrice = discountDealEntity.getDiscountType()
                    .getDiscountedPrice(basketItemResponseDTO.getActualPrice(), discountDealEntity.getRate());
            basketItemResponseDTO.setDeal(
                    DiscountDealResponseDTO.builder()
                            .code(discountDealEntity.getCode())
                            .productCode(productCode)
                            .discountType(discountDealEntity.getDiscountType())
                            .dealType(discountDealEntity.getDealType())
                            .rate(discountDealEntity.getRate())
                            .build());
            basketItemResponseDTO.setPriceAfterDiscount(discountedPrice);
        }
    }
}

