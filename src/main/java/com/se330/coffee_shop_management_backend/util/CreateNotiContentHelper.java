package com.se330.coffee_shop_management_backend.util;

import java.util.List;
import java.util.UUID;

public class CreateNotiContentHelper {

    public static String createPaymentSuccessContent(UUID orderId) {
        return "Thanh toán thành công cho đơn hàng " + orderId;
    }

    public static String createPaymentFailedContent(UUID orderId, String reason) {
        return "Thanh toán thất bại cho đơn hàng " + orderId + ": " + reason;
    }

    public static String createPaymentRefundedContent(UUID orderId) {
        return "Đã hoàn tiền cho đơn hàng " + orderId;
    }

    public static String createPaymentPendingContent(UUID orderId) {
        return "Đang chờ xác nhận thanh toán cho đơn hàng " + orderId;
    }

    public static String createOrderSuccessContentCustomer(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được tạo thành công. Cảm ơn bạn đã sử dụng dịch vụ của BCoffee!",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderFailedPaymentContent(UUID orderId) {
        return String.format("Thanh toán cho đơn hàng #%s thất bại. Vui lòng kiểm tra lại phương thức thanh toán hoặc số dư tài khoản của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderFailedIngredientsContent(UUID orderId) {
        return String.format("Đơn hàng #%s không thể thực hiện do không đủ nguyên liệu. Vui lòng thử lại sau hoặc điều chỉnh đơn hàng của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderInvalidContent(UUID orderId) {
        return String.format("Đơn hàng #%s không hợp lệ. Vui lòng kiểm tra lại thông tin đơn hàng của bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderReceivedContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được tiếp nhận và đang được xử lý. BCoffee sẽ sớm chuẩn bị đơn hàng cho bạn.",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderCompletedContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được hoàn thàn. Cảm ơn bạn đã đặt hàng tại BCoffee!",
                orderId.toString().substring(0, 8));
    }

    public static String createOrderCancelledContent(UUID orderId) {
        return String.format("Đơn hàng #%s đã bị hủy. Nếu bạn có thắc mắc, vui lòng liên hệ với BCoffee để được hỗ trợ.",
                orderId.toString().substring(0, 8));
    }

    public static String createInStorePurchaseContent(UUID orderId) {
        return String.format("Cảm ơn bạn đã mua hàng tại cửa hàng của BCoffee. Mã đơn hàng của bạn: #%s",
                orderId.toString().substring(0, 8));
    }

    public static String orderDeliveringContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã hoàn thành và đang được giao. Vui lòng chờ trong giây lát.",
                orderId.toString().substring(0, 8));
    }

    public static String orderDeliveredContent(UUID orderId) {
        return String.format("Đơn hàng #%s của bạn đã được giao thành công. Cảm ơn bạn đã đặt hàng tại BCoffee!",
                orderId.toString().substring(0, 8));
    }

    // DISCOUNT notification content methods

    public static String createDiscountForManager(String discountName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' đã được tạo thành công. Vui lòng kiểm tra chi tiết trong hệ thống.",
                discountName);
    }

    public static String updateDiscountForManager(String discountName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' đã được cập nhật thành công. Vui lòng kiểm tra chi tiết trong hệ thống.",
                discountName);
    }

    public static String createDiscountAddedContent(String discountName, String discountValue, String startDate, String endDate, String branchName) {
        return String.format("Ưu đãi mới! '%s' với giá trị %s tại chi nhánh %s! Áp dụng từ %s đến %s. Đừng bỏ lỡ!",
                discountName, discountValue, branchName, startDate, endDate);
    }

    public static String createDiscountDeletedContent(String discountName, String branchName) {
        return String.format("Thông báo: Chương trình khuyến mãi '%s' tại chi nhánh '%s' đã kết thúc. Cảm ơn quý khách đã tham gia!",
                discountName, branchName);
    }

    public static String createDiscountUpdatedContent(String discountName, String branchName) {
        return String.format("Chú ý! Thông tin khuyến mãi '%s' tại chi nhánh %s vừa được cập nhật. Vui lòng kiểm tra chi tiết mới nhất!",
                discountName, branchName);
    }

    public static String createDiscountExpiringContent(String discountName, int daysRemaining, String branchName) {
        return String.format("Sắp kết thúc! Chỉ còn %d ngày để tận hưởng ưu đãi '%s' tại chi nhánh %s. Hãy ghé BCoffee ngay hôm nay!",
                daysRemaining, discountName, branchName);
    }

    // SYSTEM notification content methods
    public static String createMaintenanceNotificationContent(String startTime, String duration) {
        return String.format("Hệ thống sẽ bảo trì vào lúc %s, dự kiến kéo dài %s. Mong quý khách thông cảm.",
                startTime, duration);
    }

    public static String createVersionUpdateContent(String version) {
        return String.format("Hệ thống đã được cập nhật lên phiên bản %s với nhiều tính năng mới.", version);
    }

    public static String createPolicyChangeContent(String policyName) {
        return String.format("Chính sách '%s' đã được cập nhật. Vui lòng xem chi tiết trong phần Điều khoản sử dụng.",
                policyName);
    }

    // INVENTORY notification content methods
    public static String createLowStockWarningContent(String itemName, int currentQuantity) {
        return String.format("Cảnh báo: Sản phẩm '%s' sắp hết hàng (số lượng hiện tại: %d). Vui lòng nhập thêm.",
                itemName, currentQuantity);
    }

    public static String createExpirationWarningContent(String itemName, int daysRemaining) {
        return String.format("Cảnh báo: Sản phẩm '%s' sẽ hết hạn trong %d ngày. Vui lòng kiểm tra kho.",
                itemName, daysRemaining);
    }

    // EMPLOYEE notification content methods
    public static String createWelcomeBranchContentManager(String employeeName) {
        return String.format("Bạn đã tiếp nhận thành công nhân viên mới %s!", employeeName);
    }

    public static String createWelcomeBranchContent(String branchName) {
        return String.format("Chào mừng bạn đến với chi nhánh %s! Chúc bạn có những trải nghiệm tuyệt vời.", branchName);
    }

    public static String createNewShiftAssignmentContentManager(String employeeName, String month, String year) {
        return String.format("Bạn đã phân công ca làm việc mới trong tháng %s/%s cho nhân viên %s.", month, year, employeeName);
    }

    public static String createNewShiftAssignmentContent(String month, String year) {
        return String.format("Bạn đã được phân công ca làm việc mới trong tháng %s/%s.", month, year);
    }

    public static String createCheckinSuccessContent(String employeeName, String time) {
        return String.format("%s đã checkin thành công vào lúc %s.", employeeName, time);
    }

    public static String createCheckinSuccessContent(String time) {
        return String.format("Bạn đã checkin thành công vào lúc %s. Chúc bạn có một ngày làm việc hiệu quả!", time);
    }

    // SALARY notification content methods for salary creation
    public static String createSalaryCreatedContentForEmployee(String month, String year, String amount) {
        return String.format("Lương tháng %s/%s của bạn đã được tính với tổng số tiền %s.", month, year, amount);
    }

    public static String createSalaryCreatedContentForManager(String employeeName, String month, String year, String amount) {
        return String.format("Bạn đã tính lương tháng %s/%s cho nhân viên %s với tổng số tiền %s thành công.", month, year, employeeName, amount);
    }

    public static String createSalaryCreatedContentForManagerAll(String month, String year) {
        return String.format("Bạn đã tính lương tháng %s/%s cho tất cả nhân viên thành công.", month, year);
    }

    // SALARY notification content methods for salary updates
    public static String createSalaryUpdatedContentForEmployee(String month, String year, String amount) {
        return String.format("Lương tháng %s/%s của bạn đã được cập nhật với tổng số tiền mới %s.", month, year, amount);
    }

    public static String createSalaryUpdatedContentForManager(String employeeName, String month, String year, String amount) {
        return String.format("Bạn đã cập nhật lương tháng %s/%s cho nhân viên %s với tổng số tiền mới %s thành công.", month, year, employeeName, amount);
    }

    // SALARY notification content methods for salary deletion
    public static String createSalaryDeletedContentForEmployee(String month, String year) {
        return String.format("Thông tin lương tháng %s/%s của bạn đã bị hủy.", month, year);
    }

    public static String createSalaryDeletedContentForManager(String employeeName, String month, String year) {
        return String.format("Bạn đã hủy thông tin lương tháng %s/%s của nhân viên %s thành công.", month, year, employeeName);
    }

    // EMPLOYEE notification content methods for employee info updates
    public static String createEmployeeInfoUpdatedContent() {
        return "Thông tin của bạn đã được cập nhật trên hệ thống.";
    }

    public static String createEmployeeInfoUpdatedContentForManager(String employeeName) {
        return String.format("Bạn đã cập nhật thông tin nhân viên %s thành công.", employeeName);
    }

    // EMPLOYEE notification content methods for shift updates
    public static String createShiftUpdatedContent(String month, String year) {
        return String.format("Ca làm việc trong tháng %s/%s của bạn đã được cập nhật. Vui lòng kiểm tra lịch làm việc mới.", month, year);
    }

    public static String createShiftUpdatedContentForManager(String employeeName) {
        return String.format("Bạn đã cập nhật ca làm việc cho nhân viên %s thành công.", employeeName);
    }

    // EMPLOYEE notification content methods for shift deletion
    public static String createShiftDeletedContent(String month, String year) {
        return String.format("Ca làm việc trong tháng %s/%s của bạn đã bị xóa. Vui lòng kiểm tra lịch làm việc mới.", month, year);
    }

    public static String createShiftDeletedContentForManager(String employeeName) {
        return String.format("Bạn đã xóa ca làm việc của nhân viên %s thành công.", employeeName);
    }

    // EMPLOYEE notification content methods for checkin updates
    public static String createCheckinUpdatedContent(String time) {
        return String.format("Thông tin check-in của bạn đã được cập nhật thành công vào lúc %s.", time);
    }

    public static String createCheckinUpdatedContentForManager(String employeeName, String time) {
        return String.format("Bạn đã cập nhật thông tin check-in của nhân viên %s vào lúc %s thành công.", employeeName, time);
    }

    public static String createCheckinDeletedContent(String time) {
        return String.format("Thông tin check-in của bạn đã được xóa vào lúc %s.", time);
    }

    public static String createCheckinDeletedContentForManager(String employeeName, String time) {
        return String.format("Bạn đã xóa thông tin check-in của nhân viên %s vào lúc %s thành công.", employeeName, time);
    }

    // BRANCH notification content methods for branch creation
    public static String createBranchAddedContent(String branchName, String address) {
        return String.format("Khai trương chi nhánh mới! BCoffee vui mừng thông báo chi nhánh %s tại địa chỉ %s đã chính thức đi vào hoạt động. Hãy ghé thăm chúng tôi!",
                branchName, address);
    }

    // BRANCH notification content methods for branch deletion
    public static String createBranchDeletedContent(String branchName, String lastDay) {
        return String.format("Thông báo: Chi nhánh %s sẽ ngưng hoạt động từ ngày %s. BCoffee xin chân thành cảm ơn quý khách đã ủng hộ trong thời gian qua.",
                branchName, lastDay);
    }

    // BRANCH notification content methods for branch update
    public static String createBranchUpdatedContent(String branchName) {
        return String.format("Thông báo: Thông tin chi nhánh %s đã được cập nhật. Vui lòng kiểm tra thông tin mới nhất của chi nhánh trên ứng dụng hoặc website của BCoffee.",
                branchName);
    }

    // MANAGER notification content methods
    public static String createManagerNotificationSentContent(String employeeName) {
        return String.format("Thông báo của bạn đã được gửi thành công đến %s.", employeeName);
    }

    public static String createManagerNotificationSentContentForMany(List<String> employeeNames) {
        if (employeeNames.size() == 2) {
            return String.format("Thông báo của bạn đã được gửi thành công đến %s, %s.", employeeNames.get(0), employeeNames.get(1));
        } else {
            return String.format("Thông báo của bạn đã được gửi thành công đến %s, %s, ... .", employeeNames.get(0), employeeNames.get(1));
        }
    }

    public static String createManagerNotificationSentContentForAll() {
        return "Thông báo của bạn đã được gửi thành công đến tất cả nhân viên.";
    }

    public static String createManagerNotificationReceivedContent(String managerName, String content) {
        return String.format("Bạn có thông báo mới từ %s: \"%s\"", managerName, content);
    }

    // INVOICE notification content methods
    public static String createInvoiceSuccessContent(UUID invoiceId, String warehouseName) {
        return String.format("Phiếu nhập kho #%s đã được tạo thành công cho kho %s.",
                invoiceId.toString().substring(0, 8), warehouseName);
    }

    public static String createInvoiceFailedContent(UUID invoiceId, String reason) {
        return String.format("Nhập kho thất bại cho phiếu #%s. Lý do: %s",
                invoiceId.toString().substring(0, 8), reason);
    }

    public static String createInvoiceUpdatedContent(UUID invoiceId) {
        return String.format("Phiếu nhập kho #%s đã được cập nhật thành công.",
                invoiceId.toString().substring(0, 8));
    }

    public static String createInvoiceCancelledContent(UUID invoiceId) {
        return String.format("Phiếu nhập kho #%s đã bị hủy.", invoiceId.toString().substring(0, 8));
    }

    // TRANSFER notification content methods
    public static String createTransferSuccessContent(UUID transferId, String branchName) {
        return String.format("Phiếu xuất kho #%s đến chi nhánh %s đã được tạo thành công.",
                transferId.toString().substring(0, 8), branchName);
    }

    public static String createTransferFailedContent(UUID transferId, String reason) {
        return String.format("Xuất kho thất bại cho phiếu #%s. Lý do: %s",
                transferId.toString().substring(0, 8), reason);
    }

    public static String createTransferInsufficientContent(UUID transferId, String itemName) {
        return String.format("Xuất kho thất bại cho phiếu #%s. Kho không đủ số lượng sản phẩm '%s'.",
                transferId.toString().substring(0, 8), itemName);
    }

    public static String createTransferUpdatedContent(UUID transferId) {
        return String.format("Phiếu xuất kho #%s đã được cập nhật thành công.",
                transferId.toString().substring(0, 8));
    }

    public static String createTransferCancelledContent(UUID transferId) {
        return String.format("Phiếu xuất kho #%s đã bị hủy.", transferId.toString().substring(0, 8));
    }

    // SUPPLIER notification content methods
    public static String createSupplierAddedContentManager(String supplierName) {
        return String.format("Đã thêm nhà cung cấp mới '%s' vào hệ thống quản lý.", supplierName);
    }

    public static String createSupplierUpdatedContentManager(String supplierName) {
        return String.format("Thông tin nhà cung cấp '%s' đã được cập nhật thành công.", supplierName);
    }

    public static String createSupplierDeletedContentManager(String supplierName) {
        return String.format("Đã xóa thông tin nhà cung cấp '%s' khỏi hệ thống.", supplierName);
    }

    // PRODUCT notification content methods
    public static String createProductAddedContentManager(String productName) {
        return String.format("Sản phẩm mới '%s' đã được thêm vào hệ thống.", productName);
    }

    public static String createProductAddedContentAll(String productName) {
        return String.format("BCoffee vừa ra mắt sản phẩm mới: '%s'. Hãy thử ngay hôm nay!", productName);
    }

    public static String createProductUpdatedContentManager(String productName) {
        return String.format("Thông tin sản phẩm '%s' đã được cập nhật thành công.", productName);
    }

    public static String createProductUpdatedContentAll(String productName) {
        return String.format("Thông tin sản phẩm '%s' vừa được cập nhật. Kiểm tra ngay để biết thêm chi tiết!", productName);
    }

    public static String createProductDeletedContentManager(String productName) {
        return String.format("Sản phẩm '%s' đã được xóa khỏi hệ thống.", productName);
    }

    public static String createProductDeletedContentAll(String productName) {
        return String.format("Thông báo: Sản phẩm '%s' đã ngưng kinh doanh. Cảm ơn quý khách đã ủng hộ!", productName);
    }

    // WAREHOUSE notification content methods
    public static String createWarehouseAddedContentManager(String warehouseName) {
        return String.format("Đã thêm nhà kho mới '%s' vào hệ thống quản lý.", warehouseName);
    }

    public static String createWarehouseUpdatedContentManager(String warehouseName) {
        return String.format("Thông tin nhà kho '%s' đã được cập nhật thành công.", warehouseName);
    }

    public static String createWarehouseDeletedContentManager(String warehouseName) {
        return String.format("Đã xóa thông tin nhà kho '%s' khỏi hệ thống.", warehouseName);
    }

    // Updated INVOICE notification content methods
    public static String createInvoiceFailedInsufficientStockContent(UUID invoiceId, String itemName) {
        return String.format("Cập nhật phiếu nhập kho #%s thất bại. Hàng tồn của nguyên liệu '%s' trong kho không đáp ứng.",
                invoiceId.toString().substring(0, 8), itemName);
    }

    public static String createInvoiceCancelFailedContent(UUID invoiceId, String reason) {
        return String.format("Hủy phiếu nhập kho #%s thất bại. Lý do: %s",
                invoiceId.toString().substring(0, 8), reason);
    }

    // Updated TRANSFER notification content methods
    public static String createTransferBranchSuccessContent(UUID transferId, String branchName) {
        return String.format("Xuất nguyên liệu cho chi nhánh %s thành công với mã phiếu #%s.",
                branchName, transferId.toString().substring(0, 8));
    }

    public static String createTransferBranchFailedContent(UUID transferId, String branchName, String reason) {
        return String.format("Xuất nguyên liệu cho chi nhánh %s thất bại với mã phiếu #%s. Lý do: %s",
                branchName, transferId.toString().substring(0, 8), reason);
    }
}
