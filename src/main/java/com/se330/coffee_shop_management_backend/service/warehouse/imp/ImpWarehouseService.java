package com.se330.coffee_shop_management_backend.service.warehouse.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.warehouse.WarehouseUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.warehouse.IWarehouseService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ImpWarehouseService implements IWarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final INotificationService notificationService;

    public ImpWarehouseService(
            WarehouseRepository warehouseRepository,
            UserRepository userRepository,
            INotificationService notificationService
    ) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse findByIdWarehouse(UUID id) {
        return warehouseRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Warehouse> findAllWarehouses(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Warehouse createWarehouse(WarehouseCreateRequestDTO warehouseCreateRequestDTO) {
        Warehouse newWarehouse = warehouseRepository.save(Warehouse.builder()
                .warehouseName(warehouseCreateRequestDTO.getWarehouseName())
                .warehousePhone(warehouseCreateRequestDTO.getWarehousePhone())
                .warehouseEmail(warehouseCreateRequestDTO.getWarehouseEmail())
                .warehouseAddress(warehouseCreateRequestDTO.getWarehouseAddress())
                .build()
        );

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createWarehouseAddedContentManager(newWarehouse.getWarehouseName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }

        return newWarehouse;
    }

    @Override
    @Transactional
    public Warehouse updateWarehouse(WarehouseUpdateRequestDTO warehouseUpdateRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(warehouseUpdateRequestDTO.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseUpdateRequestDTO.getWarehouseId()));

        existingWarehouse.setWarehouseName(warehouseUpdateRequestDTO.getWarehouseName());
        existingWarehouse.setWarehousePhone(warehouseUpdateRequestDTO.getWarehousePhone());
        existingWarehouse.setWarehouseEmail(warehouseUpdateRequestDTO.getWarehouseEmail());
        existingWarehouse.setWarehouseAddress(warehouseUpdateRequestDTO.getWarehouseAddress());

        warehouseRepository.save(existingWarehouse);

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createWarehouseUpdatedContentManager(existingWarehouse.getWarehouseName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }

        return existingWarehouse;
    }

    @Transactional
    @Override
    public void deleteWarehouse(UUID id) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + id));

        warehouseRepository.delete(existingWarehouse);

        List<User> managersAndAdmin = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);
        managersAndAdmin.addAll(userRepository.findAllByRoleName(Constants.RoleEnum.ADMIN));

        for (User user : managersAndAdmin) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.SUPPLIER)
                            .notificationContent(CreateNotiContentHelper.createWarehouseDeletedContentManager(existingWarehouse.getWarehouseName()))
                            .senderId(null)
                            .receiverId(user.getId())
                            .isRead(false)
                            .build()
            );
        }
    }
}
