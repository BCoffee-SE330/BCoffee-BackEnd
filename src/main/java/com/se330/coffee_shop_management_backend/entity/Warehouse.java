package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "warehouse_id"))
})
public class Warehouse extends AbstractBaseEntity {
    @Column(name = "warehouse_name",nullable = false)
    private String warehouseName;

    @Column(name = "warehouse_phone", nullable = false)
    private String warehousePhone;

    @Column(name = "warehouse_email", nullable = false)
    private String warehouseEmail;

    @Column(name = "warehouse_address", nullable = false)
    private String warehouseAddress;

    // Replace ManyToOne with OneToMany for invoices
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();

    // These relationships are already correctly implemented
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Transfer> transfers = new ArrayList<>();
}