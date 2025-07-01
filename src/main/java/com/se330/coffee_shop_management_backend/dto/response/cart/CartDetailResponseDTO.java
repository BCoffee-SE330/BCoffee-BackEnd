package com.se330.coffee_shop_management_backend.dto.response.cart;

import com.se330.coffee_shop_management_backend.entity.CartDetail;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CartDetailResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    private String cartId;
    private String variantId;
    private int cartDetailQuantity;
    private BigDecimal cartDetailUnitPrice;
    private BigDecimal cartDetailTotalPrice;
    private BigDecimal cartDetailDiscountCost;
    private BigDecimal cartDetailUnitPriceAfterDiscount;
    private BigDecimal cartDetailTotalPriceAfterDiscount;
    private String productThumb;
    private String productName;
    private String variantTierIdx;
    private String productId;

    public static CartDetailResponseDTO convert(CartDetail cartDetail) {

        Product product = cartDetail.getProductVariant().getProduct();
        String productThumb = product.getProductThumb() != null ? product.getProductThumb() : null;
        String productName = product.getProductName();
        String productId = product.getId() != null ? product.getId().toString() : null;
        String variantTierIdx = cartDetail.getProductVariant().getVariantTierIdx() != null ? cartDetail.getProductVariant().getVariantTierIdx() : "";
        
        return CartDetailResponseDTO.builder()
                .id(cartDetail.getId().toString())
                .cartId(cartDetail.getCart().getId().toString())
                .variantId(cartDetail.getProductVariant().getId().toString())
                .productThumb(productThumb)
                .productName(productName)
                .variantTierIdx(variantTierIdx)
                .cartDetailQuantity(cartDetail.getCartDetailQuantity())
                .productId(productId)
                .cartDetailUnitPrice(cartDetail.getCartDetailUnitPrice())
                .cartDetailTotalPrice(cartDetail.getCartDetailUnitPrice().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())))
                .cartDetailDiscountCost(cartDetail.getCartDetailDiscountCost())
                .cartDetailUnitPriceAfterDiscount(cartDetail.getCartDetailUnitPriceAfterDiscount())
                .cartDetailTotalPriceAfterDiscount(cartDetail.getCartDetailUnitPriceAfterDiscount().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())))
                .build();
    }

    public static List<CartDetailResponseDTO> convert(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return List.of();
        }

        return cartDetails.stream()
                .map(CartDetailResponseDTO::convert)
                .toList();
    }
}
