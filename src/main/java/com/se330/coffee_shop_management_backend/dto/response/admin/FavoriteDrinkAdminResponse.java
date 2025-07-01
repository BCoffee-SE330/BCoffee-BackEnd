package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.FavoriteDrink;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class FavoriteDrinkAdminResponse {
    // Mã của người dùng yêu thích đồ uống
    private String userId;
    // Mã của sản phẩm đồ uống được yêu thích
    private String productId;

    public static FavoriteDrinkAdminResponse convert(FavoriteDrink favoriteDrink) {
        return FavoriteDrinkAdminResponse.builder()
                .userId(favoriteDrink.getUser().getId().toString())
                .productId(favoriteDrink.getProduct().getId().toString())
                .build();
    }

    public static List<FavoriteDrinkAdminResponse> convert(List<FavoriteDrink> favoriteDrinks) {
        if (favoriteDrinks == null || favoriteDrinks.isEmpty()) {
            return List.of();
        }
        return favoriteDrinks.stream()
                .map(FavoriteDrinkAdminResponse::convert)
                .toList();
    }
}
