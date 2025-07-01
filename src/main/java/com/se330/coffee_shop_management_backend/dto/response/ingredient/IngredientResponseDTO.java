package com.se330.coffee_shop_management_backend.dto.response.ingredient;

import com.se330.coffee_shop_management_backend.entity.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class IngredientResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String ingredientName;
    private String ingredientDescription;
    private BigDecimal ingredientPrice;
    private String ingredientType;
    private long shelfLifeDays;

    public static IngredientResponseDTO convert(Ingredient ingredient) {
        return IngredientResponseDTO.builder()
                .id(ingredient.getId().toString())
                .createdAt(ingredient.getCreatedAt())
                .updatedAt(ingredient.getUpdatedAt())
                .ingredientName(ingredient.getIngredientName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .ingredientPrice(ingredient.getIngredientPrice())
                .ingredientType(ingredient.getIngredientType())
                .shelfLifeDays(ingredient.getShelfLifeDays())
                .build();
    }

    public static List<IngredientResponseDTO> convert(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }

        return ingredients.stream()
                .map(IngredientResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
