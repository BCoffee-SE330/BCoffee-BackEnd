package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository  extends JpaRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {
    boolean existsByUser_Id(UUID userId);

    @EntityGraph(attributePaths = {"user", "cartDetails"})
    Cart findByUser_Id(UUID userId);

    @Override
    @EntityGraph(attributePaths = {"user", "cartDetails"})
    List<Cart> findAll();
}