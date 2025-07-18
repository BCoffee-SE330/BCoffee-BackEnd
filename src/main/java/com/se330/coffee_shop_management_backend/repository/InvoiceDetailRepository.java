package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
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
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, UUID>, JpaSpecificationExecutor<InvoiceDetail> {
    @Override
    @EntityGraph(attributePaths = {"invoice", "ingredient"})
    Optional<InvoiceDetail> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"invoice", "ingredient"})
    Page<InvoiceDetail> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"invoice", "ingredient"})
    InvoiceDetail save(InvoiceDetail invoiceDetail);

    @Override
    @EntityGraph(attributePaths = {"invoice", "ingredient"})
    List<InvoiceDetail> findAll();
}