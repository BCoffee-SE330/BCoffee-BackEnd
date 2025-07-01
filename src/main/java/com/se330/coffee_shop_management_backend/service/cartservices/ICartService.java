package com.se330.coffee_shop_management_backend.service.cartservices;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Cart;
import com.se330.coffee_shop_management_backend.entity.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICartService {
    Page<Cart> getAllCarts(Pageable pageable);
    Cart getCartByUserId();
    Page<CartDetail> getAllCartDetailsByUserId(Pageable pageable);
    Page<UUID> findBranchesWithSufficientInventory(Pageable pageable);
    Cart addCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO);
    Cart addProductVariantToCart(UUID variantId);
    Cart removeProductVariantFromCart(UUID variantId);
    Cart removeAllWithSpecificVariant(UUID variantId);
    Cart updateCartDetail(CartDetailCreateRequestDTO cartDetailCreateRequestDTO);
    Cart clearCart();
}