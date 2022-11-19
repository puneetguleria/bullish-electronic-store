package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketItemResponseDTO;
import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.entity.ProductEntity;
import com.bullish.electronicstore.entity.ShoppingBasketEntity;
import com.bullish.electronicstore.entity.ShoppingBasketItemEntity;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.repository.ProductRepository;
import com.bullish.electronicstore.repository.ShoppingBasketRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bullish.electronicstore.type.DealType.BUY_ONE_GET_ONE;
import static com.bullish.electronicstore.type.DiscountType.FLAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ShoppingBasketServiceImplTest {

    @Mock
    private CodeGenerator<UUID> codeGenerator;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private DiscountDealRepository discountDealRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoppingBasketRepository shoppingBasketRepository;

    @InjectMocks
    private ShoppingBasketServiceImpl shoppingBasketService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should create a shopping basket")
    void shouldCreateBasket() {
        LocalDateTime localDateTime = LocalDateTime.now();
        UUID code = UUID.randomUUID();
        when(dateTimeService.now()).thenReturn(localDateTime);
        when(codeGenerator.generate()).thenReturn(code);

        ArgumentCaptor<ShoppingBasketEntity> basketEntityArgumentCaptor = ArgumentCaptor.forClass(ShoppingBasketEntity.class);
        var basketResponse = shoppingBasketService.createNewBasket();

        verify(shoppingBasketRepository, times(1)).save(basketEntityArgumentCaptor.capture());
        var basketEntity = basketEntityArgumentCaptor.getValue();

        assertAll(
                "Checking saved entity",
                () -> assertEquals(code, basketEntity.getCode()),
                () -> assertTrue(basketEntity.getItems().isEmpty()),
                () -> assertEquals(localDateTime, basketEntity.getLastModifiedAt())
        );

        assertAll(
                "Checking basket response",
                () -> assertEquals(code, basketResponse.getCode()),
                () -> assertTrue(basketResponse.getItems().isEmpty()),
                () -> assertEquals(0, basketResponse.getTotalPrice())
        );
    }

    @Test
    @DisplayName("Should return a basket")
    void shouldReturnABasket() {
        UUID basketCode = UUID.randomUUID();

        UUID productCode1 = UUID.randomUUID();
        double priceProduct1 = 10.34;

        UUID productCode2 = UUID.randomUUID();
        double priceProduct2 = 210.34;

        LocalDateTime dateTime = LocalDateTime.now();

        List<ShoppingBasketItemEntity> mockShoppingItems = getMockShoppingItems(productCode1, productCode2, dateTime);

        ShoppingBasketEntity mockShoppingBasketEntity = ShoppingBasketEntity.builder().code(basketCode).items(mockShoppingItems).lastModifiedAt(dateTime).build();

        when(shoppingBasketRepository.findOneByCode(basketCode)).thenReturn(Optional.of(mockShoppingBasketEntity));
        when(productRepository.findOneByCode(productCode1)).thenReturn(Optional.of(getMockProductEntity(productCode1, priceProduct1)));
        when(productRepository.findOneByCode(productCode2)).thenReturn(Optional.of(getMockProductEntity(productCode2, priceProduct2)));

        var basketResponseOptional = shoppingBasketService.getBasket(basketCode);

        var basketResponse = basketResponseOptional.get();

        assertAll(
                "Checking basket response",
                () -> assertEquals(basketCode, basketResponse.getCode()),
                () -> assertEquals(2, basketResponse.getItems().size()),
                () -> assertEquals(List.of(
                        getShoppingResponseItem(productCode1, priceProduct1, 0, null),
                        getShoppingResponseItem(productCode2, priceProduct2, 0, null)
                ), basketResponse.getItems()),
                () -> assertEquals(220.68, basketResponse.getTotalPrice())
        );

    }

    @Test
    @DisplayName("Should add items to basket")
    void shouldAddItemsToBasket() {
        UUID basketCode = UUID.randomUUID();

        UUID productCode = UUID.randomUUID();
        double priceProduct = 210.34;

        UUID discountDealCode = UUID.randomUUID();
        double discountRate = 50.20;

        LocalDateTime dateTime = LocalDateTime.now();

        ShoppingBasketEntity mockShoppingBasketEntity = ShoppingBasketEntity.builder().id(1l).code(basketCode).items(new ArrayList<>()).lastModifiedAt(dateTime).build();
        DiscountDealEntity mockDiscountEntity = getMockDiscountEntity(discountDealCode, productCode, FLAT, BUY_ONE_GET_ONE, discountRate);

        when(shoppingBasketRepository.findOneByCode(basketCode)).thenReturn(Optional.of(mockShoppingBasketEntity));
        when(productRepository.findOneByCode(productCode)).thenReturn(Optional.of(getMockProductEntity(productCode, priceProduct)));
        when(discountDealRepository.findOneByProductCode(productCode)).thenReturn(Optional.of(mockDiscountEntity));

        ArgumentCaptor<ShoppingBasketEntity> basketEntityArgumentCaptor = ArgumentCaptor.forClass(ShoppingBasketEntity.class);

        ShoppingBasketItemRequestDTO request = new ShoppingBasketItemRequestDTO(productCode, 2);
        var basketResponseOptional = shoppingBasketService.addProductToBasket(basketCode, request);

        verify(shoppingBasketRepository, times(1)).save(basketEntityArgumentCaptor.capture());

        var basketEntity = basketEntityArgumentCaptor.getValue();
        assertEquals(basketCode, basketEntity.getCode());

        var basketResponse = basketResponseOptional.get();

        assertAll(
                "Checking basket response",
                () -> assertEquals(basketCode, basketResponse.getCode()),
                () -> assertEquals(2, basketResponse.getItems().size()),
                () -> assertEquals(List.of(
                        getShoppingResponseItem(productCode, priceProduct, 0, null),
                        getShoppingResponseItem(productCode, priceProduct, discountRate, mockDiscountEntity.getCode())
                ), basketResponse.getItems()),
                () -> assertEquals(370.48, basketResponse.getTotalPrice())
        );
    }

    @Test
    @DisplayName("Should remove items from basket")
    void shouldRemoveItemsBasket() {
        UUID basketCode = UUID.randomUUID();

        UUID productCode1 = UUID.randomUUID();
        double priceProduct1 = 10.34;

        UUID productCode2 = UUID.randomUUID();

        LocalDateTime dateTime = LocalDateTime.now();

        ShoppingBasketEntity mockShoppingBasketEntity = ShoppingBasketEntity.builder().id(1l).code(basketCode).items(getMockShoppingItems(productCode1, productCode2, dateTime)).lastModifiedAt(dateTime).build();

        when(shoppingBasketRepository.findOneByCode(basketCode)).thenReturn(Optional.of(mockShoppingBasketEntity));
        when(productRepository.findOneByCode(productCode1)).thenReturn(Optional.of(getMockProductEntity(productCode1, priceProduct1)));

        ArgumentCaptor<ShoppingBasketEntity> basketEntityArgumentCaptor = ArgumentCaptor.forClass(ShoppingBasketEntity.class);

        ShoppingBasketItemRequestDTO request = new ShoppingBasketItemRequestDTO(productCode2, 1);
        var basketResponseOptional = shoppingBasketService.deleteProductsFromBasket(basketCode, request);

        verify(shoppingBasketRepository, times(1)).save(basketEntityArgumentCaptor.capture());

        var basketEntity = basketEntityArgumentCaptor.getValue();
        assertEquals(basketCode, basketEntity.getCode());

        var basketResponse = basketResponseOptional.get();

        assertAll(
                "Checking basket response",
                () -> assertEquals(basketCode, basketResponse.getCode()),
                () -> assertEquals(1, basketResponse.getItems().size()),
                () -> assertEquals(List.of(
                        getShoppingResponseItem(productCode1, priceProduct1, 0, null)
                ), basketResponse.getItems()),
                () -> assertEquals(10.34, basketResponse.getTotalPrice())
        );
    }

    private ShoppingBasketItemResponseDTO getShoppingResponseItem(UUID productCode, double actualPrice, double discount, UUID dealCode) {
        DiscountDealResponseDTO discountDealResponseDTO = null;
        if (discount > 0) {
            discountDealResponseDTO = DiscountDealResponseDTO.builder()
                    .code(dealCode)
                    .productCode(productCode)
                    .rate(discount)
                    .discountType(FLAT)
                    .dealType(BUY_ONE_GET_ONE)
                    .build();
        }
        return ShoppingBasketItemResponseDTO.builder()
                .name(productCode.toString())
                .actualPrice(actualPrice)
                .priceAfterDiscount(actualPrice - discount)
                .deal(discountDealResponseDTO)
                .productCode(productCode)
                .build();
    }

    private static ProductEntity getMockProductEntity(UUID productCode, double priceProduct) {
        return ProductEntity.builder().code(productCode).name(productCode.toString()).price(priceProduct).build();
    }

    private List<ShoppingBasketItemEntity> getMockShoppingItems(UUID productCode1, UUID productCode2, LocalDateTime dateTime) {
        ShoppingBasketEntity shoppingBasket = ShoppingBasketEntity.builder().build();
        return new ArrayList<>(List.of(
                ShoppingBasketItemEntity.builder().shoppingBasket(shoppingBasket).productCode(productCode1).lastModifiedAt(dateTime).build(),
                ShoppingBasketItemEntity.builder().shoppingBasket(shoppingBasket).productCode(productCode2).lastModifiedAt(dateTime).build()
        ));
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