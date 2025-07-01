package com.se330.coffee_shop_management_backend.dto.response.recipe;


import com.se330.coffee_shop_management_backend.entity.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class RecipeResponseDTO  {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of product creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of product update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private int recipeQuantity;
    private String recipeUnit;
    private boolean recipeIsTopping;
    private String ingredientId;
    private String productVariantId;

    public static RecipeResponseDTO convert(Recipe recipe) {
        return RecipeResponseDTO.builder()
                .id(recipe.getId().toString())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .recipeQuantity(recipe.getRecipeQuantity())
                .recipeUnit(recipe.getRecipeUnit())
                .recipeIsTopping(recipe.isRecipeIsTopping())
                .ingredientId(recipe.getIngredient().getId() != null ? recipe.getIngredient().getId().toString() : null)
                .productVariantId(recipe.getProductVariant().getId() != null ? recipe.getProductVariant().getId().toString() : null)
                .build();
    }

    public static List<RecipeResponseDTO> convert(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            return List.of();
        }
        return recipes.stream()
                .map(RecipeResponseDTO::convert)
                .toList();
    }
}
