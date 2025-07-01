package com.se330.coffee_shop_management_backend.service.invoiceservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.InvoiceRepository;
import com.se330.coffee_shop_management_backend.repository.StockRepository;
import com.se330.coffee_shop_management_backend.repository.SupplierRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceDetailService;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpInvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final IInvoiceDetailService invoiceDetailService;
    private final StockRepository stockRepository;
    private final INotificationService notificationService;

    public ImpInvoiceService(
            InvoiceRepository invoiceRepository,
            WarehouseRepository warehouseRepository,
            SupplierRepository supplierRepository,
            IInvoiceDetailService invoiceDetailService,
            StockRepository stockRepository,
            INotificationService notificationService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;
        this.invoiceDetailService = invoiceDetailService;
        this.stockRepository = stockRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findByIdInvoice(UUID id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Invoice createInvoice(InvoiceCreateRequestDTO invoiceCreateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(invoiceCreateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + invoiceCreateRequestDTO.getWarehouseId()));

        Supplier existingSupplier = supplierRepository.findById(invoiceCreateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + invoiceCreateRequestDTO.getSupplierId()));

        // first create a new invoice
        Invoice newInvoice = invoiceRepository.save(
                Invoice.builder()
                        .warehouse(existingWarehouse)
                        .supplier(existingSupplier)
                        .invoiceDescription(invoiceCreateRequestDTO.getInvoiceDescription())
                        .invoiceTrackingNumber(CreateTrackingNumber.createTrackingNumber("INVOICE"))
                        .invoiceTransferTotalCost(invoiceCreateRequestDTO.getInvoiceTransferTotalCost())
                        .build()
        );

        // then add invoice detail to that stock
        for (InvoiceDetailCreateRequestDTO invoiceDetailCreateRequestDTO : invoiceCreateRequestDTO.getInvoiceDetails()) {
            invoiceDetailCreateRequestDTO.setInvoiceId(newInvoice.getId());
            InvoiceDetail invoiceDetail = invoiceDetailService.createInvoiceDetail(invoiceDetailCreateRequestDTO);

            if (newInvoice.getWarehouse() != null) {
                // now find and update stock in that warehouse
                boolean stockExists = false;
                for (Stock stock : existingWarehouse.getStocks()) {
                    if (stock.getIngredient().getId().equals(invoiceDetailCreateRequestDTO.getIngredientId())) {
                        stockExists = true;
                        stock.setStockQuantity(stock.getStockQuantity() + invoiceDetailCreateRequestDTO.getInvoiceDetailQuantity());
                        stockRepository.save(stock);
                        break;
                    }
                }

                // if stock does not exist, create a new stock entry
                if (!stockExists) {
                    Stock newStock = Stock.builder()
                            .ingredient(invoiceDetail.getIngredient())
                            .warehouse(existingWarehouse)
                            .stockQuantity(invoiceDetailCreateRequestDTO.getInvoiceDetailQuantity())
                            .build();
                    stockRepository.save(newStock);
                }
            }
        }

        invoiceRepository.save(newInvoice);



        return newInvoice;
    }

    /**
     * Updates an existing invoice with new details and adjusts warehouse stock accordingly.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates and retrieves required entities (existing invoice, warehouse, supplier)</li>
     *   <li>Updates basic invoice information (supplier, warehouse, description, tracking number, cost)</li>
     *   <li>Reverts stock quantities for all existing invoice details</li>
     *   <li>Removes all existing invoice details</li>
     *   <li>Creates new invoice details based on the request</li>
     *   <li>Updates warehouse stock quantities for each ingredient</li>
     *   <li>Creates new stock entries if ingredients don't exist in the warehouse</li>
     *   <li>Returns the updated invoice with all new relationships established</li>
     * </ol>
     *
     * @param invoiceUpdateRequestDTO The data transfer object containing all information needed to update the invoice:
     *                               invoice ID, supplier ID, warehouse ID, description, tracking number,
     *                               transfer total cost, and list of invoice details with ingredients and quantities
     * @return The fully updated Invoice entity with all relationships established and stock quantities adjusted
     * @throws EntityNotFoundException If any referenced entity (invoice, warehouse, supplier) cannot be found
     */
    @Transactional
    @Override
    public Invoice updateInvoice(InvoiceUpdateRequestDTO invoiceUpdateRequestDTO) {
        Invoice existingInvoice = invoiceRepository.findById(invoiceUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + invoiceUpdateRequestDTO.getId()));

        Warehouse existingWarehouse = warehouseRepository.findById(invoiceUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + invoiceUpdateRequestDTO.getWarehouseId()));

        Supplier existingSupplier = supplierRepository.findById(invoiceUpdateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + invoiceUpdateRequestDTO.getSupplierId()));

        // Update basic invoice information
        if (existingInvoice.getSupplier() != null) {
            existingInvoice.getSupplier().getInvoices().remove(existingInvoice);
            existingInvoice.setSupplier(existingSupplier);
            existingInvoice.getSupplier().getInvoices().add(existingInvoice);
        }

        if (existingInvoice.getWarehouse() != null) {
            existingInvoice.getWarehouse().getInvoices().remove(existingInvoice);
            existingInvoice.setWarehouse(existingWarehouse);
            existingInvoice.getWarehouse().getInvoices().add(existingInvoice);
        }

        existingInvoice.setInvoiceDescription(invoiceUpdateRequestDTO.getInvoiceDescription());
        existingInvoice.setInvoiceTrackingNumber(CreateTrackingNumber.createTrackingNumber("INVOICE"));
        existingInvoice.setInvoiceTransferTotalCost(invoiceUpdateRequestDTO.getInvoiceTransferTotalCost());

        // Revert stock quantities for all existing invoice details
        for (InvoiceDetail oldDetail : existingInvoice.getInvoiceDetails()) {
            Ingredient ingredient = oldDetail.getIngredient();
            int quantityToRevert = oldDetail.getInvoiceDetailQuantity();

            // Find corresponding stock and revert quantity
            for (Stock stock : existingWarehouse.getStocks()) {
                if (stock.getIngredient().getId().equals(ingredient.getId())) {
                    stock.setStockQuantity(stock.getStockQuantity() - quantityToRevert);
                    stockRepository.save(stock);
                    break;
                }
            }

            // Delete old invoice detail
            invoiceDetailService.deleteInvoiceDetail(oldDetail.getId());
        }

        // Create new invoice details and update stock
        for (InvoiceDetailCreateRequestDTO detailDTO : invoiceUpdateRequestDTO.getInvoiceDetails()) {
            detailDTO.setInvoiceId(existingInvoice.getId());
            InvoiceDetail newDetail = invoiceDetailService.createInvoiceDetail(detailDTO);

            // Update stock with new quantities
            boolean stockExists = false;
            for (Stock stock : existingWarehouse.getStocks()) {
                if (stock.getIngredient().getId().equals(detailDTO.getIngredientId())) {
                    stockExists = true;
                    stock.setStockQuantity(stock.getStockQuantity() + detailDTO.getInvoiceDetailQuantity());
                    stockRepository.save(stock);
                    break;
                }
            }

            if (!stockExists) {
                // Create new stock if it doesn't exist
                Stock newStock = Stock.builder()
                        .ingredient(newDetail.getIngredient())
                        .warehouse(existingWarehouse)
                        .stockQuantity(detailDTO.getInvoiceDetailQuantity())
                        .build();
                stockRepository.save(newStock);
            }
        }

        return invoiceRepository.save(existingInvoice);
    }

    @Transactional
    @Override
    public void deleteInvoice(UUID id) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        if(existingInvoice.getSupplier() != null) {
            existingInvoice.getSupplier().getInvoices().remove(existingInvoice);
            existingInvoice.setSupplier(null);
        }

        if(existingInvoice.getWarehouse() != null) {
            existingInvoice.getWarehouse().getInvoices().remove(existingInvoice);
            existingInvoice.setWarehouse(null);
        }

        invoiceRepository.delete(existingInvoice);
    }
}
