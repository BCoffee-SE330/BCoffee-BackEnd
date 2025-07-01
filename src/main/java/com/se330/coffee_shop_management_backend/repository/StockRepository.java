package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    @EntityGraph(attributePaths = {"ingredient", "warehouse"})
    Page<Stock> findAllByWarehouse_Id(UUID warehouseId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "warehouse"})
    Stock save(Stock stock);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "warehouse"})
    Page<Stock> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"ingredient", "warehouse"})
    List<Stock> findAll();

    @Override
    @EntityGraph(attributePaths = {"ingredient", "warehouse"})
    Optional<Stock> findById(UUID id);
}