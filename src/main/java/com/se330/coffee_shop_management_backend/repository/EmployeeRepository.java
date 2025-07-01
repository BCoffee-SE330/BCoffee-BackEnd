package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Employee;
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
public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {

    @Override
    @EntityGraph(attributePaths = {"user", "branch", "user.role"})
    Optional<Employee> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"user", "branch", "user.role"})
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "branch", "user.role"})
    Page<Employee> findAllByBranch_Id(UUID branchId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "branch", "managedBranch"})
    List<Employee> findAll();
}