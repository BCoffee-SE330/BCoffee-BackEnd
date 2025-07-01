package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.InventoryRepository;
import com.se330.coffee_shop_management_backend.repository.OrderDetailRepository;
import com.se330.coffee_shop_management_backend.repository.OrderRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ImpOrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;
    private final InventoryRepository inventoryRepository;

    public ImpOrderDetailService(
            OrderDetailRepository orderDetailRepository,
            ProductVariantRepository productVariantRepository,
            OrderRepository orderRepository,
            BranchRepository branchRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderDetailRepository = orderDetailRepository;
        this.productVariantRepository = productVariantRepository;
        this.branchRepository = branchRepository;
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetail findByIdOrderDetail(UUID id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetail> findAllByIdOrder(UUID orderId, Pageable pageable) {
        return orderDetailRepository.findAllByOrder_Id(orderId, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetail> findAllOrderDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailCreateRequestDTO orderDetailCreateRequestDTO) {
        ProductVariant existingProductVariant = productVariantRepository.findById(orderDetailCreateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + orderDetailCreateRequestDTO.getProductVariantId()));

        Order existingOrder = orderRepository.findById(orderDetailCreateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderDetailCreateRequestDTO.getOrderId()));

        Branch branch = branchRepository.findById(orderDetailCreateRequestDTO.getBranchId()).orElseThrow(
                () -> new EntityNotFoundException("Branch not found with id: " + orderDetailCreateRequestDTO.getBranchId()
        ));
        UUID branchId = branch.getId();

        // Calculate required ingredients for this order detail
        Map<UUID, Integer> requiredIngredients = new HashMap<>();
        for (Recipe recipe : existingProductVariant.getRecipes()) {
            UUID ingredientId = recipe.getIngredient().getId();
            int quantity = recipe.getRecipeQuantity() * orderDetailCreateRequestDTO.getOrderDetailQuantity();
            requiredIngredients.put(ingredientId, quantity);
        }

        // Get branch inventories sorted by expiry date
        List<Inventory> branchInventories = inventoryRepository.findAllByBranch_IdSortedByExpiredDay(branchId);

        if (branchInventories.isEmpty()) {
            throw new IllegalArgumentException("Branch has no inventory");
        }

        // Check if there's enough inventory
        Map<UUID, Integer> availableQuantities = new HashMap<>();
        for (Inventory inventory : branchInventories) {
            UUID ingredientId = inventory.getIngredient().getId();
            availableQuantities.put(ingredientId,
                    availableQuantities.getOrDefault(ingredientId, 0) + inventory.getInventoryQuantity());
        }

        // Verify we have enough of each required ingredient
        for (Map.Entry<UUID, Integer> entry : requiredIngredients.entrySet()) {
            UUID ingredientId = entry.getKey();
            int requiredQty = entry.getValue();

            if (!availableQuantities.containsKey(ingredientId) || availableQuantities.get(ingredientId) < requiredQty) {
                throw new IllegalArgumentException("Not enough inventory for ingredient with id: " + ingredientId);
            }
        }

        // Create a deep copy of requiredIngredients to track remaining quantities
        Map<UUID, Integer> remainingQuantities = new HashMap<>(requiredIngredients);

        // Deduct from inventory (earliest expiry first)
        List<Inventory> updatedInventories = new ArrayList<>();
        for (Inventory inventory : branchInventories) {
            UUID ingredientId = inventory.getIngredient().getId();

            if (remainingQuantities.containsKey(ingredientId) && remainingQuantities.get(ingredientId) > 0) {
                int requiredQty = remainingQuantities.get(ingredientId);
                int availableQty = inventory.getInventoryQuantity();

                if (availableQty >= requiredQty) {
                    // We have enough in this inventory item
                    inventory.setInventoryQuantity(availableQty - requiredQty);
                    remainingQuantities.put(ingredientId, 0);
                } else {
                    // Use all available and continue with next inventory
                    inventory.setInventoryQuantity(0);
                    remainingQuantities.put(ingredientId, requiredQty - availableQty);
                }
                updatedInventories.add(inventory);
            }
        }

        // Verify all requirements were met
        boolean allSatisfied = remainingQuantities.values().stream().allMatch(qty -> qty == 0);
        if (!allSatisfied) {
            throw new IllegalArgumentException("Not enough inventory for required ingredients");
        }

        // Save the updated inventories
        inventoryRepository.saveAll(updatedInventories);

        BigDecimal unitPrice = existingProductVariant.getVariantPrice();

        // Create and return the order detail
        return orderDetailRepository.save(
                OrderDetail.builder()
                        .orderDetailQuantity(orderDetailCreateRequestDTO.getOrderDetailQuantity())
                        .orderDetailUnitPrice(unitPrice)
                        .orderDetailDiscountCost(BigDecimal.ZERO)
                        .orderDetailUnitPriceAfterDiscount(unitPrice)
                        .productVariant(existingProductVariant)
                        .order(existingOrder)
                        .build()
        );
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(OrderDetailUpdateRequestDTO orderDetailUpdateRequestDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderDetailUpdateRequestDTO.getOrderDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Order detail not found with id: " + orderDetailUpdateRequestDTO.getOrderDetailId()));

        ProductVariant existingProductVariant = productVariantRepository.findById(orderDetailUpdateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + orderDetailUpdateRequestDTO.getProductVariantId()));

        Order existingOrder = orderRepository.findById(orderDetailUpdateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderDetailUpdateRequestDTO.getOrderId()));

        BigDecimal unitPrice = existingProductVariant.getVariantPrice();

        existingOrderDetail.setOrderDetailQuantity(orderDetailUpdateRequestDTO.getOrderDetailQuantity());
        existingOrderDetail.setOrderDetailUnitPrice(unitPrice);
        existingOrderDetail.setProductVariant(existingProductVariant);
        existingOrderDetail.setOrder(existingOrder);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(UUID id) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order detail not found with id: " + id));

        if (existingOrderDetail.getOrder() != null) {
            existingOrderDetail.getOrder().getOrderDetails().remove(existingOrderDetail);
        }

        if (existingOrderDetail.getProductVariant() != null) {
            existingOrderDetail.getProductVariant().getOrderDetails().remove(existingOrderDetail);
        }

        orderDetailRepository.delete(existingOrderDetail);
    }
}
