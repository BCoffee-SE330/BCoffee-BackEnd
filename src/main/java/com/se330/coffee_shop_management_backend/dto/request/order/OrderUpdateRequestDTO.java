package com.se330.coffee_shop_management_backend.dto.request.order;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderUpdateRequestDTO {
    private UUID orderId;
    private Constants.OrderStatusEnum orderStatus;
}