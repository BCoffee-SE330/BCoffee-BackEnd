package com.se330.coffee_shop_management_backend.dto.response.invoice;

import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InvoiceDetailResponseDTO {
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

    private int invoiceDetailQuantity;
    private String invoiceDetailUnit;
    private String ingredientId;
    private String invoiceId;

    public static InvoiceDetailResponseDTO convert(InvoiceDetail invoiceDetail) {
        return InvoiceDetailResponseDTO.builder()
                .id(invoiceDetail.getId().toString())
                .createdAt(invoiceDetail.getCreatedAt())
                .updatedAt(invoiceDetail.getUpdatedAt())
                .invoiceDetailQuantity(invoiceDetail.getInvoiceDetailQuantity())
                .invoiceDetailUnit(invoiceDetail.getInvoiceDetailUnit())
                .ingredientId(invoiceDetail.getIngredient().getId().toString())
                .invoiceId(invoiceDetail.getInvoice().getId().toString())
                .build();
    }

    public static List<InvoiceDetailResponseDTO> convert(List<InvoiceDetail> invoiceDetails) {
        if (invoiceDetails == null || invoiceDetails.isEmpty()) {
            return List.of();
        }

        return invoiceDetails.stream()
                .map(InvoiceDetailResponseDTO::convert)
                .toList();
    }
}
