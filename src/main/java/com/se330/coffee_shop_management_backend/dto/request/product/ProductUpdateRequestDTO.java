package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductUpdateRequestDTO {
    private UUID productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private int productCommentCount;
    private BigDecimal productRatingsAverage;
    private Boolean productIsPublished;
    private Boolean productIsDeleted;
    private UUID productCategory;
}