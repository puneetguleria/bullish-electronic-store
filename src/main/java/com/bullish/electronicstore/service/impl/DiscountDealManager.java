package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.entity.DiscountDealEntity;
import com.bullish.electronicstore.repository.DiscountDealRepository;
import com.bullish.electronicstore.type.DealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.bullish.electronicstore.type.DealType.FLAT_OFF;
import static java.util.Objects.isNull;

class DiscountDealManager {
    @Getter
    @AllArgsConstructor
    @Setter
    private class DiscountDealAvailability {
        private DiscountDealEntity discountDeal;
        private boolean isAvailable;
    }

    private final DiscountDealRepository discountDealRepository;

    public DiscountDealManager(final DiscountDealRepository discountDealRepository) {
        this.discountDealRepository = discountDealRepository;
    }

    private final Map<UUID, DiscountDealAvailability> discountDealMap = new HashMap<>();

    public Optional<DiscountDealEntity> applyAndGetAvailableDeal(UUID productCode) {

        addDealToMapIfAbsent(productCode);

        Optional<DiscountDealAvailability> dealAvailabilityOptional = getDeal(productCode);
        if (dealAvailabilityOptional.isPresent()) {
            DiscountDealAvailability availability = dealAvailabilityOptional.get();
            DiscountDealEntity discountDeal = availability.getDiscountDeal();
            return apply(availability, discountDeal.getDealType())
                    ? Optional.of(discountDeal)
                    : Optional.empty();
        }
        return Optional.empty();
    }


    private static boolean apply(DiscountDealAvailability discountDealAvailability, DealType dealType) {
        boolean isEligible;
        switch (dealType) {
            case FLAT_OFF:
                isEligible = true;
                break;
            case BUY_ONE_GET_ONE:
                isEligible = discountDealAvailability.isAvailable();
                //Toggle availability for next time on same product
                discountDealAvailability.setAvailable(!discountDealAvailability.isAvailable());
                break;
            default:
                isEligible = false;

        }
        return isEligible;
    }

    private void addDealToMapIfAbsent(UUID productCode) {
        DiscountDealAvailability discountDealAvailability = this.discountDealMap.get(productCode);
        if (isNull(discountDealAvailability)) {
            Optional<DiscountDealEntity> discountDealOptional = discountDealRepository.findOneByProductCode(productCode);
            if (discountDealOptional.isPresent()) {
                discountDealMap.put(productCode, getDefaultDiscountDealAvailability(discountDealOptional.get()));
            }
        }
    }

    private Optional<DiscountDealAvailability> getDeal(UUID productCode) {
        DiscountDealAvailability discountDealAvailability = this.discountDealMap.get(productCode);
        return isNull(discountDealAvailability) ? Optional.empty() : Optional.of(discountDealAvailability);
    }

    private DiscountDealAvailability getDefaultDiscountDealAvailability(DiscountDealEntity discountDeal) {
        return new DiscountDealAvailability(discountDeal, discountDeal.getDealType() == FLAT_OFF);
    }

}
