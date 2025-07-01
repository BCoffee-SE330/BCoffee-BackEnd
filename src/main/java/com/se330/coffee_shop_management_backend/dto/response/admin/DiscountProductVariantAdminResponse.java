package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Discount;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class DiscountProductVariantAdminResponse {
    // Mã của phiếu giảm giá, khóa ngoại tham chiếu đến Discount
    private String discountId;
    // Mã của biến thể sản phẩm, khóa ngoại tham chiếu đến ProductVariant
    private String variantId;

    public static DiscountProductVariantAdminResponse convert(Discount discount, ProductVariant variant) {
        return DiscountProductVariantAdminResponse.builder()
                .discountId(discount.getId().toString())
                .variantId(variant.getId().toString())
                .build();
    }

    public static List<DiscountProductVariantAdminResponse> convert(Discount discount) {
        if (discount.getProductVariants() == null || discount.getProductVariants().isEmpty()) {
            return List.of();
        }
        return discount.getProductVariants().stream()
                .map(variant -> convert(discount, variant))
                .toList();
    }
}