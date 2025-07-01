package com.se330.coffee_shop_management_backend.service.invoiceservices;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IInvoiceService {
    Invoice findByIdInvoice(UUID id);
    Page<Invoice> findAllInvoices(Pageable pageable);
    Invoice createInvoice(InvoiceCreateRequestDTO invoiceCreateRequestDTO);
    Invoice updateInvoice(InvoiceUpdateRequestDTO invoiceUpdateRequestDTO);
    void deleteInvoice(UUID id);
}