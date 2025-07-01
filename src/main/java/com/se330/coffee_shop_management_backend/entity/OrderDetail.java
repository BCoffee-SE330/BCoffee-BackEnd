package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "order_detail_id"))
})
public class OrderDetail extends AbstractBaseEntity {
    @Column(name = "order_detail_quantity",  nullable = false)
    private int orderDetailQuantity;

    @Column(name = "order_detail_unit_price", nullable = false)
    private BigDecimal orderDetailUnitPrice;

    @Column(name = "order_detail_unit_price_after_discount", nullable = false)
    private BigDecimal orderDetailUnitPriceAfterDiscount;

    @Column(name = "order_detail_discount_cost", nullable = false)
    private BigDecimal orderDetailDiscountCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "var_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_detail_product_variant",
                    foreignKeyDefinition = "FOREIGN KEY (var_id) REFERENCES product_variants (var_id) ON DELETE CASCADE"
            )
    )
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(
                    name = "fk_order_detail_order",
                    foreignKeyDefinition = "FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE"
            )
    )
    private Order order;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsedDiscount> usedDiscounts = new ArrayList<>();
}