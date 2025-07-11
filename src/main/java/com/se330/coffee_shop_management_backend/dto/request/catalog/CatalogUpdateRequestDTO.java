package com.se330.coffee_shop_management_backend.dto.request.catalog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CatalogUpdateRequestDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer parentCatalogId;
}