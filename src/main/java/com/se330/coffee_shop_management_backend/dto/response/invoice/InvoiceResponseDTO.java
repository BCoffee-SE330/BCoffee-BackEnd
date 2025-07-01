package com.se330.coffee_shop_management_backend.dto.response.invoice;

import com.se330.coffee_shop_management_backend.entity.Invoice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InvoiceResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String invoiceDescription;
    private String invoiceTrackingNumber;
    private BigDecimal invoiceTransferTotalCost;
    private List<String> invoiceDetailIds;
    private String supplierId;
    private String warehouseId;

    public static InvoiceResponseDTO convert(Invoice invoice) {
        return InvoiceResponseDTO.builder()
                .id(invoice.getId().toString())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .invoiceDescription(invoice.getInvoiceDescription())
                .invoiceTrackingNumber(invoice.getInvoiceTrackingNumber())
                .invoiceTransferTotalCost(invoice.getInvoiceTransferTotalCost())
                .invoiceDetailIds(invoice.getInvoiceDetails().stream().map(detail -> detail.getId().toString()).toList())
                .supplierId(invoice.getSupplier().getId().toString())
                .warehouseId(invoice.getWarehouse().getId().toString())
                .build();
    }

    public static List<InvoiceResponseDTO> convert(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            return List.of();
        }

        return invoices.stream()
                .map(InvoiceResponseDTO::convert)
                .toList();
    }
}
