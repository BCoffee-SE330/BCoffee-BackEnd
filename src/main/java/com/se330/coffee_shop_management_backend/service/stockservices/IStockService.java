package com.se330.coffee_shop_management_backend.service.stockservices;

import com.se330.coffee_shop_management_backend.dto.request.stock.StockCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.stock.StockUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IStockService {
    Stock findByIdStock(UUID id);
    Page<Stock> findAllStocks(Pageable pageable);
    Page<Stock> findAllStocksByWarehouseId(UUID stockId, Pageable pageable);
    Stock createStock(StockCreateRequestDTO stockCreateRequestDTO);
    Stock updateStock(StockUpdateRequestDTO stockUpdateRequestDTO);
    void deleteStock(UUID id);
}
