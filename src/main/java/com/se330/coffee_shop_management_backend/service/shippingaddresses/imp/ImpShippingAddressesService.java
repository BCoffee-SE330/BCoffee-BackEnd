package com.se330.coffee_shop_management_backend.service.shippingaddresses.imp;

import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.ShippingAddressesRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.shippingaddresses.IShippingAddressesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpShippingAddressesService implements IShippingAddressesService {

    private final ShippingAddressesRepository shippingAddressesRepository;
    private final UserService userService;

    public ImpShippingAddressesService(
            ShippingAddressesRepository shippingAddressesRepository,
            UserService userService
    ) {
        this.shippingAddressesRepository = shippingAddressesRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingAddresses findByIdShippingAddresses(UUID id) {
        return shippingAddressesRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShippingAddresses> findAllShippingAddresses(Pageable pageable) {
        return shippingAddressesRepository.findAll(pageable);
    }

    @Override
    public Page<ShippingAddresses> findAllShippingAddressesByMe(Pageable pageable) {
        return shippingAddressesRepository.findAllByUser_Id(
                userService.getUser().getId(), pageable
        );
    }

    @Override
    @Transactional
    public ShippingAddresses createShippingAddresses(ShippingAddressesCreateRequestDTO shippingAddressesCreateRequestDTO) {
        User existingUser = userService.getUser();

        return shippingAddressesRepository.save(
                ShippingAddresses.builder()
                        .addressLine(shippingAddressesCreateRequestDTO.getAddressLine())
                        .addressCity(shippingAddressesCreateRequestDTO.getAddressCity())
                        .addressDistrict(shippingAddressesCreateRequestDTO.getAddressDistrict())
                        .addressIsDefault(shippingAddressesCreateRequestDTO.isAddressIsDefault())
                        .user(existingUser)
                        .build()
        );
    }

    @Transactional
    @Override
    public ShippingAddresses updateShippingAddresses(ShippingAddressesUpdateRequestDTO shippingAddressesUpdateRequestDTO) {
        ShippingAddresses existingShippingAddresses = shippingAddressesRepository.findById(shippingAddressesUpdateRequestDTO.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        User existingUser = userService.getUser();

        if (existingShippingAddresses.getUser() != null) {
            existingShippingAddresses.getUser().getShippingAddresses().remove(existingShippingAddresses);
            existingShippingAddresses.setUser(existingUser);
            existingUser.getShippingAddresses().add(existingShippingAddresses);
        }

        existingShippingAddresses.setAddressLine(shippingAddressesUpdateRequestDTO.getAddressLine());
        existingShippingAddresses.setAddressCity(shippingAddressesUpdateRequestDTO.getAddressCity());
        existingShippingAddresses.setAddressDistrict(shippingAddressesUpdateRequestDTO.getAddressDistrict());
        existingShippingAddresses.setAddressIsDefault(shippingAddressesUpdateRequestDTO.isAddressIsDefault());

        return shippingAddressesRepository.save(existingShippingAddresses);
    }

    @Override
    @Transactional
    public void deleteShippingAddresses(UUID id) {
        ShippingAddresses existingShippingAddresses = shippingAddressesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        if (existingShippingAddresses.getUser() != null) {
            existingShippingAddresses.getUser().getShippingAddresses().remove(existingShippingAddresses);
            existingShippingAddresses.setUser(null);
        }

        shippingAddressesRepository.deleteById(id);
    }
}
