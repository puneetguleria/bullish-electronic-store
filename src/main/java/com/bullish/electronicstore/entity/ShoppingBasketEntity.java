package com.bullish.electronicstore.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name="shopping_basket")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ShoppingBasketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shoppingBasket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingBasketItemEntity> items;

    private LocalDateTime lastModifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingBasketEntity that = (ShoppingBasketEntity) o;
        return id == that.id && code.equals(that.code) && items.equals(that.items) && lastModifiedAt.equals(that.lastModifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, items, lastModifiedAt);
    }
}
