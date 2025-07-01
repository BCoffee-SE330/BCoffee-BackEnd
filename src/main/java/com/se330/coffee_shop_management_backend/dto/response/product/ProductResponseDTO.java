package com.se330.coffee_shop_management_backend.dto.response.product;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of product creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of product update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String productName;
    private String productThumb;
    private String productDescription;
    private BigDecimal productPrice;
    private String productSlug;
    private BigDecimal productRatingsAverage;
    private Boolean productIsPublished;
    private Boolean productIsDeleted;
    private String productCategoryId;
    private boolean isFavorite;
    private int productCommentCount;

    public static ProductResponseDTO convert(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .productName(product.getProductName())
                .productThumb(product.getProductThumb())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productSlug(product.getProductSlug())
                .productRatingsAverage(product.getProductRatingsAverage())
                .productIsPublished(product.getProductIsPublished())
                .productIsDeleted(product.getProductIsDeleted())
                .productCategoryId(product.getProductCategory().getId().toString())
                .productCommentCount(product.getProductCommentCount())
                .build();
    }

    public static ProductResponseDTO convert(Product product, boolean isFavorite) {
        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .productName(product.getProductName())
                .productThumb(product.getProductThumb())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productSlug(product.getProductSlug())
                .productRatingsAverage(product.getProductRatingsAverage())
                .productIsPublished(product.getProductIsPublished())
                .productIsDeleted(product.getProductIsDeleted())
                .isFavorite(isFavorite)
                .productCategoryId(product.getProductCategory().getId().toString())
                .build();
    }

    public static List<ProductResponseDTO> convert(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(ProductResponseDTO::convert)
                .collect(Collectors.toList());
    }

}
