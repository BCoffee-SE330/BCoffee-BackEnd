package com.se330.coffee_shop_management_backend.dto.response.order;

import com.se330.coffee_shop_management_backend.entity.OrderDetail;
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
public class OrderDetailResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of order detail creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of order detail update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private int orderDetailQuantity;
    private BigDecimal orderDetailUnitPrice;
    private String productVariantId;
    private String orderId;
    private String productName;
    private String productThumb;
    private String variantTierId;

    public static OrderDetailResponseDTO convert(OrderDetail orderDetail) {
        return OrderDetailResponseDTO.builder()
                .id(orderDetail.getId().toString())
                .createdAt(orderDetail.getCreatedAt())
                .updatedAt(orderDetail.getUpdatedAt())
                .orderDetailQuantity(orderDetail.getOrderDetailQuantity())
                .orderDetailUnitPrice(orderDetail.getOrderDetailUnitPrice())
                .productVariantId(orderDetail.getProductVariant() != null ?
                        orderDetail.getProductVariant().getId().toString() : null)
                .orderId(orderDetail.getOrder() != null ?
                        orderDetail.getOrder().getId().toString() : null)
                .productName(orderDetail.getProductVariant() != null ?
                        orderDetail.getProductVariant().getProduct().getProductName() : null)
                .productThumb(orderDetail.getProductVariant() != null ?
                        orderDetail.getProductVariant().getProduct().getProductThumb() : null)
                .variantTierId(orderDetail.getProductVariant() != null ?
                        orderDetail.getProductVariant().getVariantTierIdx() : null)
                .build();
    }

    public static List<OrderDetailResponseDTO> convert(List<OrderDetail> orderDetails) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return Collections.emptyList();
        }

        return orderDetails.stream()
                .map(OrderDetailResponseDTO::convert)
                .collect(Collectors.toList());
    }
}