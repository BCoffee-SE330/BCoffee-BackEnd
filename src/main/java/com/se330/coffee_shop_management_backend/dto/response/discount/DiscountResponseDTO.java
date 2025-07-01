package com.se330.coffee_shop_management_backend.dto.response.discount;

import com.se330.coffee_shop_management_backend.entity.Discount;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class DiscountResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of discount creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of discount update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String discountName;
    private String discountDescription;
    private String discountType;
    private BigDecimal discountValue;
    private String discountCode;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private int discountMaxUsers;
    private int discountUserCount;
    private int discountMaxPerUser;
    private BigDecimal discountMinOrderValue;
    private boolean discountIsActive;
    private String branchId;
    private String branchName;
    private Set<ProductResponseDTO> products;

    public static DiscountResponseDTO convert(Discount discount) {
        if (discount == null) {
            return null;
        }

        Set<ProductResponseDTO> products = new HashSet<>();

        for (ProductVariant productVariant : discount.getProductVariants()) {
            products.add(new ProductResponseDTO(productVariant));
        }

        return DiscountResponseDTO.builder()
                .id(discount.getId().toString())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .discountName(discount.getDiscountName())
                .discountDescription(discount.getDiscountDescription())
                .discountType(discount.getDiscountType().getValue())
                .discountValue(discount.getDiscountValue())
                .discountCode(discount.getDiscountCode())
                .discountStartDate(discount.getDiscountStartDate())
                .discountEndDate(discount.getDiscountEndDate())
                .discountMaxUsers(discount.getDiscountMaxUsers())
                .discountUserCount(discount.getDiscountUserCount())
                .discountMaxPerUser(discount.getDiscountMaxPerUser())
                .discountMinOrderValue(discount.getDiscountMinOrderValue())
                .discountIsActive(discount.isDiscountIsActive())
                .branchId(discount.getBranch() != null ? discount.getBranch().getId().toString() : null)
                .branchName(discount.getBranch() != null ? discount.getBranch().getBranchName() : null)
                .products(products)
                .build();
    }

    public static List<DiscountResponseDTO> convert(List<Discount> discounts) {
        if (discounts == null || discounts.isEmpty()) {
            return Collections.emptyList();
        }

        return discounts.stream()
                .map(DiscountResponseDTO::convert)
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponseDTO {
        private String id;
        private String name;
        private String thumb;
        private BigDecimal price;
        private BigDecimal ratingsAverage;

        public ProductResponseDTO(ProductVariant productVariant) {
            this.id = productVariant.getProduct().getId().toString();
            this.name = productVariant.getProduct().getProductName();
            this.thumb = productVariant.getProduct().getProductThumb();
            this.price = productVariant.getProduct().getProductPrice();
            this.ratingsAverage = productVariant.getProduct().getProductRatingsAverage();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductResponseDTO that = (ProductResponseDTO) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}