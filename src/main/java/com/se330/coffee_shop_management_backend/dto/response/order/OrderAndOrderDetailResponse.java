package com.se330.coffee_shop_management_backend.dto.response.order;

import com.se330.coffee_shop_management_backend.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderAndOrderDetailResponse {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of order creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of order update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private BigDecimal orderTotalCost;
    private BigDecimal orderDiscountCost;
    private BigDecimal orderTotalCostAfterDiscount;
    private String orderStatus;
    private String orderTrackingNumber;
    private String employeeName;
    private String userName;
    private String userPhoneNumber;
    private String shippingAddressName;

    private List<OrderDetailResponseDTO> orderDetails;

    public static OrderAndOrderDetailResponse convert(Order order) {

        OrderAndOrderDetailResponse orderAndOrderDetailResponse = OrderAndOrderDetailResponse.builder()
                .id(order.getId().toString())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderTotalCost(order.getOrderTotalCost())
                .orderDiscountCost(order.getOrderDiscountCost())
                .orderTotalCostAfterDiscount(order.getOrderTotalCostAfterDiscount())
                .orderStatus(order.getOrderStatus().getValue())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .employeeName(order.getEmployee() != null ? order.getEmployee().getUser().getFullName() : null)
                .userName(order.getUser() != null ? order.getUser().getFullName() : null)
                .userPhoneNumber(order.getUser() != null ? order.getUser().getPhoneNumber() : null)
                .shippingAddressName(order.getShippingAddress() != null ? order.getShippingAddress().toString() : null)
                .build();

        if (order.getOrderDetails() != null) {
            orderAndOrderDetailResponse.setOrderDetails(
                    OrderDetailResponseDTO.convert(order.getOrderDetails())
            );
        } else {
            orderAndOrderDetailResponse.setOrderDetails(Collections.emptyList());
        }

        return orderAndOrderDetailResponse;
    }

    public static List<OrderAndOrderDetailResponse> convert(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(OrderAndOrderDetailResponse::convert)
                .collect(Collectors.toList());
    }
}
