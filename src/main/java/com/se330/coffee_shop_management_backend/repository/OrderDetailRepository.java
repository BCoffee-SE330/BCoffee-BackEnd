package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.OrderDetail;
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
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    List<OrderDetail> findAllByOrder_Id(UUID orderId);

    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    Page<OrderDetail> findAllByOrder_Id(UUID orderId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    Optional<OrderDetail> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    Page<OrderDetail> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    OrderDetail save(OrderDetail orderDetail);

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    List<OrderDetail> findAll();

    @Override
    @EntityGraph(attributePaths = {"order", "productVariant", "order.user", "order.employee", "productVariant.product"})
    List<OrderDetail> findAllById(Iterable<UUID> ids);
}