package com.se330.coffee_shop_management_backend.dto.request.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductCategoryUpdateRequestDTO {
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
    private Integer catalogId;
}

