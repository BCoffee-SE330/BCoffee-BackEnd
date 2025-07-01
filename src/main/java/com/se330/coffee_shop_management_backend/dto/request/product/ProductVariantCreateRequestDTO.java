package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductVariantCreateRequestDTO {
    private String variantTierIdx;
    private Boolean variantDefault;
    private BigDecimal variantPrice;
    private UUID product;
    private int variantSort;
}