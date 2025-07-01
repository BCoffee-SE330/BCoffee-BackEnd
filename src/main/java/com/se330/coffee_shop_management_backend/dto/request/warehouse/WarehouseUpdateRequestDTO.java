package com.se330.coffee_shop_management_backend.dto.request.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class WarehouseUpdateRequestDTO {
    private UUID warehouseId;
    private String warehouseName;
    private String warehousePhone;
    private String warehouseEmail;
    private String warehouseAddress;
}
