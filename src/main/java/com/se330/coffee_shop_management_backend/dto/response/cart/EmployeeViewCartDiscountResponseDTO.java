package com.se330.coffee_shop_management_backend.dto.response.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@SuperBuilder
public class EmployeeViewCartDiscountResponseDTO {
    private BigDecimal cartTotalCost;
    private BigDecimal cartDiscountCost;
    private BigDecimal cartTotalCostAfterDiscount;
}
