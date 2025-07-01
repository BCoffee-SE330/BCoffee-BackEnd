package com.se330.coffee_shop_management_backend.service.invoiceservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Invoice;
import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.InvoiceDetailRepository;
import com.se330.coffee_shop_management_backend.repository.InvoiceRepository;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpInvoiceDetailService implements IInvoiceDetailService {

    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final IngredientRepository ingredientRepository;

    public ImpInvoiceDetailService(
            InvoiceDetailRepository invoiceDetailRepository,
            InvoiceRepository invoiceRepository,
            IngredientRepository ingredientRepository
    ) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceRepository = invoiceRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDetail findByIdInvoiceDetail(UUID id) {
        return invoiceDetailRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDetail> findAllInvoiceDetails(Pageable pageable) {
        return invoiceDetailRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public InvoiceDetail createInvoiceDetail(InvoiceDetailCreateRequestDTO invoiceDetailCreateRequestDTO) {
        Invoice existingInvoice = invoiceRepository.findById(invoiceDetailCreateRequestDTO.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with ID: " + invoiceDetailCreateRequestDTO.getInvoiceId()));

        Ingredient existingIngredient = ingredientRepository.findById(invoiceDetailCreateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + invoiceDetailCreateRequestDTO.getIngredientId()));

        return invoiceDetailRepository.save(
                InvoiceDetail.builder()
                        .invoice(existingInvoice)
                        .ingredient(existingIngredient)
                        .invoiceDetailQuantity(invoiceDetailCreateRequestDTO.getInvoiceDetailQuantity())
                        .invoiceDetailUnit(invoiceDetailCreateRequestDTO.getInvoiceDetailUnit())
                        .build()
        );
    }

    @Transactional
    @Override
    public InvoiceDetail updateInvoiceDetail(InvoiceDetailUpdateRequestDTO invoiceDetailUpdateRequestDTO) {

        InvoiceDetail existingInvoiceDetail = invoiceDetailRepository.findById(invoiceDetailUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("InvoiceDetail not found with ID: " + invoiceDetailUpdateRequestDTO.getId()));

        Invoice existingInvoice = invoiceRepository.findById(invoiceDetailUpdateRequestDTO.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with ID: " + invoiceDetailUpdateRequestDTO.getInvoiceId()));

        Ingredient existingIngredient = ingredientRepository.findById(invoiceDetailUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + invoiceDetailUpdateRequestDTO.getIngredientId()));

        if (existingInvoiceDetail.getInvoice() != null) {
            existingInvoiceDetail.getInvoice().getInvoiceDetails().remove(existingInvoiceDetail);
            existingInvoiceDetail.setInvoice(existingInvoice);
            existingInvoice.getInvoiceDetails().add(existingInvoiceDetail);
        }

        if (existingInvoiceDetail.getIngredient() != null) {
            existingInvoiceDetail.getIngredient().getInvoiceDetails().remove(existingInvoiceDetail);
            existingInvoiceDetail.setIngredient(existingIngredient);
            existingIngredient.getInvoiceDetails().add(existingInvoiceDetail);
        }

        existingInvoiceDetail.setInvoiceDetailQuantity(invoiceDetailUpdateRequestDTO.getInvoiceDetailQuantity());
        existingInvoiceDetail.setInvoiceDetailUnit(invoiceDetailUpdateRequestDTO.getInvoiceDetailUnit());

        return invoiceDetailRepository.save(existingInvoiceDetail);
    }

    @Transactional
    @Override
    public void deleteInvoiceDetail(UUID id) {
        InvoiceDetail existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InvoiceDetail not found with ID: " + id));

        if(existingInvoiceDetail.getInvoice() != null) {
            existingInvoiceDetail.getInvoice().getInvoiceDetails().remove(existingInvoiceDetail);
            existingInvoiceDetail.setInvoice(null);
        }

        if(existingInvoiceDetail.getIngredient() != null) {
            existingInvoiceDetail.getIngredient().getInvoiceDetails().remove(existingInvoiceDetail);
            existingInvoiceDetail.setIngredient(null);
        }

        invoiceDetailRepository.delete(existingInvoiceDetail);
    }
}
