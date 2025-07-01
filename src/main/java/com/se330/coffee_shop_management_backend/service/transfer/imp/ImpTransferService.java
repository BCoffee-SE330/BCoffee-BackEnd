package com.se330.coffee_shop_management_backend.service.transfer.imp;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.StockRepository;
import com.se330.coffee_shop_management_backend.repository.TransferRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferDetailService;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferService;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImpTransferService implements ITransferService {

    private final TransferRepository transferRepository;
    private final WarehouseRepository warehouseRepository;
    private final BranchRepository branchRepository;
    private final ITransferDetailService transferDetailService;
    private final StockRepository stockRepository;

    public ImpTransferService(
            TransferRepository transferRepository,
            WarehouseRepository warehouseRepository,
            BranchRepository branchRepository,
            ITransferDetailService transferDetailService,
            StockRepository stockRepository
    ) {
        this.transferRepository = transferRepository;
        this.warehouseRepository = warehouseRepository;
        this.branchRepository = branchRepository;
        this.transferDetailService = transferDetailService;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Transfer findByIdTransfer(UUID id) {
        return transferRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transfer> findAllTransfers(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Transfer createTransfer(TransferCreateRequestDTO transferCreateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(transferCreateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + transferCreateRequestDTO.getWarehouseId()));

        Branch existingBranch = branchRepository.findById(transferCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + transferCreateRequestDTO.getBranchId()));

        Transfer newTransfer = transferRepository.save(
                Transfer.builder()
                        .warehouse(existingWarehouse)
                        .branch(existingBranch)
                        .transferDescription(transferCreateRequestDTO.getTransferDescription())
                        .transferTrackingNumber(CreateTrackingNumber.createTrackingNumber("TRANSFER"))
                        .transferTotalCost(transferCreateRequestDTO.getTransferTotalCost())
                        .build()
        );

        // now create transfer details
        for (TransferDetailCreateRequestDTO transferDetailCreateRequestDTO : transferCreateRequestDTO.getTransferDetails()) {
            transferDetailCreateRequestDTO.setTransferId(newTransfer.getId());
            transferDetailCreateRequestDTO.setBranchId(newTransfer.getBranch().getId());
            transferDetailService.createTransferDetail(transferDetailCreateRequestDTO);

            // then remove all inventory items that are in stock of the warehouse
            for (Stock stock : existingWarehouse.getStocks()) {
                if (stock.getIngredient().getId().equals(transferDetailCreateRequestDTO.getIngredientId())) {
                    int stockQuantityNew = stock.getStockQuantity() - transferDetailCreateRequestDTO.getTransferDetailQuantity();
                    if (stockQuantityNew < 0) {
                        throw new IllegalArgumentException("Cannot get stock for ingredient: " + stock.getIngredient().getIngredientName() + " with quantity: " + transferDetailCreateRequestDTO.getTransferDetailQuantity() + "current stock: " + stock.getStockQuantity());
                    }
                    stock.setStockQuantity(stockQuantityNew);
                    stockRepository.save(stock);
                    break;
                }
            }
        }

        return transferRepository.save(newTransfer);
    }

    @Transactional
    @Override
    public Transfer updateTransfer(TransferUpdateRequestDTO transferUpdateRequestDTO) {
        Transfer existingTransfer = transferRepository.findById(transferUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + transferUpdateRequestDTO.getId()));

        Warehouse existingWarehouse = warehouseRepository.findById(transferUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + transferUpdateRequestDTO.getWarehouseId()));

        Branch existingBranch = branchRepository.findById(transferUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + transferUpdateRequestDTO.getBranchId()));

        if (existingTransfer.getWarehouse() != null) {
            existingTransfer.getWarehouse().getTransfers().remove(existingTransfer);
            existingTransfer.setWarehouse(existingWarehouse);
            existingWarehouse.getTransfers().add(existingTransfer);
        }

        if (existingTransfer.getBranch() != null) {
            existingTransfer.getBranch().getTransfers().remove(existingTransfer);
            existingTransfer.setBranch(existingBranch);
            existingBranch.getTransfers().add(existingTransfer);
        }

        existingTransfer.setTransferDescription(transferUpdateRequestDTO.getTransferDescription());
        existingTransfer.setTransferTrackingNumber(CreateTrackingNumber.createTrackingNumber("TRANSFER"));
        existingTransfer.setTransferTotalCost(transferUpdateRequestDTO.getTransferTotalCost());

        // Handle transfer details update - delete all existing details and restore stock
        List<TransferDetail> existingTransferDetails = new ArrayList<>(existingTransfer.getTransferDetails());
        for (TransferDetail detail : existingTransferDetails) {
            // Restore stock for the ingredient in the warehouse
            for (Stock stock : existingWarehouse.getStocks()) {
                if (stock.getIngredient().getId().equals(detail.getIngredient().getId())) {
                    // Add the quantity back to stock
                    int stockQuantityNew = stock.getStockQuantity() + detail.getTransferDetailQuantity();
                    stock.setStockQuantity(stockQuantityNew);
                    stockRepository.save(stock);
                    break;
                }
            }

            transferDetailService.deleteTransferDetail(detail.getId());
        }

        // Create new transfer details from the update request
        for (TransferDetailCreateRequestDTO transferDetailCreateRequestDTO : transferUpdateRequestDTO.getTransferDetails()) {
            transferDetailCreateRequestDTO.setTransferId(existingTransfer.getId());
            transferDetailCreateRequestDTO.setBranchId(existingTransfer.getBranch().getId());
            transferDetailService.createTransferDetail(transferDetailCreateRequestDTO);

            // Update warehouse stock for the new transfer details
            for (Stock stock : existingWarehouse.getStocks()) {
                if (stock.getIngredient().getId().equals(transferDetailCreateRequestDTO.getIngredientId())) {
                    int stockQuantityNew = stock.getStockQuantity() - transferDetailCreateRequestDTO.getTransferDetailQuantity();
                    if (stockQuantityNew < 0) {
                        throw new IllegalArgumentException("Insufficient stock for ingredient: " + stock.getIngredient().getIngredientName());
                    }
                    stock.setStockQuantity(stockQuantityNew);
                    stockRepository.save(stock);
                    break;
                }
            }
        }

        return transferRepository.save(existingTransfer);
    }

    @Transactional
    @Override
    public void deleteTransfer(UUID id) {
        Transfer existingTransfer = transferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transfer not found with ID: " + id));

        if(existingTransfer.getWarehouse() != null) {
            existingTransfer.getWarehouse().getTransfers().remove(existingTransfer);
            existingTransfer.setWarehouse(null);
        }

        if(existingTransfer.getBranch() != null) {
            existingTransfer.getBranch().getTransfers().remove(existingTransfer);
            existingTransfer.setBranch(null);
        }

        transferRepository.delete(existingTransfer);
    }
}
