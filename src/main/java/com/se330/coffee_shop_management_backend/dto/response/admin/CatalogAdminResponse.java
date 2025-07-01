package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Catalog;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CatalogAdminResponse {
    // Mã của danh mục
    private Integer catalogId;
    // Thời gian tạo danh mục
    private LocalDateTime createdAt;
    // Thời gian cập nhật danh mục gần nhất
    private LocalDateTime updatedAt;
    // Tên của danh mục
    private String name;
    // Mô tả chi tiết của danh mục
    private String description;
    // Mã của danh mục cha, khóa ngoại tham chiếu đến bảng catalogs, nếu không có danh mục cha thì để null
    private Integer parentCatalogId;

    public static CatalogAdminResponse convert(Catalog catalog) {
        return CatalogAdminResponse.builder()
                .catalogId(catalog.getId())
                .createdAt(catalog.getCreatedAt())
                .updatedAt(catalog.getUpdatedAt())
                .name(catalog.getName())
                .description(catalog.getDescription())
                .parentCatalogId(catalog.getParentCatalog() != null ? catalog.getParentCatalog().getId() : null)
                .build();
    }

    public static List<CatalogAdminResponse> convert(List<Catalog> catalogs) {
        if (catalogs == null || catalogs.isEmpty()) {
            return List.of();
        }

        return catalogs.stream()
                .map(CatalogAdminResponse::convert)
                .toList();
    }
}