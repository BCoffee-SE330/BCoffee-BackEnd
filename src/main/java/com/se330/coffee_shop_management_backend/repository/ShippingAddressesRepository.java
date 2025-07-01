package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.entity.User;
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
public interface ShippingAddressesRepository extends JpaRepository<ShippingAddresses, UUID>, JpaSpecificationExecutor<ShippingAddresses> {
    @EntityGraph(attributePaths = {"user"})
    List<ShippingAddresses> id(UUID id);

    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<ShippingAddresses> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user"})
    List<ShippingAddresses> findAll();

    @Override
    @EntityGraph(attributePaths = {"user"})
    ShippingAddresses save(ShippingAddresses shippingAddresses);

    @EntityGraph(attributePaths = {"user"})
    @Override
    Optional<ShippingAddresses> findById(UUID id);
    @EntityGraph(attributePaths = {"user"})
    Page<ShippingAddresses> findAllByUser_Id(UUID userId, Pageable pageable);
}
