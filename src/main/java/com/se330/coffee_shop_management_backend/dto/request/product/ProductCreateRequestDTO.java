package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductCreateRequestDTO {
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private UUID productCategory;
}
