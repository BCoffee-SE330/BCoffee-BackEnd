package com.se330.coffee_shop_management_backend.dto.request.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class InvoiceDetailCreateRequestDTO {
    private int invoiceDetailQuantity;
    private String invoiceDetailUnit;
    private UUID ingredientId;

    @Schema(description = "this field could be null or random UUID if creating invoice detail through invoice")
    private UUID invoiceId;
}
