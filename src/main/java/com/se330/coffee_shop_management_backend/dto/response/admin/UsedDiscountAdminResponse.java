package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class UsedDiscountAdminResponse {
    // Mã định danh của phiếu giảm giá đã sử dụng
    private String discountId;
    // Mã định danh của chi tiết đơn hàng đã sử dụng phiếu giảm giá
    private String orderDetailId;
    // Số lần sử dụng phiếu giảm giá
    private int timeUsed;

    public static UsedDiscountAdminResponse convert(com.se330.coffee_shop_management_backend.entity.UsedDiscount usedDiscount) {
        return UsedDiscountAdminResponse.builder()
                .discountId(usedDiscount.getDiscount().getId().toString())
                .orderDetailId(usedDiscount.getOrderDetail().getId().toString())
                .timeUsed(usedDiscount.getTimesUse())
                .build();
    }

    public static java.util.List<UsedDiscountAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.UsedDiscount> usedDiscounts) {
        if (usedDiscounts == null || usedDiscounts.isEmpty()) {
            return java.util.List.of();
        }
        return usedDiscounts.stream()
                .map(UsedDiscountAdminResponse::convert)
                .toList();
    }
}
