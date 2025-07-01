package com.se330.coffee_shop_management_backend.service.useddiscount.imp;

import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.discount.UsedDiscountUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.DiscountRepository;
import com.se330.coffee_shop_management_backend.repository.OrderDetailRepository;
import com.se330.coffee_shop_management_backend.repository.UsedDiscountRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.useddiscount.IUsedDiscountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpUsedDiscountService implements IUsedDiscountService {

    private final UsedDiscountRepository usedDiscountRepository;
    private final DiscountRepository discountRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    public ImpUsedDiscountService(
            UsedDiscountRepository usedDiscountRepository,
            DiscountRepository discountRepository,
            OrderDetailRepository orderDetailRepository,
            UserRepository userRepository
    ) {
        this.usedDiscountRepository = usedDiscountRepository;
        this.discountRepository = discountRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UsedDiscount findByIdUsedDiscount(UUID id) {
        return usedDiscountRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsedDiscount> findAllUsedDiscounts(Pageable pageable) {
        return usedDiscountRepository.findAll(pageable);
    }

    /**
     * Creates a new UsedDiscount record, tracking the application of a discount to an order detail.
     *
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates the existence of the specified discount and order detail</li>
     *   <li>Checks if applying this discount would exceed its maximum allowed usage</li>
     *   <li>Updates the discount's usage counter</li>
     *   <li>Deactivates the discount if it reaches its maximum allowed usage</li>
     *   <li>Creates and persists a new UsedDiscount entity linking the discount to the order detail</li>
     * </ol>
     *
     * @param usedDiscountCreateRequestDTO Contains the discount ID, order detail ID, and number of times the discount is being used
     * @return The newly created UsedDiscount entity
     * @throws EntityNotFoundException If the specified discount or order detail cannot be found
     * @throws RollbackException If applying this discount would exceed the maximum allowed usage
     */
    @Override
    @Transactional
    public UsedDiscount createUsedDiscount(UsedDiscountCreateRequestDTO usedDiscountCreateRequestDTO) {
        Discount existingDiscount = discountRepository.findById(usedDiscountCreateRequestDTO.getDiscountId())
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + usedDiscountCreateRequestDTO.getDiscountId()));

        OrderDetail orderDetail = orderDetailRepository.findById(usedDiscountCreateRequestDTO.getOrderDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Order Detail not found with id: " + usedDiscountCreateRequestDTO.getOrderDetailId()));

        int timesUse = existingDiscount.getDiscountUserCount() + usedDiscountCreateRequestDTO.getTimesUse();
        if (timesUse > existingDiscount.getDiscountMaxUsers()) {
            throw new RollbackException("Exceeded discount's max usage");
        }

        existingDiscount.setDiscountUserCount(timesUse);

        if (timesUse == existingDiscount.getDiscountMaxUsers()) {
            existingDiscount.setDiscountIsActive(false);
        }

        discountRepository.save(existingDiscount);

        UsedDiscount usedDiscount = usedDiscountRepository.save(UsedDiscount.builder()
                .discount(existingDiscount)
                .orderDetail(orderDetail)
                .timesUse(usedDiscountCreateRequestDTO.getTimesUse())
                .build());

        return findByIdUsedDiscount(usedDiscount.getId());
    }

    @Override
    @Transactional
    public UsedDiscount updateUsedDiscount(UsedDiscountUpdateRequestDTO usedDiscountUpdateRequestDTO) {
        UsedDiscount existingUsedDiscount = usedDiscountRepository.findById(usedDiscountUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Used Discount not found with id: " + usedDiscountUpdateRequestDTO.getId()));

        Discount existingDiscount = discountRepository.findById(usedDiscountUpdateRequestDTO.getDiscountId())
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + usedDiscountUpdateRequestDTO.getDiscountId()));

        OrderDetail orderDetail = orderDetailRepository.findById(usedDiscountUpdateRequestDTO.getOrderDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Order Detail not found with id: " + usedDiscountUpdateRequestDTO.getOrderDetailId()));

        existingUsedDiscount.setDiscount(existingDiscount);
        existingUsedDiscount.setOrderDetail(orderDetail);
        existingUsedDiscount.setTimesUse(usedDiscountUpdateRequestDTO.getTimesUse());

        usedDiscountRepository.save(existingUsedDiscount);

        return findByIdUsedDiscount(existingUsedDiscount.getId());
    }

    @Override
    @Transactional
    public void deleteUsedDiscount(UUID id) {
        UsedDiscount existingUsedDiscount = usedDiscountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Used Discount not found with id: " + id));

        if (existingUsedDiscount.getDiscount() != null) {
            existingUsedDiscount.getDiscount().getUsedDiscounts().remove(existingUsedDiscount);
        }

        if (existingUsedDiscount.getOrderDetail() != null) {
            existingUsedDiscount.getOrderDetail().getUsedDiscounts().remove(existingUsedDiscount);
        }

        usedDiscountRepository.delete(existingUsedDiscount);
    }

    @Override
    @Transactional(readOnly = true)
    public int timesUsedDiscountByUser(UUID discountId, UUID userId) {
        if (!discountRepository.existsById(discountId)) {
            throw new EntityNotFoundException("Discount not found with id: " + discountId);
        }
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        return usedDiscountRepository.sumTimesUsedByUserAndDiscount(discountId, userId);
    }
}
