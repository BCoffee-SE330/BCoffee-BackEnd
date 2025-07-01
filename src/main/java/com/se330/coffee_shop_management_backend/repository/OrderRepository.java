package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.util.Constants;
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
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch", "employee.user", "orderDetails","orderDetails.productVariant", "orderDetails.productVariant", "orderDetails.productVariant.product", "orderPayment"})
    Page<Order> findAllByUser_Id(UUID userId, Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch", "employee.user", "orderDetails","orderDetails.productVariant", "orderDetails.productVariant", "orderDetails.productVariant.product", "orderPayment"})
    Page<Order> findAllByOrderStatusAndBranch_Id(Constants.OrderStatusEnum orderStatus, UUID branchId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch", "employee.user", "orderDetails","orderDetails.productVariant", "orderDetails.productVariant", "orderDetails.productVariant.product", "orderPayment"})
    Optional<Order> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch", "employee.user", "orderDetails","orderDetails.productVariant", "orderDetails.productVariant", "orderDetails.productVariant.product", "orderPayment"})
    Page<Order> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch", "employee.user", "orderDetails","orderDetails.productVariant", "orderDetails.productVariant", "orderDetails.productVariant.product", "orderPayment"})
    Order save(Order order);

    @Override
    @EntityGraph(attributePaths = {"employee", "orderPayment", "user", "shippingAddress", "branch"})
    List<Order> findAll();
}