package com.se330.coffee_shop_management_backend.dto.request.ingredient;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class IngredientUpdateRequestDTO {
    private UUID ingredientId;
    private String ingredientName;
    private String ingredientDescription;
    private BigDecimal ingredientPrice;
    private String ingredientType;
    private long shelfLifeDays;
}
