package com.se330.coffee_shop_management_backend.service.cartservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImpCartService implements ICartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final BranchRepository branchRepository;
    private final InventoryRepository inventoryRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<Cart> getAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUserId() {
        UUID userId = userService.getUser().getId();
        if (cartRepository.existsByUser_Id(userId)) {
            return cartRepository.findByUser_Id(userId);
        } else {
            return cartRepository.save(
                    Cart.builder()
                            .user(userRepository.findById(userId).orElseThrow(
                                    () -> new IllegalArgumentException("User not found with ID: " + userId)
                            ))
                            .cartDiscountCost(BigDecimal.ZERO)
                            .cartTotalCost(BigDecimal.ZERO)
                            .cartTotalCostAfterDiscount(BigDecimal.ZERO)
                            .build()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDetail> getAllCartDetailsByUserId(Pageable pageable) {
        UUID userId = userService.getUser().getId();
        return cartDetailRepository.findAllByCart_User_Id(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UUID> findBranchesWithSufficientInventory(Pageable pageable) {
        UUID userId = userService.getUser().getId();
        Cart existingCart = cartRepository.findByUser_Id(userId);
        if (existingCart == null || existingCart.getCartDetails().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<CartDetail> cartDetails = existingCart.getCartDetails();

        Map<UUID, Integer> requiredIngredients = new HashMap<>();

        for (CartDetail cartDetail : cartDetails) {
            ProductVariant variant = cartDetail.getProductVariant();
            for (Recipe recipe : variant.getRecipes()) {
                UUID ingredientId = recipe.getIngredient().getId();
                int quantity = recipe.getRecipeQuantity() * cartDetail.getCartDetailQuantity();

                requiredIngredients.merge(ingredientId, quantity, Integer::sum);
            }
        }

        List<Branch> branches = branchRepository.findAll();
        List<UUID> sufficientBranchIds = new ArrayList<>();

        for (Branch branch : branches) {
            boolean isInventorySufficient = true;

            for (Map.Entry<UUID, Integer> entry : requiredIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                Integer requiredQuantity = entry.getValue();

                Integer availableQuantity = inventoryRepository.countQuantityByBranch_IdAndIngredient_Id(branch.getId(), ingredientId);

                if (availableQuantity < requiredQuantity) {
                    isInventorySufficient = false;
                    break;
                }
            }

            if (isInventorySufficient) {
                sufficientBranchIds.add(branch.getId());
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sufficientBranchIds.size());

        if (start >= sufficientBranchIds.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, sufficientBranchIds.size());
        }

        List<UUID> pageContent = sufficientBranchIds.subList(start, end);
        return new PageImpl<>(pageContent, pageable, sufficientBranchIds.size());
    }

    @Override
    @Transactional
    public Cart addCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO) {
        Cart existingCart = null;
        UUID userId = userService.getUser().getId();

        if (!cartRepository.existsByUser_Id(userId))
            existingCart = cartRepository.save(
                    Cart.builder()
                            .user(userRepository.findById(userId).orElseThrow(
                                    () -> new IllegalArgumentException("User not found with ID: " + userId)
                            ))
                            .cartDiscountCost(BigDecimal.ZERO)
                            .cartTotalCost(BigDecimal.ZERO)
                            .cartTotalCostAfterDiscount(BigDecimal.ZERO)
                            .build());
        else
            existingCart = cartRepository.findByUser_Id(userId);

        ProductVariant existingProductVariant = productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        BigDecimal unitPrice = existingProductVariant.getVariantPrice();

        // Kiểm tra xem CartDetail đã tồn tại trong Cart chưa
        CartDetail existingCartDetail = cartDetailRepository.findByCart_IdAndProductVariant_Id(existingCart.getId(), existingProductVariant.getId());
        CartDetail newCartDetail;
        if (existingCartDetail != null) {
            existingCartDetail.setCartDetailQuantity(existingCartDetail.getCartDetailQuantity() + cartDetailCreateRequestDTO.getCartDetailQuantity());
            existingCartDetail.setCartDetailUnitPrice(unitPrice);
            existingCartDetail.setCartDetailUnitPriceAfterDiscount(unitPrice);
            existingCartDetail.setCartDetailDiscountCost(BigDecimal.ZERO);
            newCartDetail = cartDetailRepository.save(existingCartDetail);
        } else {
            newCartDetail = CartDetail.builder()
                    .cart(existingCart)
                    .productVariant(existingProductVariant)
                    .cartDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity())
                    .cartDetailUnitPrice(unitPrice)
                    .cartDetailDiscountCost(BigDecimal.ZERO)
                    .cartDetailUnitPriceAfterDiscount(unitPrice)
                    .build();
        }

        // Lưu CartDetail vào database
        newCartDetail = cartDetailRepository.save(newCartDetail);

        // Thêm CartDetail vào danh sách trong Cart (quan trọng)
        existingCart.getCartDetails().add(newCartDetail);

        calculateCartTotalCost(existingCart);

        // Lưu Cart sau khi cập nhật
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public Cart addProductVariantToCart(UUID variantId) {
        return addCartDetail(
                CartDetailCreateRequestDTO.builder()
                        .variantId(variantId)
                        .cartDetailQuantity(1)
                        .build()
        );
    }

    @Override
    @Transactional
    public Cart removeProductVariantFromCart(UUID productVariantId) {
        UUID userId = userService.getUser().getId();
        Cart existingCart = cartRepository.findByUser_Id(userId);

        if (existingCart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }

        CartDetail existingCartDetail = cartDetailRepository.findByCart_IdAndProductVariant_Id(existingCart.getId(), productVariantId);

        if (existingCartDetail == null) {
            throw new IllegalArgumentException("Cart detail not found for product variant ID: " + productVariantId);
        }

        if (existingCartDetail.getCartDetailQuantity() > 1) {
            existingCartDetail.setCartDetailQuantity(existingCartDetail.getCartDetailQuantity() - 1);
            cartDetailRepository.save(existingCartDetail);
        } else {
            existingCart.getCartDetails().remove(existingCartDetail);
            cartDetailRepository.delete(existingCartDetail);
        }

        calculateCartTotalCost(existingCart);

        return cartRepository.findById(existingCart.getId()).orElseThrow();
    }

    @Override
    @Transactional
    public Cart removeAllWithSpecificVariant(UUID variantId) {
        UUID userId = userService.getUser().getId();
        Cart existingCart = cartRepository.findByUser_Id(userId);

        if (existingCart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }

        CartDetail existingCartDetail = cartDetailRepository.findByCart_IdAndProductVariant_Id(existingCart.getId(), variantId);

        if (existingCartDetail == null) {
            throw new IllegalArgumentException("Cart detail not found for product variant ID: " + variantId);
        }

        existingCart.getCartDetails().remove(existingCartDetail);

        cartDetailRepository.delete(existingCartDetail);

        calculateCartTotalCost(existingCart);

        return cartRepository.findById(existingCart.getId()).orElseThrow();
    }

    @Override
    @Transactional
    public Cart updateCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO) {
        UUID userId = userService.getUser().getId();
        Cart existingCart = cartRepository.findByUser_Id(userId);

        if (existingCart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }

        ProductVariant existingProductVariant = productVariantRepository.findById(cartDetailCreateRequestDTO.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));

        BigDecimal unitPrice = existingProductVariant.getVariantPrice();

        CartDetail existingCartDetail = cartDetailRepository.findByCart_IdAndProductVariant_Id(existingCart.getId(), existingProductVariant.getId());

        if (existingCartDetail != null) {
            existingCartDetail.setCartDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity());
            existingCartDetail.setCartDetailUnitPrice(unitPrice);
            existingCartDetail.setCartDetailUnitPriceAfterDiscount(unitPrice);
            cartDetailRepository.save(existingCartDetail);
        } else {
            throw new IllegalArgumentException("Cart detail not found for product variant ID: " + cartDetailCreateRequestDTO.getVariantId());
        }

        calculateCartTotalCost(existingCart);

        return cartRepository.findById(existingCart.getId()).orElseThrow();
    }



    @Transactional
    @Override
    public Cart clearCart() {
        UUID userId = userService.getUser().getId();
        Cart cart = cartRepository.findByUser_Id(userId);

        if (cart == null) {
            return getCartByUserId();
        }

        // Xóa tất cả cart details thông qua việc clear collection
        cart.getCartDetails().clear();

        // Cập nhật lại giá trị giỏ hàng
        cart.setCartTotalCost(BigDecimal.ZERO);
        cart.setCartDiscountCost(BigDecimal.ZERO);
        cart.setCartTotalCostAfterDiscount(BigDecimal.ZERO);

        // Lưu giỏ hàng đã cập nhật
        return cartRepository.save(cart);
    }

    @Transactional
    protected void calculateCartTotalCost(Cart cart) {
        BigDecimal cartTotalCost = BigDecimal.ZERO;
        BigDecimal cartDiscountCost = BigDecimal.ZERO;
        BigDecimal cartTotalCostAfterDiscount = BigDecimal.ZERO;

        if (cart.getCartDetails() != null && !cart.getCartDetails().isEmpty()) {
            for (CartDetail cartDetail : cart.getCartDetails()) {
                cartTotalCost = cartTotalCost.add(cartDetail.getCartDetailUnitPrice().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
                cartDiscountCost = cartDiscountCost.add(cartDetail.getCartDetailDiscountCost());
                cartTotalCostAfterDiscount = cartTotalCostAfterDiscount.add(cartDetail.getCartDetailUnitPriceAfterDiscount().multiply(BigDecimal.valueOf(cartDetail.getCartDetailQuantity())));
            }
        }

        cart.setCartTotalCost(cartTotalCost);
        cart.setCartDiscountCost(cartDiscountCost);
        cart.setCartTotalCostAfterDiscount(cartTotalCostAfterDiscount);

        cartRepository.save(cart);
    }
}
