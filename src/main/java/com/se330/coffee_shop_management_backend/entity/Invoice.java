package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "invoice_id"))
})
public class Invoice extends AbstractBaseEntity {
    @Column(name = "invoice_description", nullable = false)
    private String invoiceDescription;

    @Column(name = "invoice_tracking_number", nullable = false)
    private String invoiceTrackingNumber;

    @Column(name = "invoice_transfer_total_cost", nullable = false)
    private BigDecimal invoiceTransferTotalCost;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "supplier_id",
            foreignKey = @ForeignKey(
                    name = "fk_invoice_supplier",
                    foreignKeyDefinition = "FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id) ON DELETE CASCADE"
            )
    )
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "warehouse_id",
            foreignKey = @ForeignKey(
                    name = "fk_invoice_warehouse",
                    foreignKeyDefinition = "FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE CASCADE"
            )
    )
    private Warehouse warehouse;
}