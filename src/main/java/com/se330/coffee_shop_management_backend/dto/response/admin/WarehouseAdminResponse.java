package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Warehouse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class WarehouseAdminResponse {
    // Mã định danh của nhà kho
    private String warehouseId;
    // Tên của nhà kho
    private String warehouseName;
    // Số điện thoại của nhà kho
    private String warehousePhone;
    // Email của nhà kho
    private String warehouseEmail;
    // Địa chỉ của nhà kho
    private String warehouseAddress;

    public static WarehouseAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Warehouse warehouse) {
        return WarehouseAdminResponse.builder()
                .warehouseId(warehouse.getId().toString())
                .warehouseName(warehouse.getWarehouseName())
                .warehousePhone(warehouse.getWarehousePhone())
                .warehouseEmail(warehouse.getWarehouseEmail())
                .warehouseAddress(warehouse.getWarehouseAddress())
                .build();
    }

    public static List<WarehouseAdminResponse> convert(List<Warehouse> warehouses) {
        if (warehouses == null || warehouses.isEmpty()) {
            return List.of();
        }
        return warehouses.stream()
                .map(WarehouseAdminResponse::convert)
                .toList();
    }
}
