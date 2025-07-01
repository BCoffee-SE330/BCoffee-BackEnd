package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartAdminResponse {
    // Mã của giỏ hàng
    private String cartId;
    // Tổng giá trị giỏ hàng trước khi giảm giá (đã bao gồm thuế)
    private BigDecimal cartTotalCost;
    // Giá trị giảm giá
    private BigDecimal cartDiscountCost;
    // Tổng giá trị sau khi giảm giá (đã bao gồm thuế)
    private BigDecimal cartTotalCostAfterDiscount;
    // Mã của người mua hàng, khóa ngoại tham chiếu đến bảng người dùng
    private String userId;

    public static CartAdminResponse convert(Cart cart) {
        return CartAdminResponse.builder()
                .cartId(cart.getId().toString())
                .cartTotalCost(cart.getCartTotalCost())
                .cartDiscountCost(cart.getCartDiscountCost())
                .cartTotalCostAfterDiscount(cart.getCartTotalCostAfterDiscount())
                .userId(cart.getUser() != null ? cart.getUser().getId().toString() : null)
                .build();
    }

    public static List<CartAdminResponse> convert(List<Cart> carts) {
        if (carts == null || carts.isEmpty()) {
            return List.of();
        }

        return carts.stream()
                .map(CartAdminResponse::convert)
                .toList();
    }
}