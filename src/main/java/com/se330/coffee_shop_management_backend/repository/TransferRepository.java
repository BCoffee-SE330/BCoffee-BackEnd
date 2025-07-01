package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Transfer;
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
public interface TransferRepository extends JpaRepository<Transfer, UUID>, JpaSpecificationExecutor<Transfer> {
    @Override
    @EntityGraph(attributePaths = {"branch", "warehouse"})
    Transfer save(Transfer transfer);

    @Override
    @EntityGraph(attributePaths = {"branch", "warehouse"})
    Optional<Transfer> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"branch", "warehouse"})
    Page<Transfer> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"branch", "warehouse"})
    List<Transfer> findAll();
}