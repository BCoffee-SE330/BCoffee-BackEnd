package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderDetailAdminResponse {
    // Khóa ngoại tham chiếu đến bảng Order
    private String orderId;
    // Khóa ngoại tham chiếu đến bảng ProductVariant
    private String productVariantId;

    // Số lượng sản phẩm trong đơn hàng
    private int orderDetailQuantity;
    // Đơn giá của biến thể sản phẩm trong đơn hàng
    private BigDecimal orderDetailUnitPrice;
    // Đơn giá của biến thể sản phẩm sau khi áp dụng giảm giá
    private BigDecimal orderDetailUnitPriceAfterDiscount;
    // Chi phí giảm giá của biến thể sản phẩm trong đơn hàng
    private BigDecimal orderDetailDiscountCost;

    public static OrderDetailAdminResponse convert (OrderDetail orderDetail) {
        return OrderDetailAdminResponse.builder()
                .orderId(orderDetail.getOrder().getId().toString())
                .productVariantId(orderDetail.getProductVariant().getId().toString())
                .orderDetailQuantity(orderDetail.getOrderDetailQuantity())
                .orderDetailUnitPrice(orderDetail.getOrderDetailUnitPrice())
                .orderDetailUnitPriceAfterDiscount(orderDetail.getOrderDetailUnitPriceAfterDiscount())
                .orderDetailDiscountCost(orderDetail.getOrderDetailDiscountCost())
                .build();
    }

    public static List<OrderDetailAdminResponse> convert (List<OrderDetail> orderDetails) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return List.of();
        }

        return orderDetails.stream()
                .map(OrderDetailAdminResponse::convert)
                .toList();
    }
}
