package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.NotificationRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.NotificationTypeEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class Notifications {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        createNotifications();
    }

    private void createNotifications() {
        log.info("Creating dummy notifications...");
        Random random = new Random();

        // Get all users
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            log.error("No users found to create notifications");
            return;
        }

        // Separate managers and non-managers
        List<User> managers = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);

        // All users can be receivers

        // Prepare notification type values for random selection
        List<Constants.NotificationTypeEnum> notificationTypes = List.of(
                ORDER, DISCOUNT, SYSTEM, INVENTORY, EMPLOYEE, MANAGER, INVOICE, TRANSFER, BRANCH, SUPPLIER, PRODUCT, WAREHOUSE
        );

        List<Notification> notifications = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            // Random notification type
            Constants.NotificationTypeEnum notificationType = notificationTypes.get(random.nextInt(notificationTypes.size()));

            // Random receiver (cannot be null)
            User receiver = allUsers.get(random.nextInt(allUsers.size()));

            // Random sender (can be null or a manager)
            User sender = random.nextBoolean() && !managers.isEmpty() ? managers.get(random.nextInt(managers.size())) : null;

            // Generate content based on notification type
            String content = generateRandomContent(notificationType, random);

            // Create notification
            Notification notification = Notification.builder()
                    .notificationType(notificationType)
                    .notificationContent(content)
                    .sender(sender)
                    .receiver(receiver)
                    .isRead(random.nextBoolean())
                    .build();

            notifications.add(notification);

            // Save in batches of 500 to avoid memory issues
            if (i > 0 && i % 500 == 0) {
                notificationRepository.saveAll(notifications);
                notifications.clear();
                log.info("Created {} notifications so far", i);
            }
        }

        // Save remaining notifications
        if (!notifications.isEmpty()) {
            notificationRepository.saveAll(notifications);
        }

        log.info("Created 10,000 dummy notifications successfully");
    }

    private String generateRandomContent(Constants.NotificationTypeEnum type, Random random) {
        UUID randomId = UUID.randomUUID();
        String randomName = "Item" + random.nextInt(100);
        String randomBranchName = "Chi nhánh " + random.nextInt(10);
        String randomDiscountName = "Khuyến mãi " + random.nextInt(20);
        String randomEmployeeName = "Nhân viên " + random.nextInt(50);

        return switch (type) {
            case ORDER -> {
                int choice = random.nextInt(8);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createOrderSuccessContentCustomer(randomId);
                    case 1 -> CreateNotiContentHelper.createOrderFailedPaymentContent(randomId);
                    case 2 -> CreateNotiContentHelper.createOrderFailedIngredientsContent(randomId);
                    case 3 -> CreateNotiContentHelper.createOrderInvalidContent(randomId);
                    case 4 -> CreateNotiContentHelper.createOrderReceivedContent(randomId);
                    case 5 -> CreateNotiContentHelper.createOrderCompletedContent(randomId);
                    case 6 -> CreateNotiContentHelper.createOrderCancelledContent(randomId);
                    default -> CreateNotiContentHelper.createInStorePurchaseContent(randomId);
                };
            }
            case DISCOUNT -> {
                int choice = random.nextInt(5);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createDiscountForManager(randomDiscountName);
                    case 1 -> CreateNotiContentHelper.updateDiscountForManager(randomDiscountName);
                    case 2 -> CreateNotiContentHelper.createDiscountAddedContent(randomDiscountName,
                            random.nextInt(50) + "K",
                            "01/01/2023", "31/12/2023", randomBranchName);
                    case 3 -> CreateNotiContentHelper.createDiscountDeletedContent(randomDiscountName, randomBranchName);
                    default -> CreateNotiContentHelper.createDiscountExpiringContent(randomDiscountName, random.nextInt(5) + 1, randomBranchName);
                };
            }
            case SYSTEM -> {
                int choice = random.nextInt(3);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createMaintenanceNotificationContent("20:00", "2 giờ");
                    case 1 -> CreateNotiContentHelper.createVersionUpdateContent("2.0." + random.nextInt(10));
                    default -> CreateNotiContentHelper.createPolicyChangeContent("Chính sách bảo mật");
                };
            }
            case INVENTORY -> {
                int choice = random.nextInt(2);
                yield choice == 0 ?
                        CreateNotiContentHelper.createLowStockWarningContent(randomName, random.nextInt(10)) :
                        CreateNotiContentHelper.createExpirationWarningContent(randomName, random.nextInt(10) + 1);
            }
            case EMPLOYEE -> {
                int choice = random.nextInt(6);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createWelcomeBranchContentManager(randomEmployeeName);
                    case 1 -> CreateNotiContentHelper.createWelcomeBranchContent(randomBranchName);
                    case 2 -> CreateNotiContentHelper.createNewShiftAssignmentContentManager(randomEmployeeName,
                            String.valueOf(random.nextInt(12) + 1), "2023");
                    case 3 -> CreateNotiContentHelper.createNewShiftAssignmentContent(String.valueOf(random.nextInt(12) + 1), "2023");
                    case 4 -> CreateNotiContentHelper.createCheckinSuccessContent(randomEmployeeName, "08:00");
                    default -> CreateNotiContentHelper.createCheckinSuccessContent("08:00");
                };
            }
            case MANAGER -> {
                int choice = random.nextInt(3);
                List<String> names = List.of("Nguyễn Văn A", "Trần Thị B", "Lê Văn C");
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createManagerNotificationSentContent(randomEmployeeName);
                    case 1 -> CreateNotiContentHelper.createManagerNotificationSentContentForMany(names);
                    default -> CreateNotiContentHelper.createManagerNotificationReceivedContent("Quản lý", "Họp gấp lúc 15:00");
                };
            }
            case INVOICE -> {
                int choice = random.nextInt(4);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createInvoiceSuccessContent(randomId, "Kho " + random.nextInt(5));
                    case 1 -> CreateNotiContentHelper.createInvoiceFailedContent(randomId, "Thiếu thông tin");
                    case 2 -> CreateNotiContentHelper.createInvoiceUpdatedContent(randomId);
                    default -> CreateNotiContentHelper.createInvoiceCancelledContent(randomId);
                };
            }
            case TRANSFER -> {
                int choice = random.nextInt(5);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createTransferSuccessContent(randomId, randomBranchName);
                    case 1 -> CreateNotiContentHelper.createTransferFailedContent(randomId, "Lỗi hệ thống");
                    case 2 -> CreateNotiContentHelper.createTransferInsufficientContent(randomId, randomName);
                    case 3 -> CreateNotiContentHelper.createTransferUpdatedContent(randomId);
                    default -> CreateNotiContentHelper.createTransferCancelledContent(randomId);
                };
            }
            case BRANCH -> {
                int choice = random.nextInt(3);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createBranchAddedContent(randomBranchName, "123 Đường ABC, Quận 1");
                    case 1 -> CreateNotiContentHelper.createBranchDeletedContent(randomBranchName, "31/12/2023");
                    default -> CreateNotiContentHelper.createBranchUpdatedContent(randomBranchName);
                };
            }
            case SUPPLIER -> {
                int choice = random.nextInt(3);
                String supplierName = "Nhà cung cấp " + random.nextInt(10);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createSupplierAddedContentManager(supplierName);
                    case 1 -> CreateNotiContentHelper.createSupplierUpdatedContentManager(supplierName);
                    default -> CreateNotiContentHelper.createSupplierDeletedContentManager(supplierName);
                };
            }
            case PRODUCT -> {
                int choice = random.nextInt(6);
                String productName = "Sản phẩm " + random.nextInt(20);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createProductAddedContentManager(productName);
                    case 1 -> CreateNotiContentHelper.createProductAddedContentAll(productName);
                    case 2 -> CreateNotiContentHelper.createProductUpdatedContentManager(productName);
                    case 3 -> CreateNotiContentHelper.createProductUpdatedContentAll(productName);
                    case 4 -> CreateNotiContentHelper.createProductDeletedContentManager(productName);
                    default -> CreateNotiContentHelper.createProductDeletedContentAll(productName);
                };
            }
            case WAREHOUSE -> {
                int choice = random.nextInt(3);
                String warehouseName = "Kho " + random.nextInt(5);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createWarehouseAddedContentManager(warehouseName);
                    case 1 -> CreateNotiContentHelper.createWarehouseUpdatedContentManager(warehouseName);
                    default -> CreateNotiContentHelper.createWarehouseDeletedContentManager(warehouseName);
                };
            }
            case PAYMENT -> {
                int choice = random.nextInt(4);
                yield switch (choice) {
                    case 0 -> CreateNotiContentHelper.createPaymentSuccessContent(randomId);
                    case 1 -> CreateNotiContentHelper.createPaymentFailedContent(randomId, "Insufficient funds");
                    case 2 -> CreateNotiContentHelper.createPaymentRefundedContent(randomId);
                    default -> CreateNotiContentHelper.createPaymentPendingContent(randomId);
                };
            }
        };
    }
}
