package com.se330.coffee_shop_management_backend.service.customerservices.imp;

import com.se330.coffee_shop_management_backend.dto.response.customer.CustomerResponseDTO;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.customerservices.ICustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class ImpCustomerService implements ICustomerService {

    private final UserService userService;
    private final UserRepository userRepository;

    public ImpCustomerService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAllCustomerOfMyBranch(Pageable pageable) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        Page<User> userPage = userRepository.findAllByOrdersBranchId(branchId, pageable);
        return userPage.map(CustomerResponseDTO::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAllCustomerOfBranch(Pageable pageable, UUID branchId) {
        Page<User> userPage = userRepository.findAllByOrdersBranchId(branchId, pageable);
        return userPage.map(CustomerResponseDTO::convert);
    }
}
