package com.se330.coffee_shop_management_backend.dto.request.recipe;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class RecipeCreateRequestDTO {
    private int recipeQuantity;
    private String recipeUnit;
    private boolean recipeIsTopping;
    private UUID ingredientId;
    private UUID productVariantId;
}
