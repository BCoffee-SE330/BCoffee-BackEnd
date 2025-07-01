package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {

    @EntityGraph(attributePaths = {"ingredient", "branch"})
    Page<Inventory> findAllByBranch_Id(UUID branchId, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.branch.id = :branchId AND i.inventoryExpireDate >= CURRENT_TIMESTAMP ORDER BY i.inventoryExpireDate ASC")
    @EntityGraph(attributePaths = {"ingredient", "branch"})
    List<Inventory> findAllByBranch_IdSortedByExpiredDay(UUID branchId);

    @EntityGraph(attributePaths = {"ingredient", "branch"})
    List<Inventory> findAllByBranch_IdAndIngredient_Id(UUID branchId, UUID ingredientId);

    @EntityGraph(attributePaths = {"ingredient", "branch"})
    Page<Inventory> findAllByBranch_IdAndIngredient_Id(UUID branchId, UUID ingredientId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(i.inventoryQuantity), 0) FROM Inventory i WHERE i.branch.id = :branchId AND i.ingredient.id = :ingredientId AND i.inventoryExpireDate > CURRENT_TIMESTAMP")
    @EntityGraph(attributePaths = {"ingredient", "branch"})
    Integer countQuantityByBranch_IdAndIngredient_Id(UUID branchId, UUID ingredientId);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "branch"})
    Optional<Inventory> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "branch"})
    Page<Inventory> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "branch"})
    List<Inventory> findAll();
}