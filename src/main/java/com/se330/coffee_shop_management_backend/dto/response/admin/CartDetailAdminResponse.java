package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.CartDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartDetailAdminResponse {
    // Mã của chi tiết giỏ hàng
    private String cartDetailId;
    // Số lượng sản phẩm (biến thể) trong chi tiết giỏ hàng
    private int cartDetailQuantity;
    // Giá đơn vị của sản phẩm (biến thể )
    private BigDecimal cartDetailUnitPrice;
    // Giá trị giảm giá cho sản phẩm (biến thể ) này
    private BigDecimal cartDetailDiscountCost;
    // Giá đơn vị sau khi giảm giá
    private BigDecimal cartDetailUnitPriceAfterDiscount;
    // Mã của giỏ hàng, khóa ngoại tham chiếu đến bảng giỏ hàng
    private String cartId;
    // Mã của biến thể sản phẩm, khóa ngoại tham chiếu đến bảng biến thể sản phẩm
    private String varId;

    public static CartDetailAdminResponse convert(CartDetail cartDetail) {
        return CartDetailAdminResponse.builder()
                .cartDetailId(cartDetail.getId().toString())
                .cartDetailQuantity(cartDetail.getCartDetailQuantity())
                .cartDetailUnitPrice(cartDetail.getCartDetailUnitPrice())
                .cartDetailDiscountCost(cartDetail.getCartDetailDiscountCost())
                .cartDetailUnitPriceAfterDiscount(cartDetail.getCartDetailUnitPriceAfterDiscount())
                .cartId(cartDetail.getCart() != null ? cartDetail.getCart().getId().toString() : null)
                .varId(cartDetail.getProductVariant() != null ? cartDetail.getProductVariant().getId().toString() : null)
                .build();
    }

    public static List<CartDetailAdminResponse> convert(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return List.of();
        }

        return cartDetails.stream()
                .map(CartDetailAdminResponse::convert)
                .toList();
    }
}