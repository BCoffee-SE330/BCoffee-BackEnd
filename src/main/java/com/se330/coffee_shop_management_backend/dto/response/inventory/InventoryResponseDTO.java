package com.se330.coffee_shop_management_backend.dto.response.inventory;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InventoryResponseDTO  {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private int inventoryQuantity;
    private LocalDateTime inventoryExpireDate;
    private String branchId;
    private String ingredientId;

    public static InventoryResponseDTO convert(com.se330.coffee_shop_management_backend.entity.Inventory inventory) {
        return InventoryResponseDTO.builder()
                .id(inventory.getId().toString())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .inventoryQuantity(inventory.getInventoryQuantity())
                .inventoryExpireDate(inventory.getInventoryExpireDate())
                .branchId(inventory.getBranch().getId().toString())
                .ingredientId(inventory.getIngredient().getId().toString())
                .build();
    }

    public static List<InventoryResponseDTO> convert(List<com.se330.coffee_shop_management_backend.entity.Inventory> inventories) {
        if(inventories == null || inventories.isEmpty()) {
            return List.of();
        }

        return inventories.stream()
                .map(InventoryResponseDTO::convert)
                .toList();
    }
}
