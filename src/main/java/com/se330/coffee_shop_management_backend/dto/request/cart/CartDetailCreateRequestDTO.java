package com.se330.coffee_shop_management_backend.dto.request.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartDetailCreateRequestDTO {
    private UUID variantId;
    private int cartDetailQuantity;
}
