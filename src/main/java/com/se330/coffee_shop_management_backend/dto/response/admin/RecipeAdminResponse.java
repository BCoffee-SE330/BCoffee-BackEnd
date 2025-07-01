package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Recipe;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class RecipeAdminResponse {
    // Mã sản phẩm biến thể, khóa ngoại đến ProductVariant
    private String productVariantId;
    // Mã nguyên liệu, khóa ngoại đến Ingredient
    private String ingredientId;
    // Số lượng nguyên liệu trong công thức
    private int recipeQuantity;
    // Đơn vị của nguyên liệu trong công thức (gram hoặc ml)
    private String recipeUnit;
    // Biến thể công thức có phải là topping hay không
    private boolean recipeIsTopping;

    public static RecipeAdminResponse convert(Recipe recipe) {
        return RecipeAdminResponse.builder()
                .productVariantId(recipe.getProductVariant().getId().toString())
                .ingredientId(recipe.getIngredient().getId().toString())
                .recipeQuantity(recipe.getRecipeQuantity())
                .recipeUnit(recipe.getRecipeUnit())
                .recipeIsTopping(recipe.isRecipeIsTopping())
                .build();
    }

    public static List<RecipeAdminResponse> convert(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            return List.of();
        }
        return recipes.stream()
                .map(RecipeAdminResponse::convert)
                .toList();
    }
}
