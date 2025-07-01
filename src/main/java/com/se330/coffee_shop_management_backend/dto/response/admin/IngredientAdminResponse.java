package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Ingredient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class IngredientAdminResponse {
    // Mã cung nguyên liệu
    private String ingredientId;
    // Tên nguyên liệu
    private String ingredientName;
    // Mô tả nguyên liệu
    private String ingredientDescription;
    // Giá nguyên liệu
    private BigDecimal ingredientPrice;
    // Loại nguyên liệu (ví dụ: cà phê, sữa, đường, v.v.)
    private String ingredientType;
    // Thời gian bảo quản nguyên liệu (tính bằng ngày)
    private long shelfLifeDays;

    public static IngredientAdminResponse convert(Ingredient ingredient) {
        return IngredientAdminResponse.builder()
                .ingredientId(ingredient.getId().toString())
                .ingredientName(ingredient.getIngredientName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .ingredientPrice(ingredient.getIngredientPrice())
                .ingredientType(ingredient.getIngredientType())
                .shelfLifeDays(ingredient.getShelfLifeDays())
                .build();
    }

    public static List<IngredientAdminResponse> convert(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }

        return ingredients.stream()
                .map(IngredientAdminResponse::convert)
                .toList();
    }
}
