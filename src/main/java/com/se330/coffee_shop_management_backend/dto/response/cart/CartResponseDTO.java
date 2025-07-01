package com.se330.coffee_shop_management_backend.dto.response.cart;

import com.se330.coffee_shop_management_backend.entity.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;
    private BigDecimal cartTotalCost;
    private BigDecimal cartDiscountCost;
    private BigDecimal cartTotalCostAfterDiscount;

    public static CartResponseDTO convert(Cart cart) {
        return CartResponseDTO.builder()
                .id(cart.getId().toString())
                .cartTotalCost(cart.getCartTotalCost())
                .cartDiscountCost(cart.getCartDiscountCost())
                .cartTotalCostAfterDiscount(cart.getCartTotalCostAfterDiscount())
                .build();
    }

    public static List<CartResponseDTO> convert(List<Cart> carts) {
        if (carts == null || carts.isEmpty()) {
            return List.of();
        }

        return carts.stream()
                .map(CartResponseDTO::convert)
                .toList();
    }
}
