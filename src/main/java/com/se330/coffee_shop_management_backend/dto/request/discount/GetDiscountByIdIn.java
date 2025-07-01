package com.se330.coffee_shop_management_backend.dto.request.discount;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetDiscountByIdIn {
    List<String> discountIds;
}
