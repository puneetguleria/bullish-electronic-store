package com.bullish.electronicstore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name="shopping_basket_item")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ShoppingBasketItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShoppingBasketEntity shoppingBasket;

    private UUID productCode;

    private LocalDateTime lastModifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingBasketItemEntity that = (ShoppingBasketItemEntity) o;
        return id == that.id && shoppingBasket.equals(that.shoppingBasket) && productCode.equals(that.productCode) && lastModifiedAt.equals(that.lastModifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shoppingBasket, productCode, lastModifiedAt);
    }
}
