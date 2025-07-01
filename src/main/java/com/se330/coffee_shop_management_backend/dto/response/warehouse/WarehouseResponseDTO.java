package com.se330.coffee_shop_management_backend.dto.response.warehouse;

import com.se330.coffee_shop_management_backend.entity.Warehouse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class WarehouseResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of shift creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of shift update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String warehouseName;
    private String warehousePhone;
    private String warehouseEmail;
    private String warehouseAddress;

    public static WarehouseResponseDTO convert(Warehouse warehouse) {
        return WarehouseResponseDTO.builder()
                .id(warehouse.getId().toString())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .warehouseName(warehouse.getWarehouseName())
                .warehousePhone(warehouse.getWarehousePhone())
                .warehouseEmail(warehouse.getWarehouseEmail())
                .warehouseAddress(warehouse.getWarehouseAddress())
                .build();
    }

    public static List<WarehouseResponseDTO> convert(List<Warehouse> warehouses) {
        if (warehouses == null || warehouses.isEmpty()) {
            return List.of();
        }

        return warehouses.stream()
                .map(WarehouseResponseDTO::convert)
                .toList();
    }
}
