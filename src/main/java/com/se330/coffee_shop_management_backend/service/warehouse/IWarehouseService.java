package com.se330.coffee_shop_management_backend.service.warehouse;

import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IWarehouseService {
    Warehouse findByIdWarehouse(UUID id);
    Page<Warehouse> findAllWarehouses(Pageable pageable);
    Warehouse createWarehouse(WarehouseCreateRequestDTO warehouseCreateRequestDTO);
    Warehouse updateWarehouse(WarehouseUpdateRequestDTO warehouseUpdateRequestDTO);
    void deleteWarehouse(UUID id);
}
