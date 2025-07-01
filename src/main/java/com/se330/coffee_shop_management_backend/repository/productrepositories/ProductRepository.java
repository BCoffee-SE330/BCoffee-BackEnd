package com.se330.coffee_shop_management_backend.repository.productrepositories;

import com.se330.coffee_shop_management_backend.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Override
    Page<Product> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productCategory"})
    List<Product> findAll();

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    Page<Product> findAllById(@Param("ids") Iterable<UUID> ids, Pageable pageable);

    // Case 1: Get all best-selling products (no filters)
    @Query("SELECT p FROM Product p ORDER BY COALESCE((SELECT SUM(od.orderDetailQuantity) FROM OrderDetail od WHERE od.productVariant.product = p), 0) DESC")
    List<Product> findAllBestSellingProductsList();

    // Case 2: Filter by year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE od.productVariant.product = p " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year), 0) DESC")
    List<Product> findBestSellingProductsByYear(@Param("year") int year);

    // Case 3: Filter by month and year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE od.productVariant.product = p " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM o.createdAt) = :month), 0) DESC")
    List<Product> findBestSellingProductsByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year);

    // Case 4: Filter by day, month and year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE od.productVariant.product = p " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM o.createdAt) = :month " +
            "AND EXTRACT(DAY FROM o.createdAt) = :day), 0) DESC")
    List<Product> findBestSellingProductsByDayAndMonthAndYear(
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year);

    // Case 1.2: Filter by branch
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE od.productVariant.product = p " +
            "AND b.id = :branchId), 0) DESC")
    List<Product> findBestSellingProductsByBranch(@Param("branchId") UUID branchId);

    // Case 2.2: Filter by branch and year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE od.productVariant.product = p " +
            "AND b.id = :branchId " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year), 0) DESC")
    List<Product> findBestSellingProductsByBranchAndYear(
            @Param("branchId") UUID branchId,
            @Param("year") int year);

    // Case 3.2: Filter by branch, month and year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE od.productVariant.product = p " +
            "AND b.id = :branchId " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM o.createdAt) = :month), 0) DESC")
    List<Product> findBestSellingProductsByBranchAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("month") int month,
            @Param("year") int year);

    // Case 4.2: Filter by branch, day, month and year
    @Query("SELECT p FROM Product p ORDER BY COALESCE((" +
            "SELECT SUM(od.orderDetailQuantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE od.productVariant.product = p " +
            "AND b.id = :branchId " +
            "AND EXTRACT(YEAR FROM o.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM o.createdAt) = :month " +
            "AND EXTRACT(DAY FROM o.createdAt) = :day), 0) DESC")
    List<Product> findBestSellingProductsByBranchAndDayAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year);

    Page<Product> findAllByProductCategory_Id(UUID productCategoryId, Pageable pageable);
}