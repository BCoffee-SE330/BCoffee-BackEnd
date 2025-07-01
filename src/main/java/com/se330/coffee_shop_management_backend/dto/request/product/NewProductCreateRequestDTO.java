package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewProductCreateRequestDTO {
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private UUID productCategory;
    private boolean isDrink;
    private Map<UUID, Integer> productIngredients;
}
