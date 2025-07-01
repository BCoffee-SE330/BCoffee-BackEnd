package com.se330.coffee_shop_management_backend.service.useddiscount;

import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.UsedDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUsedDiscountService {
    UsedDiscount findByIdUsedDiscount(UUID id);
    Page<UsedDiscount> findAllUsedDiscounts(Pageable pageable);
    UsedDiscount createUsedDiscount(UsedDiscountCreateRequestDTO usedDiscountCreateRequestDTO);
    UsedDiscount updateUsedDiscount(UsedDiscountUpdateRequestDTO usedDiscountUpdateRequestDTO);
    void deleteUsedDiscount(UUID id);
    int timesUsedDiscountByUser(UUID discountId, UUID userId);
}