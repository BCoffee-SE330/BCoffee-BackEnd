package com.se330.coffee_shop_management_backend.service.favoritedrinkservices.imp;

import com.se330.coffee_shop_management_backend.entity.FavoriteDrink;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.repository.FavoriteDrinkRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.favoritedrinkservices.IFavoriteDrinkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImpFavoriteDrinkService implements IFavoriteDrinkService {

    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    public ImpFavoriteDrinkService(
            FavoriteDrinkRepository favoriteDrinkRepository,
            UserRepository userRepository,
            UserService userService,
            ProductRepository productRepository) {
        this.favoriteDrinkRepository = favoriteDrinkRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void addFavoriteDrink(UUID drinkId) {
        UUID userId = userService.getUser().getId();
        Optional<FavoriteDrink> existingFavorite = favoriteDrinkRepository.findByUser_IdAndProduct_Id(userId, drinkId);

        if (existingFavorite.isPresent()) {
            existingFavorite.ifPresent(favoriteDrinkRepository::delete);
            return;
        }
        // Find user and product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(drinkId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + drinkId));

        // Create and save new favorite drink
        FavoriteDrink favoriteDrink = FavoriteDrink.builder()
                .user(user)
                .product(product)
                .build();

        favoriteDrinkRepository.save(favoriteDrink);
    }

    @Override
    @Transactional
    public void removeFavoriteDrink(UUID drinkId) {
        UUID userId = userService.getUser().getId();
        FavoriteDrink favoriteDrink = favoriteDrinkRepository.findByUser_IdAndProduct_Id(userId, drinkId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Favorite drink not found for user id: " + userId + " and product id: " + drinkId));

        favoriteDrinkRepository.delete(favoriteDrink);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllFavoriteDrinksByUserId(Pageable pageable) {
        // Check if user exists
        UUID userId = userService.getUser().getId();
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<UUID> productIds = favoriteDrinkRepository.findProductIdsByUser_Id(userId, pageable).getContent();
        if (productIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return productRepository.findAllById(productIds, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findTheMostFavoritedDrink(Pageable pageable) {
        List<UUID> productIds = favoriteDrinkRepository.findMostFavoritedProductIds(pageable).getContent();
        if (productIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return productRepository.findAllById(productIds, pageable);
    }

    @Override
    public boolean isDrinkFavoritedByUser(UUID drinkId) {
        UUID userId = userService.getUser().getId();
        return favoriteDrinkRepository.existsFavoriteDrinkByUser_IdAndProduct_Id(userId, drinkId);
    }
}