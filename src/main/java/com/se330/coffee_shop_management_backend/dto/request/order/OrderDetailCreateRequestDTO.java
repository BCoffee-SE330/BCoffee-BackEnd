package com.se330.coffee_shop_management_backend.dto.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderDetailCreateRequestDTO {
    private int orderDetailQuantity;
    private UUID productVariantId;
    private UUID orderId;
    private UUID branchId;
}