package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.TransferDetail;
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
public interface TransferDetailRepository extends JpaRepository<TransferDetail, UUID>, JpaSpecificationExecutor<TransferDetail> {
    @Override
    @EntityGraph(attributePaths = {"transfer", "ingredient"})
    TransferDetail save(TransferDetail transferDetail);

    @Override
    @EntityGraph(attributePaths = {"transfer", "ingredient"})
    Optional<TransferDetail> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"transfer", "ingredient"})
    Page<TransferDetail> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"transfer", "ingredient"})
    List<TransferDetail> findAll();
}