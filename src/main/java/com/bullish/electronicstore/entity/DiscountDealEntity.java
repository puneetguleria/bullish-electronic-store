package com.bullish.electronicstore.entity;

import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="discount_Deal")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DiscountDealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID code;

    private UUID productCode;

    private DiscountType discountType;

    private DealType dealType;

    private double rate;

    private LocalDateTime lastModifiedAt;

}
