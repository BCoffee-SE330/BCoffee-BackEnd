package com.se330.coffee_shop_management_backend.service.inventoryservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.inventory.InventoryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.inventory.InventoryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Inventory;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.InventoryRepository;
import com.se330.coffee_shop_management_backend.service.inventoryservices.IInventoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImpInventoryService implements IInventoryService {

    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;
    private final BranchRepository branchRepository;

    public ImpInventoryService(
            InventoryRepository inventoryRepository,
            IngredientRepository ingredientRepository,
            BranchRepository branchRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory findByIdInventory(UUID id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> findAllInventories(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> findAllInventoriesByBrachId(UUID branchId, Pageable pageable) {
        return inventoryRepository.findAllByBranch_Id(branchId, pageable);
    }

    @Override
    public Page<Inventory> findAllInventoriesByBranchIdAndIngredientId(UUID branchId, UUID ingredientId, Pageable pageable) {
        return inventoryRepository.findAllByBranch_IdAndIngredient_Id(branchId, ingredientId, pageable);
    }

    @Override
    @Transactional
    public Inventory createInventory(InventoryCreateRequestDTO inventoryCreateRequestDTO) {
        Ingredient existingIngredient = ingredientRepository.findById(inventoryCreateRequestDTO.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        Branch existingBranch = branchRepository.findById(inventoryCreateRequestDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        Inventory newInventory = inventoryRepository.save(
                Inventory.builder()
                        .ingredient(existingIngredient)
                        .branch(existingBranch)
                        .inventoryQuantity(inventoryCreateRequestDTO.getInventoryQuantity())
                        .inventoryExpireDate(inventoryCreateRequestDTO.getInventoryExpireDate())
                        .build()
        );

        return findByIdInventory(newInventory.getId());
    }

    @Transactional
    @Override
    public Inventory updateInventory(InventoryUpdateRequestDTO inventoryUpdateRequestDTO) {
        Inventory existingInventory = inventoryRepository.findById(inventoryUpdateRequestDTO.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Ingredient existingIngredient = ingredientRepository.findById(inventoryUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        Branch existingBranch = branchRepository.findById(inventoryUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        if (existingInventory.getIngredient() != null) {
            existingInventory.getIngredient().getInventories().remove(existingInventory);
            existingInventory.setIngredient(existingIngredient);
            existingIngredient.getInventories().add(existingInventory);
        }

        if (existingInventory.getBranch() != null) {
            existingInventory.getBranch().getInventories().remove(existingInventory);
            existingInventory.setBranch(existingBranch);
            existingBranch.getInventories().add(existingInventory);
        }

        existingInventory.setInventoryQuantity(inventoryUpdateRequestDTO.getInventoryQuantity());
        existingInventory.setInventoryExpireDate(inventoryUpdateRequestDTO.getInventoryExpireDate());

        inventoryRepository.save(existingInventory);

        return findByIdInventory(existingInventory.getId());
    }

    @Transactional
    @Override
    public void deleteInventory(UUID id) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (existingInventory.getIngredient() != null) {
            existingInventory.getIngredient().getInventories().remove(existingInventory);
            existingInventory.setIngredient(null);
        }

        if (existingInventory.getBranch() != null) {
            existingInventory.getBranch().getInventories().remove(existingInventory);
            existingInventory.setBranch(null);
        }

        inventoryRepository.deleteById(id);
    }
}
