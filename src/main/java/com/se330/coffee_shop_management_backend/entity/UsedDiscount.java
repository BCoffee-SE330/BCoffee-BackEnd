package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "used_discounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "used_discount_id"))
})
public class UsedDiscount extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "discount_id",
            foreignKey = @ForeignKey(
                    name = "fk_used_discount_discount",
                    foreignKeyDefinition = "FOREIGN KEY (discount_id) REFERENCES discounts (discount_id) ON DELETE CASCADE"
            )
    )
    private Discount discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_detail_id",
            foreignKey = @ForeignKey(
                    name = "fk_used_discount_order_detail",
                    foreignKeyDefinition = "FOREIGN KEY (order_detail_id) REFERENCES order_details (order_detail_id) ON DELETE CASCADE"
            )
    )
    private OrderDetail orderDetail;

    @Column(name = "times_use", nullable = false)
    private int timesUse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cart_detail_id",
            foreignKey = @ForeignKey(
                    name = "fk_used_discount_cart_detail",
                    foreignKeyDefinition = "FOREIGN KEY (cart_detail_id) REFERENCES cart_details (cart_detail_id) ON DELETE CASCADE"
            )
    )
    private CartDetail cartDetail;
}