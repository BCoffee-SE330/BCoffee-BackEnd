package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transfer_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "transfer_detail_id"))
})
public class TransferDetail extends AbstractBaseEntity {
    @Column(name = "transfer_detail_quantity", nullable = false)
    private int transferDetailQuantity;

    @Column(name = "transfer_detail_unit", nullable = false)
    private String transferDetailUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_detail_ingredient",
                    foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE"
            )
    )
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "transfer_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_detail_transfer",
                    foreignKeyDefinition = "FOREIGN KEY (transfer_id) REFERENCES transfers (transfer_id) ON DELETE CASCADE"
            )
    )
    private Transfer transfer;
}