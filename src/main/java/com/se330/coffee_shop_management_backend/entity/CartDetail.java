package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "cart_detail_id"))
})
public class CartDetail extends AbstractBaseEntity {
    @Column(name = "cart_detail_quantity", nullable = false)
    private int cartDetailQuantity;

    @Column(name = "cart_detail_unit_price", nullable = false)
    private BigDecimal cartDetailUnitPrice;

    @Column(name = "cart_detail_discount_cost", nullable = false)
    private BigDecimal cartDetailDiscountCost;

    @Column(name = "cart_detail_unit_price_after_discount", nullable = false)
    private BigDecimal cartDetailUnitPriceAfterDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cart_id",
            foreignKey = @ForeignKey(
                    name = "fk_cart_detail_cart",
                    foreignKeyDefinition = "FOREIGN KEY (cart_id) REFERENCES carts (cart_id) ON DELETE CASCADE"
            )
    )
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "var_id",
            foreignKey = @ForeignKey(
                    name = "fk_cart_detail_product_variant",
                    foreignKeyDefinition = "FOREIGN KEY (var_id) REFERENCES product_variants (var_id) ON DELETE CASCADE"
            )
    )
    private ProductVariant productVariant;

    @OneToMany(mappedBy = "cartDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsedDiscount> usedDiscounts = new ArrayList<>();
}