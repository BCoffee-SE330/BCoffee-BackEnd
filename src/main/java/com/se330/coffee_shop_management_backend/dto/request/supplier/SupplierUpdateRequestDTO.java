package com.se330.coffee_shop_management_backend.dto.request.supplier;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SupplierUpdateRequestDTO {
    private UUID supplierId;
    private String supplierName;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierAddress;
}
