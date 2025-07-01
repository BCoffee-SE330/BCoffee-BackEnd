package com.se330.coffee_shop_management_backend.service.supplierservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Supplier;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.SupplierRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.supplierservices.ISupplierService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ImpSupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final INotificationService notificationService;

    public ImpSupplierService(
            SupplierRepository supplierRepository,
            UserRepository userRepository,
            INotificationService notificationService
    ) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findByIdSupplier(UUID id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> findAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Supplier createSupplier(SupplierCreateRequestDTO supplierCreateRequestDTO) {
        Supplier newSupplier = supplierRepository.save(
                Supplier.builder()
                        .supplierName(supplierCreateRequestDTO.getSupplierName())
                        .supplierPhone(supplierCreateRequestDTO.getSupplierPhone())
                        .supplierEmail(supplierCreateRequestDTO.getSupplierEmail())
                        .supplierAddress(supplierCreateRequestDTO.getSupplierAddress())
                        .build()
        );

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createSupplierAddedContentManager(newSupplier.getSupplierName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }

        return newSupplier;
    }

    @Override
    @Transactional
    public Supplier updateSupplier(SupplierUpdateRequestDTO supplierUpdateRequestDTO) {
        Supplier existingSupplier = supplierRepository.findById(supplierUpdateRequestDTO.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + supplierUpdateRequestDTO.getSupplierId()));

        existingSupplier.setSupplierName(supplierUpdateRequestDTO.getSupplierName());
        existingSupplier.setSupplierPhone(supplierUpdateRequestDTO.getSupplierPhone());
        existingSupplier.setSupplierEmail(supplierUpdateRequestDTO.getSupplierEmail());
        existingSupplier.setSupplierAddress(supplierUpdateRequestDTO.getSupplierAddress());

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createSupplierUpdatedContentManager(existingSupplier.getSupplierName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }

        return supplierRepository.save(existingSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(UUID id) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + id));

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createSupplierDeletedContentManager(existingSupplier.getSupplierName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }

        supplierRepository.delete(existingSupplier);
    }
}
