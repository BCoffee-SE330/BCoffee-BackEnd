package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.entity.Role;
import com.se330.coffee_shop_management_backend.exception.NotFoundException;
import com.se330.coffee_shop_management_backend.repository.RoleRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final MessageSourceService messageSourceService;

    /**
     * Count roles.
     *
     * @return Long
     */
    @Transactional
    public long count() {
        return roleRepository.count();
    }

    /**
     * Find by role name.
     *
     * @param name Constants.RoleEnum
     * @return Role
     */
    @Transactional
    public Role findByName(final Constants.RoleEnum name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("role_not_found")));
    }

    /**
     * Create role.
     *
     * @param role Role
     * @return Role
     */
    @Transactional
    public Role create(final Role role) {
        return roleRepository.save(role);
    }

    /**
     * Save list roles.
     *
     * @param roleList List
     * @return List
     */
    @Transactional
    public List<Role> saveList(List<Role> roleList) {
        return roleRepository.saveAll(roleList);
    }
}
