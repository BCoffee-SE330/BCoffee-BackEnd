package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "invoice_detail_id"))
})
public class InvoiceDetail extends AbstractBaseEntity {
    @Column(name = "invoice_detail_quantity", nullable = false)
    private int invoiceDetailQuantity;

    @Column(name = "invoice_detail_unit", nullable = false)
    private String invoiceDetailUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "fk_invoice_detail_ingredient",
                    foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) ON DELETE CASCADE"
            )
    )
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "invoice_id",
            foreignKey = @ForeignKey(
                    name = "fk_invoice_detail_invoice",
                    foreignKeyDefinition = "FOREIGN KEY (invoice_id) REFERENCES invoices (invoice_id) ON DELETE CASCADE"
            )
    )
    private Invoice invoice;
}