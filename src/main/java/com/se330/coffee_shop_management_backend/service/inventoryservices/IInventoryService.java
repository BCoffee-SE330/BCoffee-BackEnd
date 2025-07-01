package com.se330.coffee_shop_management_backend.service.inventoryservices;

import com.se330.coffee_shop_management_backend.dto.request.inventory.InventoryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.inventory.InventoryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IInventoryService {
    Inventory findByIdInventory(UUID id);
    Page<Inventory> findAllInventories(Pageable pageable);
    Page<Inventory> findAllInventoriesByBrachId(UUID branchId, Pageable pageable);
    Page<Inventory> findAllInventoriesByBranchIdAndIngredientId(UUID branchId, UUID ingredientId, Pageable pageable);
    Inventory createInventory(InventoryCreateRequestDTO inventoryCreateRequestDTO);
    Inventory updateInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO);
    void deleteInventory(UUID id);
}
