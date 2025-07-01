package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "cart_id"))
})
public class Cart extends AbstractBaseEntity {
    @Column(name = "cart_total_cost", nullable = false)
    private BigDecimal cartTotalCost;

    @Column(name = "cart_discount_cost", nullable = false)
    private BigDecimal cartDiscountCost;

    @Column(name = "cart_total_cost_after_discount", nullable = false)
    private BigDecimal cartTotalCostAfterDiscount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_cart_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartDetail> cartDetails = new ArrayList<>();
}