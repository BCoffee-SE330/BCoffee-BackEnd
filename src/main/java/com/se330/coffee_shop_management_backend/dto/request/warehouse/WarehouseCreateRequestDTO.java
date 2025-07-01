package com.se330.coffee_shop_management_backend.dto.request.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseCreateRequestDTO {
    private String warehouseName;
    private String warehousePhone;
    private String warehouseEmail;
    private String warehouseAddress;
}
