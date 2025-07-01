package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Invoice;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InvoiceAdminResponse {
    // Mã hóa đơn vận chuyển hàng tồn kho vào tồn kho của nhà kho
    private String invoiceId;
    // Mô tả hóa đơn
    private String invoiceDescription;
    // Số theo dõi hóa đơn khi vận chuyển hàng tồn kho
    private String invoiceTrackingNumber;
    // Tổng chi phí vận chuyển hóa đơn
    private BigDecimal invoiceTransferTotalCost;
    // Mã của nhà cung cấp, khóa ngoại tham chiếu đến bảng nhà cung cấp
    private String supplierId;
    // Mã của nhà kho, khóa ngoại tham chiếu đến bảng nhà kho
    private String warehouseId;

    public static InvoiceAdminResponse convert(Invoice invoice) {
        return InvoiceAdminResponse.builder()
                .invoiceId(invoice.getId().toString())
                .invoiceDescription(invoice.getInvoiceDescription())
                .invoiceTrackingNumber(invoice.getInvoiceTrackingNumber())
                .invoiceTransferTotalCost(invoice.getInvoiceTransferTotalCost())
                .supplierId(invoice.getSupplier().getId().toString())
                .warehouseId(invoice.getWarehouse().getId().toString())
                .build();
    }

    public static List<InvoiceAdminResponse> convert(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            return List.of();
        }

        return invoices.stream()
                .map(InvoiceAdminResponse::convert)
                .toList();
    }
}
