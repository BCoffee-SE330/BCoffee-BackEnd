package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class InvoiceDetailAdminResponse {
    // Khóa ngoại tham chiếu đến bảng Invoice
    private String invoiceId;
    // Số lượng nguyên liệu trong hóa đơn
    private int invoiceDetailQuantity;
    // Đơn giá của nguyên liệu trong hóa đơn (gram hoặc ml)
    private String invoiceDetailUnit;
    // Mã của nguyên liệu, khóa ngoại tham chiếu đến bảng Ingredient
    private String ingredientId;

    public static InvoiceDetailAdminResponse convert(InvoiceDetail invoiceDetail) {
        return InvoiceDetailAdminResponse.builder()
                .invoiceId(invoiceDetail.getInvoice().getId().toString())
                .invoiceDetailQuantity(invoiceDetail.getInvoiceDetailQuantity())
                .invoiceDetailUnit(invoiceDetail.getInvoiceDetailUnit())
                .ingredientId(invoiceDetail.getIngredient().getId().toString())
                .build();
    }

    public static List<InvoiceDetailAdminResponse> convert(List<InvoiceDetail> invoiceDetails) {
        if (invoiceDetails == null || invoiceDetails.isEmpty()) {
            return List.of();
        }

        return invoiceDetails.stream()
                .map(InvoiceDetailAdminResponse::convert)
                .toList();
    }
}
