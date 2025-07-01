package com.se330.coffee_shop_management_backend.dto.request.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderCreateRequestDTO {
    private UUID shippingAddressId;
    private UUID paymentMethodId;
    private UUID branchId;
}