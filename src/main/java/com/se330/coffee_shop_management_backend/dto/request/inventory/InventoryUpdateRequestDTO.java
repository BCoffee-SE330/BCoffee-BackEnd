package com.se330.coffee_shop_management_backend.dto.request.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InventoryUpdateRequestDTO {
    private UUID inventoryId;
    private int inventoryQuantity;
    private LocalDateTime inventoryExpireDate;
    private UUID branchId;
    private UUID ingredientId;
}
