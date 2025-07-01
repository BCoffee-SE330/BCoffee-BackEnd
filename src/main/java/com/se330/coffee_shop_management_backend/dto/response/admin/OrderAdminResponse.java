package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderAdminResponse {
    // Mã của đơn hàng
    private String orderId;
    // Mã của người dùng đã đặt đơn hàng
    private String userId;
    // Trạng thái của đơn hàng, chỉ hỗ trợ PENDING("ĐANG CHỜ"),
    //        PROCESSING("ĐANG XỬ LÝ"),
    //        COMPLETED("HOÀN TẤT"),
    //        DELIVERING("ĐANG GIAO HÀNG"),
    //        DELIVERED("ĐÃ GIAO HÀNG"),
    //        CANCELLED("ĐÃ HỦY"),;
    private Constants.OrderStatusEnum orderStatus;
    // Tổng giá trị đơn hàng sau khi áp dụng giảm giá (đã bao gồm thuế)
    private BigDecimal orderTotalCostAfterDiscount;
    // Tổng giá trị giảm giá của đơn hàng
    private BigDecimal orderDiscountCost;
    // Tổng giá trị đơn hàng trước khi áp dụng giảm giá (đã bao gồm thuế)
    private BigDecimal orderTotalCost;
    // Số theo dõi đơn hàng (tracking number) để khách hàng có thể theo dõi đơn hàng của mình
    private String orderTrackingNumber;
    // Mã của nhân viên xử lý đơn hàng, khóa ngoại tham chiếu đến bảng nhân viên
    private String employeeId;
    // Mã của thanh toán của đơn hàng, khóa ngoại tham chiếu đến bảng thanh toán cho đơn hàng
    private String orderPaymentId;
    // Mã của địa chỉ giao hàng, khóa ngoại tham chiếu đến bảng địa chỉ giao hàng
    private String shippingAddressId;
    // Mã của chi nhánh nơi đơn hàng được đặt, khóa ngoại tham chiếu đến bảng chi nhánh
    private String branchId;
    // Thời gian tạo đơn hàng
    private LocalDateTime orderCreatedAt;
    // Thời gian cập nhật đơn hàng gần nhất
    private LocalDateTime orderUpdatedAt;

    public static OrderAdminResponse convert(Order order) {
        return OrderAdminResponse.builder()
                .orderId(order.getId().toString())
                .userId(order.getUser().getId().toString())
                .orderStatus(order.getOrderStatus())
                .orderTotalCostAfterDiscount(order.getOrderTotalCostAfterDiscount())
                .orderDiscountCost(order.getOrderDiscountCost())
                .orderTotalCost(order.getOrderTotalCost())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .employeeId(order.getEmployee() != null ? order.getEmployee().getId().toString() : null)
                .orderPaymentId(order.getOrderPayment().getId().toString())
                .shippingAddressId(order.getShippingAddress() != null ? order.getShippingAddress().getId().toString() : null)
                .branchId(order.getBranch().getId().toString())
                .orderCreatedAt(order.getCreatedAt())
                .orderUpdatedAt(order.getUpdatedAt())
                .build();
    }

    public static List<OrderAdminResponse> convert(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return List.of();
        }
        return orders.stream()
                .map(OrderAdminResponse::convert)
                .toList();
    }
}
