package com.bullish.electronicstore.repository;

import com.bullish.electronicstore.entity.DiscountDealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiscountDealRepository extends JpaRepository<DiscountDealEntity, Long> {
    Optional<DiscountDealEntity> findOneByProductCode(UUID productCode);
}
