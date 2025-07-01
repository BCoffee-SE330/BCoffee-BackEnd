package com.se330.coffee_shop_management_backend.service.stockservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.stock.StockCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.stock.StockUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Stock;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.StockRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.stockservices.IStockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpStockService implements IStockService {

    private final StockRepository stockRepository;
    private final IngredientRepository ingredientRepository;
    private final WarehouseRepository warehouseRepository;

    public ImpStockService(
            StockRepository stockRepository,
            IngredientRepository ingredientRepository,
            WarehouseRepository warehouseRepository
    ) {
        this.stockRepository = stockRepository;
        this.ingredientRepository = ingredientRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Stock findByIdStock(UUID id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Stock> findAllStocks(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Stock> findAllStocksByWarehouseId(UUID warehouseId, Pageable pageable) {
        return stockRepository.findAllByWarehouse_Id(warehouseId, pageable);
    }

    @Override
    @Transactional
    public Stock createStock(StockCreateRequestDTO stockCreateRequestDTO) {
        Ingredient existingIngredient = ingredientRepository.findById(stockCreateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + stockCreateRequestDTO.getIngredientId()));

        Warehouse existingWarehouse = warehouseRepository.findById(stockCreateRequestDTO.getWarehouseId()).orElse(null);

        return stockRepository.save(
                Stock.builder()
                        .stockQuantity(stockCreateRequestDTO.getStockQuantity())
                        .stockUnit(stockCreateRequestDTO.getStockUnit())
                        .ingredient(existingIngredient)
                        .warehouse(existingWarehouse)
                        .build()
        );
    }

    @Transactional
    @Override
    public Stock updateStock(StockUpdateRequestDTO stockUpdateRequestDTO) {
        Stock existingStock = stockRepository.findById(stockUpdateRequestDTO.getStockId())
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: " + stockUpdateRequestDTO.getStockId()));

        Ingredient existingIngredient = ingredientRepository.findById(stockUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with ID: " + stockUpdateRequestDTO.getIngredientId()));

        Warehouse existingWarehouse = warehouseRepository.findById(stockUpdateRequestDTO.getWarehouseId()).orElse(null);

        existingStock.setStockQuantity(stockUpdateRequestDTO.getStockQuantity());
        existingStock.setStockUnit(stockUpdateRequestDTO.getStockUnit());

        if (existingStock.getIngredient() != null) {
            existingStock.getIngredient().getStocks().remove(existingStock);
            existingStock.setIngredient(existingIngredient);
            existingIngredient.getStocks().add(existingStock);
        }

        if (existingStock.getWarehouse() != null) {
            existingStock.getWarehouse().getStocks().remove(existingStock);
            existingStock.setWarehouse(existingWarehouse);
            existingWarehouse.getStocks().add(existingStock);
        }

        return stockRepository.save(existingStock);
    }

    @Transactional
    @Override
    public void deleteStock(UUID id) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: " + id));

        if (existingStock.getIngredient() != null) {
            existingStock.getIngredient().getStocks().remove(existingStock);
            existingStock.setIngredient(null);
        }

        if (existingStock.getWarehouse() != null) {
            existingStock.getWarehouse().getStocks().remove(existingStock);
            existingStock.setWarehouse(null);
        }

        stockRepository.delete(existingStock);
    }
}
