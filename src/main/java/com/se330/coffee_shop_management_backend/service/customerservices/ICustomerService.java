package com.se330.coffee_shop_management_backend.service.customerservices;

import com.se330.coffee_shop_management_backend.dto.response.customer.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICustomerService {
    Page<CustomerResponseDTO> findAllCustomerOfMyBranch(Pageable pageable);
    Page<CustomerResponseDTO> findAllCustomerOfBranch(Pageable pageable, UUID branchId);
}
