package com.se330.coffee_shop_management_backend.dto.response.cart;

import com.se330.coffee_shop_management_backend.entity.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartAndUsedDiscountResponseDTO {
    private String id;
    private BigDecimal cartTotalCost;
    private BigDecimal cartDiscountCost;
    private BigDecimal cartTotalCostAfterDiscount;
    private Set<UUID> usedDiscounts;

    public static CartAndUsedDiscountResponseDTO convert(Cart cart, List<UUID> usedDiscountIds) {
        CartAndUsedDiscountResponseDTO response = new CartAndUsedDiscountResponseDTO();
        response.id = cart.getId().toString();
        response.cartTotalCost = cart.getCartTotalCost();
        response.cartDiscountCost = cart.getCartDiscountCost();
        response.cartTotalCostAfterDiscount = cart.getCartTotalCostAfterDiscount();
        if (usedDiscountIds != null && !usedDiscountIds.isEmpty()) {
            response.usedDiscounts = Set.copyOf(usedDiscountIds);
        } else {
            response.usedDiscounts = Set.of();
        }
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartAndUsedDiscountResponseDTO that = (CartAndUsedDiscountResponseDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
