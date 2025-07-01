package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Override
    @EntityGraph(attributePaths = {"role", "employee", "employee.branch", "orders"})
    Optional<User> findById(UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    @EntityGraph(attributePaths = {"role", "employee", "employee.branch", "orders"})
    List<User> findAllByRoleName(Constants.RoleEnum roleName);

    @Override
    @EntityGraph(attributePaths = {"role", "employee", "employee.branch", "orders"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"role", "employee", "employee.branch", "orders"})
    Page<User> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"role"})
    List<User> findAll();

    @Query("SELECT DISTINCT u FROM User u JOIN u.orders o WHERE o.branch.id = :branchId")
    @EntityGraph(attributePaths = {"role", "employee", "employee.branch", "orders"})
    Page<User> findAllByOrdersBranchId(@Param("branchId") UUID branchId, Pageable pageable);
}
