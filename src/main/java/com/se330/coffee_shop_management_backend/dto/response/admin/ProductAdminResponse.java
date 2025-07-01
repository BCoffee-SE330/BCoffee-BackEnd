package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductAdminResponse {
    // Mã sản phẩm
    private String productId;
    // Tên sản phẩm
    private String productName;
    // Ảnh đại diện sản phẩm (đường dẫn URL)
    private String productThumb;
    // Mô tả sản phẩm
    private String productDescription;
    // Giá sản phẩm, tính theo VNĐ
    private BigDecimal productPrice;
    // Slug sản phẩm, dùng để tạo URL thân thiện, được tạo tự động
    private String productSlug;
    // Số lượng bình luận về sản phẩm
    private int productCommentCount;
    // Điểm đánh giá trung bình của sản phẩm, tính từ 0.00 đến 5.00
    private BigDecimal productRatingsAverage;
    // Trạng thái sản phẩm: đã xuất bản hay chưa
    private Boolean productIsPublished;
    // Trạng thái sản phẩm: đã bị xóa hay chưa
    private Boolean productIsDeleted;
    // Mã của danh mục sản phẩm, khóa ngoại tham chiếu đến bảng danh mục sản phẩm
    private String productCategoryId;
    // Thời gian tạo sản phẩm
    private LocalDateTime productCreatedAt;
    // Thời gian cập nhật sản phẩm
    private LocalDateTime productUpdatedAt;

    public static ProductAdminResponse convert(Product product) {
        return ProductAdminResponse.builder()
                .productId(product.getId().toString())
                .productName(product.getProductName())
                .productThumb(product.getProductThumb())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productSlug(product.getProductSlug())
                .productCommentCount(product.getProductCommentCount())
                .productRatingsAverage(product.getProductRatingsAverage())
                .productIsPublished(product.getProductIsPublished())
                .productIsDeleted(product.getProductIsDeleted())
                .productCategoryId(product.getProductCategory().getId().toString())
                .build();
    }

    public static List<ProductAdminResponse> convert(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        return products.stream()
                .map(ProductAdminResponse::convert)
                .toList();
    }
}
