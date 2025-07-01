package com.se330.coffee_shop_management_backend.dto.request.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InvoiceCreateRequestDTO {
    private String invoiceDescription;
    private BigDecimal invoiceTransferTotalCost;
    private UUID supplierId;
    private UUID warehouseId;

    private List<InvoiceDetailCreateRequestDTO> invoiceDetails;
}
