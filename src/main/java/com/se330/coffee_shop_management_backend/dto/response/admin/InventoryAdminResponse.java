package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Inventory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InventoryAdminResponse {
    // Mã tồn kho của chi nhánh
    private String inventoryId;
    // Số lượng tồn kho của chi nhánh
    private int inventoryQuantity;
    // Ngày hết hạn của tồn kho, được tính theo ngày hết hạn của nguyên liệu cộng với thời gian bảo quản của nguyên liệu (tính bằng ngày)
    private LocalDateTime inventoryExpireDate;
    // Mã của chi nhánh, khóa ngoại tham chiếu đến bảng chi nhánh
    private String branchId;
    // Mã của nguyên liệu, khóa ngoại tham chiếu đến bảng nguyên liệu
    private String ingredientId;

    public static InventoryAdminResponse convert(Inventory inventory) {
        return InventoryAdminResponse.builder()
                .inventoryId(inventory.getId().toString())
                .inventoryQuantity(inventory.getInventoryQuantity())
                .inventoryExpireDate(inventory.getInventoryExpireDate())
                .branchId(inventory.getBranch().getId().toString())
                .ingredientId(inventory.getIngredient().getId().toString())
                .build();
    }

    public static List<InventoryAdminResponse> convert(List<Inventory> inventories) {
        if (inventories == null || inventories.isEmpty()) {
            return List.of();
        }

        return inventories.stream()
                .map(InventoryAdminResponse::convert)
                .toList();
    }
}
