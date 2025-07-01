package com.se330.coffee_shop_management_backend.dto.request.invoice;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class InvoiceDetailUpdateRequestDTO {
    private UUID id;
    private int invoiceDetailQuantity;
    private String invoiceDetailUnit;
    private UUID ingredientId;
    private UUID invoiceId;
}
