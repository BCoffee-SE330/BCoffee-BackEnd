package com.se330.coffee_shop_management_backend.dto.request.discount;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UsedDiscountUpdateRequestDTO {
    private UUID id;
    private UUID orderDetailId;
    private UUID discountId;
    private int timesUse;
}
