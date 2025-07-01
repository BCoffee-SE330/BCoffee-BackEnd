package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "transfer_id"))
})
public class Transfer extends AbstractBaseEntity {
    @Column(name = "transfer_description", nullable = false)
    private String transferDescription;

    @Column(name = "transfer_tracking_number", nullable = false)
    private String transferTrackingNumber;

    @Column(name = "transfer_total_cost", nullable = false)
    private BigDecimal transferTotalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_branch",
                    foreignKeyDefinition = "FOREIGN KEY (branch_id) REFERENCES branches (branch_id) ON DELETE CASCADE"
            )
    )
    private Branch branch;

    // Many transfers belong to one warehouse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "warehouse_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_warehouse",
                    foreignKeyDefinition = "FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE CASCADE"
            )
    )
    private Warehouse warehouse;

    // One transfer has many transfer details
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransferDetail> transferDetails = new ArrayList<>();
}