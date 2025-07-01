package com.se330.coffee_shop_management_backend.dto.request.recipe;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RecipeUpdateRequestDTO {
    private UUID recipeId;
    private int recipeQuantity;
    private String recipeUnit;
    private boolean recipeIsTopping;
    private UUID ingredientId;
    private UUID productVariantId;
}
