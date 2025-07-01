package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockAdminResponse {
    // Mã định danh của kho hàng, khóa ngoại tham chiếu đến bảng Warehouse
    private String warehouseId;
    // Mã định danh của nguyên liệu, khóa ngoại tham chiếu đến bảng Ingredient
    private String ingredientId;
    // Số lượng nguyên liệu trong kho
    private int stockQuantity;
    // Đơn vị của nguyên liệu trong kho (gram hoặc ml)
    private String stockUnit;

    public static StockAdminResponse convert(com.se330.coffee_shop_management_backend.entity.Stock stock) {
        return StockAdminResponse.builder()
                .warehouseId(stock.getWarehouse().getId().toString())
                .ingredientId(stock.getIngredient().getId().toString())
                .stockQuantity(stock.getStockQuantity())
                .stockUnit(stock.getStockUnit())
                .build();
    }

    public static java.util.List<StockAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) {
            return java.util.List.of();
        }
        return stocks.stream()
                .map(StockAdminResponse::convert)
                .toList();
    }
}
