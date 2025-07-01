package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, UUID>, JpaSpecificationExecutor<CartDetail> {
    void deleteAllByCart_Id(UUID cartId);
    @EntityGraph(attributePaths = {"productVariant", "productVariant.product"})
    CartDetail findByCart_IdAndProductVariant_Id(UUID cartId, UUID productVariantId);
    @EntityGraph(attributePaths = {"productVariant", "productVariant.product"})
    Page<CartDetail> findAllByCart_User_Id(UUID cartUserId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productVariant", "productVariant.product", "cart"})
    List<CartDetail> findAll();
}
