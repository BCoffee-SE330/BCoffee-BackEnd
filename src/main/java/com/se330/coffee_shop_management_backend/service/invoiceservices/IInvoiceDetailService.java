package com.se330.coffee_shop_management_backend.service.invoiceservices;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IInvoiceDetailService {
    InvoiceDetail findByIdInvoiceDetail(UUID id);
    Page<InvoiceDetail> findAllInvoiceDetails(Pageable pageable);
    InvoiceDetail createInvoiceDetail(InvoiceDetailCreateRequestDTO invoiceDetailCreateRequestDTO);
    InvoiceDetail updateInvoiceDetail(InvoiceDetailUpdateRequestDTO invoiceDetailUpdateRequestDTO);
    void deleteInvoiceDetail(UUID id);
}
