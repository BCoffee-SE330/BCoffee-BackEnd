package com.se330.coffee_shop_management_backend.service.shippingaddresses;

import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IShippingAddressesService {
    ShippingAddresses findByIdShippingAddresses(UUID id);
    Page<ShippingAddresses> findAllShippingAddresses(Pageable pageable);
    Page<ShippingAddresses> findAllShippingAddressesByMe(Pageable pageable);
    ShippingAddresses createShippingAddresses(ShippingAddressesCreateRequestDTO shippingAddressesCreateRequestDTO);
    ShippingAddresses updateShippingAddresses(ShippingAddressesUpdateRequestDTO shippingAddressesUpdateRequestDTO);
    void deleteShippingAddresses(UUID id);
}
