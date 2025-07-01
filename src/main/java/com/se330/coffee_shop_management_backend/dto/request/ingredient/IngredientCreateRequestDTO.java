package com.se330.coffee_shop_management_backend.dto.request.ingredient;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class IngredientCreateRequestDTO {
    private String ingredientName;
    private String ingredientDescription;
    private BigDecimal ingredientPrice;
    private String ingredientType;
    private long shelfLifeDays;
}
