package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, UUID>, JpaSpecificationExecutor<OrderPayment> {
    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    Page<OrderPayment> findAllByOrder_User_Id(UUID orderUserId, Pageable pageable);

    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    OrderPayment findByTransactionId(String transactionId);

    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    OrderPayment findByOrder_Id(UUID orderId);

    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    OrderPayment findByPaypalPaymentId(String paypalPaymentId);

    @Override
    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    Page<OrderPayment> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    java.util.Optional<OrderPayment> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    OrderPayment save(OrderPayment orderPayment);

    @Override
    @EntityGraph(attributePaths = {"order", "paymentMethod"})
    List<OrderPayment> findAll();
}