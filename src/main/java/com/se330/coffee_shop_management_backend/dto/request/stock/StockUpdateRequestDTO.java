package com.se330.coffee_shop_management_backend.dto.request.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class StockUpdateRequestDTO {
    private UUID stockId;
    private int stockQuantity;
    private String stockUnit;
    private UUID ingredientId;
    private UUID warehouseId;
}
