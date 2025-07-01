package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "inventory_id"))
})
public class Inventory extends AbstractBaseEntity {
    @Column(name = "inventory_quantity",nullable = false)
    private int inventoryQuantity;

    @Column(name = "inventory_expire_date", nullable = false)
    private LocalDateTime inventoryExpireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(
                    name = "fk_inventory_branch",
                    foreignKeyDefinition = "FOREIGN KEY (branch_id) REFERENCES branches (branch_id) ON DELETE CASCADE"
            )
    )
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "fk_inventory_ingredient",
                    foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE"
            )
    )
    private Ingredient ingredient;
}