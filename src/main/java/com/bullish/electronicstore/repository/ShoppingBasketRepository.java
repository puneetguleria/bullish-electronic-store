package com.bullish.electronicstore.repository;

import com.bullish.electronicstore.entity.ShoppingBasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasketEntity, Long> {
    Optional<ShoppingBasketEntity> findOneByCode(UUID basketCode);
}
