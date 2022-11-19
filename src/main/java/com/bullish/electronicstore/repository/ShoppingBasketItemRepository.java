package com.bullish.electronicstore.repository;

import com.bullish.electronicstore.entity.ShoppingBasketItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingBasketItemRepository extends JpaRepository<ShoppingBasketItemEntity, Long> {
}
