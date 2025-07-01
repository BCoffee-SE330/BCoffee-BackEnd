package com.se330.coffee_shop_management_backend.dto.request.supplier;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierCreateRequestDTO {
    private String supplierName;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierAddress;
}
