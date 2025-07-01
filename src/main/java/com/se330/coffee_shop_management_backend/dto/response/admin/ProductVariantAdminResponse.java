package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductVariantAdminResponse {
    // Mã biến thể sản phẩm
    private String variantId;
    // Chỉ mục của biến thể trong danh sách các biến thể
    // Hiện chỉ hỗ trợ small, medium, large, default
    private String variantTierIdx;
    // Biến thể có phải là mặc định hay không (chỉ là mặc định nếu small hoặc default)
    private Boolean variantDefault;
    // Slug của biến thể sản phẩm, được tạo tự động từ tên sản phẩm + chỉ mục biến thể
    private String variantSlug;
    // Sắp xếp của biến thể trong danh sách các biến thể
    // small, default: 1
    // medium: 2, large: 3
    private int variantSort;
    // Giá của biến thể sản phẩm
    private BigDecimal variantPrice;
    // Trạng thái của biến thể sản phẩm (đã xuất bản hay chưa)
    private Boolean variantIsPublished;
    // Trạng thái của biến thể sản phẩm (đã xóa hay chưa)
    private Boolean variantIsDeleted;
    // Mã của sản phẩm mà biến thể này thuộc về, khóa ngoại tham chiếu đến bảng sản phẩm
    private String productId;

    public static ProductVariantAdminResponse convert(ProductVariant productVariant) {
        return ProductVariantAdminResponse.builder()
                .variantId(productVariant.getId().toString())
                .variantTierIdx(productVariant.getVariantTierIdx())
                .variantDefault(productVariant.getVariantDefault())
                .variantSlug(productVariant.getVariantSlug())
                .variantSort(productVariant.getVariantSort())
                .variantPrice(productVariant.getVariantPrice())
                .variantIsPublished(productVariant.getVariantIsPublished())
                .variantIsDeleted(productVariant.getVariantIsDeleted())
                .productId(productVariant.getProduct().getId().toString())
                .build();
    }

    public static List<ProductVariantAdminResponse> convert(List<ProductVariant> productVariants) {
        if (productVariants == null || productVariants.isEmpty()) {
            return List.of();
        }
        return productVariants.stream()
                .map(ProductVariantAdminResponse::convert)
                .toList();
    }
}
