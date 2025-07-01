package com.se330.coffee_shop_management_backend.service.discountservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.cart.EmployeeCartRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.DiscountUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.cart.CartAndUsedDiscountResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.cart.EmployeeViewCartDiscountResponseDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.useddiscount.IUsedDiscountService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ImpDiscountService implements IDiscountService {

    private final DiscountRepository discountRepository;
    private final BranchRepository branchRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UsedDiscountRepository usedDiscountRepository;
    private final IUsedDiscountService usedDiscountService;
    private final INotificationService notificationService;
    private final UserService userService;
    private final CartRepository cartRepository;


    public ImpDiscountService(
            DiscountRepository discountRepository,
            BranchRepository branchRepository,
            ProductVariantRepository productVariantRepository,
            OrderDetailRepository orderDetailRepository,
            UserService userService,
            UsedDiscountRepository usedDiscountRepository,
            IUsedDiscountService usedDiscountService,
            INotificationService notificationService,
            CartRepository cartRepository
    ) {
        this.discountRepository = discountRepository;
        this.branchRepository = branchRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.usedDiscountRepository = usedDiscountRepository;
        this.usedDiscountService = usedDiscountService;
        this.notificationService = notificationService;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public Discount findByIdDiscount(UUID id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Discount> findAllDiscounts(Pageable pageable) {
        return discountRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Discount> findAllDiscountsByBranchId(Pageable pageable, UUID branchId) {
        Branch existingBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));

        return discountRepository.findAllByBranch(existingBranch, pageable);
    }

    @Override
    public Page<Discount> findAllDiscounts(Pageable pageable, List<String> discountIds) {
        if (discountIds == null || discountIds.isEmpty()) {
            return discountRepository.findAll(pageable);
        }
        List<UUID> discountIdUUIDs = new ArrayList<>();
        for (String id : discountIds) {
            try {
                discountIdUUIDs.add(UUID.fromString(id));
            } catch (IllegalArgumentException e) {
            }
        }
        return discountRepository.findAllByIdIn(discountIdUUIDs, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Discount> findAllDiscountsWithBeforeExpiredDate(Pageable pageable) {
        List<Discount> discounts = discountRepository.findAllActiveAndNotExpired();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), discounts.size());
        List<Discount> pagedList = discounts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, discounts.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Discount> findAllDiscountsByProductVariantId(Pageable pageable, UUID productVariantId) {
        return discountRepository.findAllByProductVariants_Id(productVariantId, pageable);
    }

    @Override
    @Transactional
    public Discount createDiscount(DiscountCreateRequestDTO discountCreateRequestDTO) {

        User manager = userService.getUser();

        Branch existingBranch = manager.getEmployee().getBranch();

        List<ProductVariant> productVariants = new ArrayList<>();
        if (discountCreateRequestDTO.getProductVariantIds() != null && !discountCreateRequestDTO.getProductVariantIds().isEmpty()) {
            productVariants = discountCreateRequestDTO.getProductVariantIds().stream()
                    .map(id -> productVariantRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + id)))
                    .toList();
        }

        Discount returnDiscount = discountRepository.save(
                Discount.builder()
                        .discountName(discountCreateRequestDTO.getDiscountName())
                        .discountDescription(discountCreateRequestDTO.getDiscountDescription())
                        .discountType(Constants.DiscountTypeEnum.valueOf(discountCreateRequestDTO.getDiscountType()))
                        .discountValue(discountCreateRequestDTO.getDiscountValue())
                        .discountCode(discountCreateRequestDTO.getDiscountCode())
                        .discountStartDate(discountCreateRequestDTO.getDiscountStartDate())
                        .discountEndDate(discountCreateRequestDTO.getDiscountEndDate())
                        .discountMaxUsers(discountCreateRequestDTO.getDiscountMaxUsers())
                        .discountUserCount(0)
                        .discountMaxPerUser(discountCreateRequestDTO.getDiscountMaxPerUser())
                        .discountMinOrderValue(discountCreateRequestDTO.getDiscountMinOrderValue())
                        .discountIsActive(discountCreateRequestDTO.isDiscountIsActive())
                        .branch(existingBranch)
                        .productVariants(productVariants)
                        .build()

        );

        for (ProductVariant productVariant : productVariants) {
            productVariant.getDiscounts().add(returnDiscount);
            productVariantRepository.save(productVariant);
        }

        // send a discount notification to the branch manager
        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.DISCOUNT)
                        .notificationContent(CreateNotiContentHelper.createDiscountForManager(returnDiscount.getDiscountName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        // if the discount is active, send a notification to every user
        if (returnDiscount.isDiscountIsActive()) {
            notificationService.sendNotificationToAllUsers(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.DISCOUNT)
                            .notificationContent(CreateNotiContentHelper.createDiscountAddedContent(
                                    returnDiscount.getDiscountName(),
                                    returnDiscount.getDiscountValue().toString(),
                                    returnDiscount.getDiscountStartDate().toString(),
                                    returnDiscount.getDiscountEndDate().toString(),
                                    returnDiscount.getBranch().getBranchName()
                            ))
                            .senderId(null)
                            .receiverId(null)
                            .isRead(false)
                            .build()
            );
        }

        return findByIdDiscount(returnDiscount.getId());
    }

    @Override
    @Transactional
    public Discount updateDiscount(DiscountUpdateRequestDTO discountUpdateRequestDTO) {
        Discount existingDiscount = discountRepository.findById(discountUpdateRequestDTO.getDiscountId())
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + discountUpdateRequestDTO.getDiscountId()));

        User manager = userService.getUser();

        existingDiscount.setDiscountName(discountUpdateRequestDTO.getDiscountName());
        existingDiscount.setDiscountDescription(discountUpdateRequestDTO.getDiscountDescription());
        existingDiscount.setDiscountType(Constants.DiscountTypeEnum.valueOf(discountUpdateRequestDTO.getDiscountType()));
        existingDiscount.setDiscountValue(discountUpdateRequestDTO.getDiscountValue());
        existingDiscount.setDiscountCode(discountUpdateRequestDTO.getDiscountCode());
        existingDiscount.setDiscountStartDate(discountUpdateRequestDTO.getDiscountStartDate());
        existingDiscount.setDiscountEndDate(discountUpdateRequestDTO.getDiscountEndDate());
        existingDiscount.setDiscountMaxUsers(discountUpdateRequestDTO.getDiscountMaxUsers());
        existingDiscount.setDiscountMaxPerUser(discountUpdateRequestDTO.getDiscountMaxPerUser());
        existingDiscount.setDiscountMinOrderValue(discountUpdateRequestDTO.getDiscountMinOrderValue());
        for (ProductVariant variant : new ArrayList<>(existingDiscount.getProductVariants())) {
            variant.getDiscounts().remove(existingDiscount);
            productVariantRepository.save(variant);
        }
        existingDiscount.getProductVariants().clear();
        if (discountUpdateRequestDTO.getProductVariantIds() != null && !discountUpdateRequestDTO.getProductVariantIds().isEmpty()) {

            List<ProductVariant> productVariants = new ArrayList<>();
            discountUpdateRequestDTO.getProductVariantIds().forEach(id -> {
                ProductVariant productVariant = productVariantRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + id));
                productVariants.add(productVariant);
                productVariant.getDiscounts().add(existingDiscount);
                productVariantRepository.save(productVariant);
            });
            existingDiscount.setProductVariants(productVariants);
        }

        if (existingDiscount.isDiscountIsActive() != discountUpdateRequestDTO.isDiscountIsActive()) {
            if (discountUpdateRequestDTO.isDiscountIsActive()) {
                notificationService.sendNotificationToAllUsers(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.DISCOUNT)
                                .notificationContent(CreateNotiContentHelper.createDiscountAddedContent(
                                        existingDiscount.getDiscountName(),
                                        existingDiscount.getDiscountValue().toString(),
                                        existingDiscount.getDiscountStartDate().toString(),
                                        existingDiscount.getDiscountEndDate().toString(),
                                        existingDiscount.getBranch().getBranchName()
                                ))
                                .senderId(null)
                                .receiverId(null)
                                .isRead(false)
                                .build()
                );
            } else {
                notificationService.sendNotificationToAllUsers(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.DISCOUNT)
                                .notificationContent(CreateNotiContentHelper.createDiscountDeletedContent(
                                        existingDiscount.getDiscountName(),
                                        existingDiscount.getBranch().getBranchName()
                                ))
                                .senderId(null)
                                .receiverId(null)
                                .isRead(false)
                                .build()
                );
            }
        }
        existingDiscount.setDiscountIsActive(discountUpdateRequestDTO.isDiscountIsActive());

        Discount updatedDiscount = discountRepository.save(existingDiscount);

        // send a notification to the branch manager about the discount update s
        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.DISCOUNT)
                        .notificationContent(CreateNotiContentHelper.updateDiscountForManager(updatedDiscount.getDiscountName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        return findByIdDiscount(existingDiscount.getId());
    }

    @Override
    @Transactional
    public void deleteDiscount(UUID discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + discountId));

        for (ProductVariant productVariant : new ArrayList<>(discount.getProductVariants())) {
            productVariant.getDiscounts().remove(discount);
            productVariantRepository.save(productVariant);
        }

        discount.getProductVariants().clear();

        discountRepository.save(discount);

        discountRepository.delete(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDiscountValid(UUID discountId, UUID productVariantId, UUID userId) {
        Discount discount = discountRepository.findById(discountId).orElse(null);
        if (discount == null) {
            return false;
        }

        if (!discount.isDiscountIsActive()) {
            return false;
        }

        if (discount.getDiscountUserCount() >= discount.getDiscountMaxUsers()) {
            return false;
        }

        // Count how many times the user has used this discount
        int timesUsed = usedDiscountRepository.sumTimesUsedByUserAndDiscount(discountId, userId);

        // Check if the user has exceeded their maximum allowed uses
        if (timesUsed >= discount.getDiscountMaxPerUser()) {
            return false;
        }

        return true;
    }

    /**
     * Applies the most valuable discount to each item in an order detail.
     *
     * <p>This method processes each unit in an order detail individually to find and apply
     * the most valuable discount available, updating the unit price accordingly.</p>
     *
     * <p>The process for each item includes:</p>
     * <ol>
     *   <li>Evaluating all available discounts for the product variant</li>
     *   <li>Checking discount validity based on order value, user limits, etc.</li>
     *   <li>Calculating the price after applying each discount (percentage or fixed amount)</li>
     *   <li>Selecting the discount that results in the lowest price</li>
     *   <li>Recording the discount usage for the user</li>
     * </ol>
     *
     * <p>After processing all items, the method calculates a new average unit price
     * based on all applied discounts and updates the order detail.</p>
     *
     * @param orderDetailId The UUID of the order detail to apply discounts to
     * @param orderTotalValue The total value of the parent order, used for minimum order value validation
     * @throws EntityNotFoundException If the specified order detail cannot be found
     */
    @Override
    @Transactional
    public void applyMostValuableDiscountOfOrderDetail(UUID orderDetailId, BigDecimal orderTotalValue) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Order Detail not found with id: " + orderDetailId));

        ProductVariant productVariant = orderDetail.getProductVariant();
        int quantity = orderDetail.getOrderDetailQuantity();
        BigDecimal totalDetailCost = BigDecimal.ZERO;

        // Track which discounts are used and how many times
        Map<UUID, Integer> appliedDiscounts = new HashMap<>();

        // Process each unit individually
        for (int i = 0; i < quantity; i++) {
            BigDecimal lowestUnitPrice = productVariant.getVariantPrice();
            Discount bestDiscount = null;

            // Find the most valuable discount for this unit
            for (Discount discount : productVariant.getDiscounts()) {
                // Skip inactive discounts or if minimum order value not met
                if (!discount.isDiscountIsActive() ||
                        orderTotalValue.compareTo(discount.getDiscountMinOrderValue()) < 0 ||
                        !discount.getBranch().getId().equals(orderDetail.getOrder().getBranch().getId())) {
                    continue;
                }

                // Skip if user has reached max uses
                if (!isDiscountValid(discount.getId(), productVariant.getId(),
                        orderDetail.getOrder().getUser().getId())) {
                    continue;
                }

                BigDecimal discountedPrice = productVariant.getVariantPrice();

                // Apply discount based on type
                if (discount.getDiscountType().name().equals(Constants.DiscountTypeEnum.PERCENTAGE.name())) {
                    BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                            .subtract(discount.getDiscountValue())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    discountedPrice = discountedPrice.multiply(discountMultiplier);
                } else {
                    discountedPrice = discountedPrice.subtract(discount.getDiscountValue());
                    if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                        discountedPrice = BigDecimal.ZERO;
                    }
                }

                // Keep track of the best discount
                if (discountedPrice.compareTo(lowestUnitPrice) < 0) {
                    lowestUnitPrice = discountedPrice;
                    bestDiscount = discount;
                }
            }

            // Add this unit's lowest price to the total detail cost
            totalDetailCost = totalDetailCost.add(lowestUnitPrice);

            // Track which discount was used
            if (bestDiscount != null) {
                appliedDiscounts.put(bestDiscount.getId(),
                        appliedDiscounts.getOrDefault(bestDiscount.getId(), 0) + 1);
            }
        }

        // Calculate the average discounted price per unit
        BigDecimal averageDiscountedPrice = quantity > 0
                ? totalDetailCost.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Calculate total discount for this order detail
        BigDecimal originalItemCost = productVariant.getVariantPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountedItemCost = averageDiscountedPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal itemDiscountAmount = originalItemCost.subtract(discountedItemCost);

        // Update the order detail with the discount information
        orderDetail.setOrderDetailDiscountCost(itemDiscountAmount);
        orderDetail.setOrderDetailUnitPriceAfterDiscount(averageDiscountedPrice);
        orderDetailRepository.save(orderDetail);

        // Create used discount records for each applied discount
        for (Map.Entry<UUID, Integer> entry : appliedDiscounts.entrySet()) {
            usedDiscountService.createUsedDiscount(new UsedDiscountCreateRequestDTO(
                    orderDetailId,
                    entry.getKey(),
                    entry.getValue()
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeViewCartDiscountResponseDTO applyDiscountToCart(EmployeeCartRequestDTO employeeCartRequestDTO) {
        Branch currentBranch = userService.getUser().getEmployee().getBranch();
        User targetUser = null;

        // Get the user for whom discounts will be checked (either from request or current employee)
        if (employeeCartRequestDTO.getUserId() != null) {
            targetUser = userService.findById(employeeCartRequestDTO.getUserId());
        }
        // If no user ID was provided, use the employee as the target
        if (targetUser == null) {
            targetUser = userService.getUser();
        }

        // Calculate initial total cart value first
        BigDecimal originalTotalValue = BigDecimal.ZERO;
        for (CartDetailCreateRequestDTO cartDetail : employeeCartRequestDTO.getCartDetails()) {
            ProductVariant productVariant = productVariantRepository.findById(cartDetail.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + cartDetail.getVariantId()));
            BigDecimal unitPrice = productVariant.getVariantPrice();
            originalTotalValue = originalTotalValue.add(unitPrice.multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
        }

        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        // Calculate discount for each cart detail
        for (CartDetailCreateRequestDTO cartDetail : employeeCartRequestDTO.getCartDetails()) {
            ProductVariant productVariant = productVariantRepository.findById(cartDetail.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + cartDetail.getVariantId()));
            int quantity = cartDetail.getCartDetailQuantity();
            BigDecimal totalDetailCost = BigDecimal.ZERO;

            // Process each unit individually
            for (int i = 0; i < quantity; i++) {
                BigDecimal lowestUnitPrice = productVariant.getVariantPrice();



                // Find the most valuable discount for this unit
                for (Discount discount : productVariant.getDiscounts()) {
                    // Skip inactive discounts or if minimum order value not met

                    UUID discountBranchId = discount.getBranch().getId();

                    if (!discount.isDiscountIsActive() ||
                            originalTotalValue.compareTo(discount.getDiscountMinOrderValue()) < 0 ||
                            !discount.getBranch().getId().equals(currentBranch.getId())) {
                        continue;
                    }

                    if (!isDiscountValid(discount.getId(), productVariant.getId(), targetUser.getId())) {
                        continue;
                    }

                    BigDecimal discountedPrice = productVariant.getVariantPrice();

                    // Apply discount based on type
                    if (discount.getDiscountType().name().equals(Constants.DiscountTypeEnum.PERCENTAGE.name())) {
                        BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                                .subtract(discount.getDiscountValue())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        discountedPrice = discountedPrice.multiply(discountMultiplier);
                    } else {
                        discountedPrice = discountedPrice.subtract(discount.getDiscountValue());
                        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                            discountedPrice = BigDecimal.ZERO;
                        }
                    }

                    // Keep track of the best discount
                    if (discountedPrice.compareTo(lowestUnitPrice) < 0) {
                        lowestUnitPrice = discountedPrice;
                    }
                }

                // Add this unit's lowest price to the total detail cost
                totalDetailCost = totalDetailCost.add(lowestUnitPrice);
            }

            // Calculate the average discounted price per unit
            BigDecimal averageDiscountedPrice = quantity > 0
                    ? totalDetailCost.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            // Calculate total discount for this cart detail
            BigDecimal originalItemCost = productVariant.getVariantPrice().multiply(BigDecimal.valueOf(quantity));
            BigDecimal discountedItemCost = averageDiscountedPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal itemDiscountAmount = originalItemCost.subtract(discountedItemCost);

            // Add to total discount amount
            totalDiscountAmount = totalDiscountAmount.add(itemDiscountAmount);
        }

        // Calculate final price after all discounts
        BigDecimal totalAfterDiscount = originalTotalValue.subtract(totalDiscountAmount);

        // Return DTO with calculated values
        return EmployeeViewCartDiscountResponseDTO.builder()
                .cartTotalCost(originalTotalValue)
                .cartDiscountCost(totalDiscountAmount)
                .cartTotalCostAfterDiscount(totalAfterDiscount)
                .build();
    }

    @Override
    @Transactional
    public CartAndUsedDiscountResponseDTO applyDiscountToCart(UUID branchId) {
        User user = userService.getUser();
        Cart existingCart = cartRepository.findByUser_Id(user.getId());

        List<UUID> appliedDiscounts = new ArrayList<>();

        // Calculate the initial total cart value
        BigDecimal originalTotalValue = BigDecimal.ZERO;
        for (CartDetail cartDetail : existingCart.getCartDetails()) {
            BigDecimal unitPrice = cartDetail.getProductVariant().getVariantPrice();
            originalTotalValue = originalTotalValue.add(unitPrice.multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
        }

        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        // Calculate discount for each cart detail
        for (CartDetail cartDetail : existingCart.getCartDetails()) {
            ProductVariant productVariant = cartDetail.getProductVariant();
            int quantity = cartDetail.getCartDetailQuantity();
            BigDecimal totalDetailCost = BigDecimal.ZERO;

            // Process each unit individually
            for (int i = 0; i < quantity; i++) {
                BigDecimal lowestUnitPrice = productVariant.getVariantPrice();
                Discount bestDiscount = null;

                // Find the most valuable discount for this unit
                for (Discount discount : productVariant.getDiscounts()) {
                    // Skip inactive discounts or if minimum order value not met

                    Branch discountBranch = discount.getBranch();

                    if (!discount.isDiscountIsActive() ||
                            originalTotalValue.compareTo(discount.getDiscountMinOrderValue()) < 0 ||
                            !discount.getBranch().getId().equals(branchId)) {
                        continue;
                    }

                    if (!isDiscountValid(discount.getId(), productVariant.getId(), user.getId())) {
                        continue;
                    }

                    BigDecimal discountedPrice = productVariant.getVariantPrice();

                    // Apply discount based on type
                    if (discount.getDiscountType().name().equals(Constants.DiscountTypeEnum.PERCENTAGE.name())) {
                        BigDecimal discountMultiplier = BigDecimal.valueOf(100)
                                .subtract(discount.getDiscountValue())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        discountedPrice = discountedPrice.multiply(discountMultiplier);
                    } else {
                        discountedPrice = discountedPrice.subtract(discount.getDiscountValue());
                        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                            discountedPrice = BigDecimal.ZERO;
                        }
                    }

                    // Keep track of the best discount
                    if (discountedPrice.compareTo(lowestUnitPrice) < 0) {
                        lowestUnitPrice = discountedPrice;
                        bestDiscount = discount;
                    }
                }

                if (bestDiscount != null) {
                    appliedDiscounts.add(bestDiscount.getId());
                }

                // Add this unit's lowest price to the total detail cost
                totalDetailCost = totalDetailCost.add(lowestUnitPrice);
            }

            // Calculate the average discounted price per unit
            BigDecimal averageDiscountedPrice = quantity > 0
                    ? totalDetailCost.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            // Calculate total discount for this cart detail
            BigDecimal originalItemCost = productVariant.getVariantPrice().multiply(BigDecimal.valueOf(quantity));
            BigDecimal discountedItemCost = averageDiscountedPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal itemDiscountAmount = originalItemCost.subtract(discountedItemCost);

            // Update cart detail with discount information
            cartDetail.setCartDetailUnitPriceAfterDiscount(averageDiscountedPrice);
            cartDetail.setCartDetailDiscountCost(itemDiscountAmount);

            // Add to total discount
            totalDiscountAmount = totalDiscountAmount.add(itemDiscountAmount);
        }

        // Update cart totals
        BigDecimal totalAfterDiscount = originalTotalValue.subtract(totalDiscountAmount);
        existingCart.setCartTotalCost(originalTotalValue);
        existingCart.setCartDiscountCost(totalDiscountAmount);
        existingCart.setCartTotalCostAfterDiscount(totalAfterDiscount);

        // Save updated cart
        cartRepository.save(existingCart);

        Cart cart = cartRepository.findByUser_Id(user.getId());

        return CartAndUsedDiscountResponseDTO.convert(cart, appliedDiscounts);
    }
}
