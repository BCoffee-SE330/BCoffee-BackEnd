package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductCategoryAdminResponse {
    // Mã loai sản phẩm
    private String categoryId;
    // Tên loai sản phẩm
    private String categoryName;
    // Mô tả loai sản phẩm
    private String categoryDescription;
    // Mã danh mục sản phẩm, khóa ngoại tham chiếu đến bảng danh mục sản phẩm
    private String catalogId;

    // Thông tin ngày tạo loại sản phẩm
    private LocalDateTime createdAt;
    // Thông tin ngày cập nhật loại sản phẩm
    private LocalDateTime updatedAt;

    public static ProductCategoryAdminResponse convert(ProductCategory productCategory) {
        return ProductCategoryAdminResponse.builder()
                .categoryId(productCategory.getId().toString())
                .categoryName(productCategory.getCategoryName())
                .categoryDescription(productCategory.getCategoryDescription())
                .catalogId(productCategory.getCatalog() != null ? productCategory.getCatalog().getId().toString() : null)
                .createdAt(productCategory.getCreatedAt())
                .updatedAt(productCategory.getUpdatedAt())
                .build();
    }

    public static List<ProductCategoryAdminResponse> convert(List<ProductCategory> productCategories) {
        if (productCategories == null || productCategories.isEmpty()) {
            return List.of();
        }

        return productCategories.stream()
                .map(ProductCategoryAdminResponse::convert)
                .toList();
    }
}
