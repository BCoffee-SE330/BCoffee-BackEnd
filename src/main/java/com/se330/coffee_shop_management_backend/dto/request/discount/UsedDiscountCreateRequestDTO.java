package com.se330.coffee_shop_management_backend.dto.request.discount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsedDiscountCreateRequestDTO {
    private UUID orderDetailId;
    private UUID discountId;
    private int timesUse;
}
