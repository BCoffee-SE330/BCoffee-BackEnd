package com.se330.coffee_shop_management_backend.service.adminservices.imp;

import com.se330.coffee_shop_management_backend.dto.response.admin.*;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.adminservices.IAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

@Service
@RequiredArgsConstructor
public class ImpAdminService implements IAdminService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final BranchRepository branchRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CatalogRepository catalogRepository;
    private final CheckinRepository checkinRepository;
    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final IngredientRepository ingredientRepository;
    private final CommentRepository commentRepository;
    private final DiscountRepository discountRepository;
    private final EmployeeRepository employeeRepository;
    private final InventoryRepository inventoryRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final InvoiceRepository invoiceRepository;
    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final RecipeRepository recipeRepository;
    private final RoleRepository roleRepository;
    private final SalaryRepository salaryRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockRepository stockRepository;
    private final SubCheckinRepository subCheckinRepository;
    private final SupplierRepository supplierRepository;
    private final TransferRepository transferRepository;
    private final TransferDetailRepository transferDetailRepository;
    private final UsedDiscountRepository usedDiscountRepository;

    @Value( "${PATH_TO_KNOWLEDGE}" )
    private String pathToKnowledge;

    @Override
    @Transactional
    public void updateDatabase() {
        deleteAll();

        try {
            createBranch();
        } catch (Exception e) {
            System.err.println("Error creating branch PDF: " + e.getMessage());
        }

        try {
            createCart();
        } catch (Exception e) {
            System.err.println("Error creating cart PDF: " + e.getMessage());
        }

        try {
            createCartDetail();
        } catch (Exception e) {
            System.err.println("Error creating cart detail PDF: " + e.getMessage());
        }

        try {
            createCatalog();
        } catch (Exception e) {
            System.err.println("Error creating catalog PDF: " + e.getMessage());
        }

        try {
            createCheckin();
        } catch (Exception e) {
            System.err.println("Error creating checkin PDF: " + e.getMessage());
        }

        try {
            createComment();
        } catch (Exception e) {
            System.err.println("Error creating comment PDF: " + e.getMessage());
        }

        try {
            createDiscount();
        } catch (Exception e) {
            System.err.println("Error creating discount PDF: " + e.getMessage());
        }

        try {
            createDiscountProductVariant();
        } catch (Exception e) {
            System.err.println("Error creating discount product variant PDF: " + e.getMessage());
        }

        try {
            createEmployee();
        } catch (Exception e) {
            System.err.println("Error creating employee PDF: " + e.getMessage());
        }

        try {
            createInventory();
        } catch (Exception e) {
            System.err.println("Error creating inventory PDF: " + e.getMessage());
        }

        try {
            createInvoice();
        } catch (Exception e) {
            System.err.println("Error creating invoice PDF: " + e.getMessage());
        }

        try {
            createInvoiceDetail();
        } catch (Exception e) {
            System.err.println("Error creating invoice detail PDF: " + e.getMessage());
        }

        try {
            createPaymentMethod();
        } catch (Exception e) {
            System.err.println("Error creating payment method PDF: " + e.getMessage());
        }

        try {
            createNotification();
        } catch (Exception e) {
            System.err.println("Error creating notification PDF: " + e.getMessage());
        }

        try {
            createOrder();
        } catch (Exception e) {
            System.err.println("Error creating order PDF: " + e.getMessage());
        }

        try {
            createOrderDetail();
        } catch (Exception e) {
            System.err.println("Error creating order detail PDF: " + e.getMessage());
        }

        try {
            createOrderPayment();
        } catch (Exception e) {
            System.err.println("Error creating order payment PDF: " + e.getMessage());
        }

        try {
            createRecipe();
        } catch (Exception e) {
            System.err.println("Error creating recipe PDF: " + e.getMessage());
        }

        try {
            createRole();
        } catch (Exception e) {
            System.err.println("Error creating role PDF: " + e.getMessage());
        }

        try {
            createSalary();
        } catch (Exception e) {
            System.err.println("Error creating salary PDF: " + e.getMessage());
        }

        try {
            createShift();
        } catch (Exception e) {
            System.err.println("Error creating shift PDF: " + e.getMessage());
        }

        try {
            createUser();
        } catch (Exception e) {
            System.err.println("Error creating user PDF: " + e.getMessage());
        }

        try {
            createShippingAddresses();
        } catch (Exception e) {
            System.err.println("Error creating shipping addresses PDF: " + e.getMessage());
        }

        try {
            createWarehouse();
        } catch (Exception e) {
            System.err.println("Error creating warehouse PDF: " + e.getMessage());
        }

        try {
            createStock();
        } catch (Exception e) {
            System.err.println("Error creating stock PDF: " + e.getMessage());
        }

        try {
            createSubCheckin();
        } catch (Exception e) {
            System.err.println("Error creating sub-checkin PDF: " + e.getMessage());
        }

        try {
            createSupplier();
        } catch (Exception e) {
            System.err.println("Error creating supplier PDF: " + e.getMessage());
        }

        try {
            createTransfer();
        } catch (Exception e) {
            System.err.println("Error creating transfer PDF: " + e.getMessage());
        }

        try {
            createTransferDetail();
        } catch (Exception e) {
            System.err.println("Error creating transfer detail PDF: " + e.getMessage());
        }

        try {
            createUsedDiscount();
        } catch (Exception e) {
            System.err.println("Error creating used discount PDF: " + e.getMessage());
        }

        try {
            createProductCategory();
        } catch (Exception e) {
            System.err.println("Error creating product category PDF: " + e.getMessage());
        }

        try {
            createProduct();
        } catch (Exception e) {
            System.err.println("Error creating product PDF: " + e.getMessage());
        }

        try {
            createFavoriteDrink();
        } catch (Exception e) {
            System.err.println("Error creating favorite drink PDF: " + e.getMessage());
        }

        try {
            createIngredient();
        } catch (Exception e) {
            System.err.println("Error creating ingredient PDF: " + e.getMessage());
        }

        try {
            createProductVariant();
        } catch (Exception e) {
            System.err.println("Error creating product variant PDF: " + e.getMessage());
        }
    }

    private void deleteAll() {
        Path directory = Paths.get(pathToKnowledge);

        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                System.out.println("Created directory: " + pathToKnowledge);
            } else if (!Files.isDirectory(directory)) {
                System.err.println("Path exists but is not a directory: " + pathToKnowledge);
                return;
            }

            try (Stream<Path> files = Files.list(directory)) {
                files.filter(Files::isRegularFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                                System.out.println("Deleted file: " + file.getFileName());
                            } catch (IOException e) {
                                System.err.println("Failed to delete file: " + file.getFileName());
                                e.printStackTrace();
                            }
                        });
            }
        } catch (IOException e) {
            System.err.println("Error accessing or creating directory: " + pathToKnowledge);
            e.printStackTrace();
        }
    }

    private void createBranch() {
        String tableName = "branches - Chi nhánh";
        String description = "Bảng chứa thông tin về các chi nhánh của BCoffee";
        String fieldDescriptions = """
            branchId - Mã của chi nhánh (ID của chi nhánh trong hệ thống)
            branchName - Tên của chi nhánh (Tên hiển thị của chi nhánh)
            branchAddress - Thông tin địa chỉ chi nhánh (Địa chỉ đầy đủ của chi nhánh)
            branchPhone - Thông tin liên hệ của chi nhánh (Số điện thoại liên hệ)
            branchEmail - Email của chi nhánh (Địa chỉ email liên hệ của chi nhánh)
            managerId - Mã của nhân viên quản lý chi nhánh (Khóa ngoại tham chiếu đến bảng nhân viên)
        """;
        List<Branch> branches = branchRepository.findAll();
        List<BranchAdminResponse> branchAdminResponses = BranchAdminResponse.convert(branches);
        createPDFFile(tableName, description, fieldDescriptions, branchAdminResponses);
    }

    private void createCart() {
        String tableName = "carts - Giỏ hàng";
        String description = "Bảng chứa thông tin về giỏ hàng của khách hàng";
        String fieldDescriptions = """
        cartId - Mã của giỏ hàng (ID của giỏ hàng trong hệ thống)
        cartTotalCost - Tổng giá trị giỏ hàng trước khi giảm giá (đã bao gồm thuế)
        cartDiscountCost - Giá trị giảm giá (Khoản tiền được giảm)
        cartTotalCostAfterDiscount - Tổng giá trị sau khi giảm giá (đã bao gồm thuế)
        userId - Mã của người mua hàng (Khóa ngoại tham chiếu đến bảng người dùng)
    """;
        List<Cart> carts = cartRepository.findAll();
        List<CartAdminResponse> cartAdminResponses = CartAdminResponse.convert(carts);
        createPDFFile(tableName, description, fieldDescriptions, cartAdminResponses);
    }

    private void createCartDetail() {
        String tableName = "cart_details - Chi tiết giỏ hàng";
        String description = "Bảng chứa thông tin chi tiết về các sản phẩm trong giỏ hàng";
        String fieldDescriptions = """
        cartDetailId - Mã của chi tiết giỏ hàng (ID của chi tiết giỏ hàng trong hệ thống)
        cartDetailQuantity - Số lượng sản phẩm (biến thể) trong chi tiết giỏ hàng
        cartDetailUnitPrice - Giá đơn vị của sản phẩm (biến thể)
        cartDetailDiscountCost - Giá trị giảm giá cho sản phẩm (biến thể) này
        cartDetailUnitPriceAfterDiscount - Giá đơn vị sau khi giảm giá
        cartId - Mã của giỏ hàng (Khóa ngoại tham chiếu đến bảng giỏ hàng)
        varId - Mã của biến thể sản phẩm (Khóa ngoại tham chiếu đến bảng biến thể sản phẩm)
    """;
        List<CartDetail> cartDetails = cartDetailRepository.findAll();
        List<CartDetailAdminResponse> cartDetailAdminResponses = CartDetailAdminResponse.convert(cartDetails);
        createPDFFile(tableName, description, fieldDescriptions, cartDetailAdminResponses);
    }

    private void createCatalog() {
        String tableName = "catalogs - Danh mục";
        String description = "Bảng chứa thông tin về các danh mục sản phẩm";
        String fieldDescriptions = """
        catalogId - Mã của danh mục (ID của danh mục trong hệ thống)
        createdAt - Thời gian tạo danh mục
        updatedAt - Thời gian cập nhật danh mục gần nhất
        name - Tên của danh mục
        description - Mô tả chi tiết của danh mục
        parentCatalogId - Mã của danh mục cha (Khóa ngoại tham chiếu đến bảng catalogs, null nếu không có danh mục cha)
    """;
        List<Catalog> catalogs = catalogRepository.findAll();
        List<CatalogAdminResponse> catalogAdminResponses = CatalogAdminResponse.convert(catalogs);
        createPDFFile(tableName, description, fieldDescriptions, catalogAdminResponses);
    }
    private void createCheckin() {
        String tableName = "checkins - Chấm công";
        String description = "Bảng chứa thông tin về các lượt chấm công của nhân viên";
        String fieldDescriptions = """
        id - Mã của lượt chấm công (ID của lượt chấm công trong hệ thống)
        createdAt - Thời gian tạo chấm công
        updatedAt - Thời gian cập nhật chấm công gần nhất
        checkinTime - Thời gian chấm công
        shiftId - Mã của ca làm việc (Khóa ngoại tham chiếu đến bảng Shift)
    """;
        List<Checkin> checkins = checkinRepository.findAll();
        List<CheckinAdminResponse> checkinAdminResponses = CheckinAdminResponse.convert(checkins);
        createPDFFile(tableName, description, fieldDescriptions, checkinAdminResponses);
    }

    private void createFavoriteDrink() {
        String tableName = "favorite_drinks - Đồ uống yêu thích";
        String description = "Bảng chứa thông tin về đồ uống yêu thích của người dùng";
        String fieldDescriptions = """
        userId - Mã của người dùng yêu thích đồ uống (Khóa ngoại tham chiếu đến bảng người dùng)
        productId - Mã của sản phẩm đồ uống được yêu thích (Khóa ngoại tham chiếu đến bảng sản phẩm)
    """;
        List<FavoriteDrink> favoriteDrinks = favoriteDrinkRepository.findAll();
        List<FavoriteDrinkAdminResponse> favoriteDrinkAdminResponses = FavoriteDrinkAdminResponse.convert(favoriteDrinks);
        createPDFFile(tableName, description, fieldDescriptions, favoriteDrinkAdminResponses);
    }
    private void createInventory() {
        String tableName = "inventories - Tồn kho";
        String description = "Bảng chứa thông tin về tồn kho của các nguyên liệu tại chi nhánh";
        String fieldDescriptions = """
        inventoryId - Mã tồn kho của chi nhánh (ID của tồn kho trong hệ thống)
        inventoryQuantity - Số lượng tồn kho của chi nhánh (Số lượng hiện có)
        inventoryExpireDate - Ngày hết hạn của tồn kho (Được tính theo ngày hết hạn của nguyên liệu cộng với thời gian bảo quản)
        branchId - Mã của chi nhánh (Khóa ngoại tham chiếu đến bảng chi nhánh)
        ingredientId - Mã của nguyên liệu (Khóa ngoại tham chiếu đến bảng nguyên liệu)
    """;
        List<Inventory> inventories = inventoryRepository.findAll();
        List<InventoryAdminResponse> inventoryAdminResponses = InventoryAdminResponse.convert(inventories);
        createPDFFile(tableName, description, fieldDescriptions, inventoryAdminResponses);
    }

    private void createInvoice() {
        String tableName = "invoices - Hóa đơn nhập hàng";
        String description = "Bảng chứa thông tin về các hóa đơn nhập kho từ nhà cung cấp";
        String fieldDescriptions = """
        invoiceId - Mã hóa đơn vận chuyển hàng tồn kho (ID của hóa đơn trong hệ thống)
        invoiceDescription - Mô tả hóa đơn (Thông tin chi tiết về đợt nhập hàng)
        invoiceTrackingNumber - Số theo dõi hóa đơn khi vận chuyển (Mã số theo dõi lô hàng)
        invoiceTransferTotalCost - Tổng chi phí vận chuyển hóa đơn (Chi phí vận chuyển)
        supplierId - Mã của nhà cung cấp (Khóa ngoại tham chiếu đến bảng nhà cung cấp)
        warehouseId - Mã của nhà kho (Khóa ngoại tham chiếu đến bảng nhà kho)
    """;
        List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceAdminResponse> invoiceAdminResponses = InvoiceAdminResponse.convert(invoices);
        createPDFFile(tableName, description, fieldDescriptions, invoiceAdminResponses);
    }

    private void createInvoiceDetail() {
        String tableName = "invoice_details - Chi tiết hóa đơn nhập hàng";
        String description = "Bảng chứa thông tin chi tiết về các nguyên liệu trong hóa đơn nhập kho";
        String fieldDescriptions = """
        invoiceId - Mã hóa đơn (Khóa ngoại tham chiếu đến bảng Invoice)
        invoiceDetailQuantity - Số lượng nguyên liệu trong hóa đơn (Số lượng nhập)
        invoiceDetailUnit - Đơn vị của nguyên liệu trong hóa đơn (gram hoặc ml)
        ingredientId - Mã của nguyên liệu (Khóa ngoại tham chiếu đến bảng Ingredient)
    """;
        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAll();
        List<InvoiceDetailAdminResponse> invoiceDetailAdminResponses = InvoiceDetailAdminResponse.convert(invoiceDetails);
        createPDFFile(tableName, description, fieldDescriptions, invoiceDetailAdminResponses);
    }
    private void createIngredient() {
        String tableName = "ingredients - Nguyên liệu";
        String description = "Bảng chứa thông tin về các nguyên liệu sử dụng trong sản phẩm";
        String fieldDescriptions = """
        ingredientId - Mã cung nguyên liệu (ID của nguyên liệu trong hệ thống)
        ingredientName - Tên nguyên liệu
        ingredientDescription - Mô tả nguyên liệu
        ingredientPrice - Giá nguyên liệu
        ingredientType - Loại nguyên liệu (ví dụ: cà phê, sữa, đường, v.v.)
        shelfLifeDays - Thời gian bảo quản nguyên liệu (tính bằng ngày)
    """;
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientAdminResponse> ingredientAdminResponses = IngredientAdminResponse.convert(ingredients);
        createPDFFile(tableName, description, fieldDescriptions, ingredientAdminResponses);
    }
    private void createNotification() {
        String tableName = "notifications - Thông báo";
        String description = "Bảng chứa thông tin về các thông báo trong hệ thống";
        String fieldDescriptions = """
        notificationId - Mã của thông báo (ID của thông báo trong hệ thống)
        notificationType - Loại thông báo (ORDER, PAYMENT, DISCOUNT, SYSTEM, INVENTORY, EMPLOYEE, MANAGER, INVOICE, TRANSFER, BRANCH, SUPPLIER, PRODUCT, WAREHOUSE)
        notificationContent - Nội dung thông báo
        senderId - Mã người gửi (Khóa ngoại tham chiếu đến bảng User, chỉ có manager được gửi thông báo, còn lại thì sender sẽ là null)
        receiverId - Mã người nhận (Khóa ngoại tham chiếu đến bảng User)
        isRead - Thông báo đã được đọc hay chưa (true/false)
    """;
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationAdminResponse> notificationAdminResponses = NotificationAdminResponse.convert(notifications);
        createPDFFile(tableName, description, fieldDescriptions, notificationAdminResponses);
    }

    private void createOrder() {
        String tableName = "orders - Đơn hàng";
        String description = "Bảng chứa thông tin về các đơn hàng trong hệ thống";
        String fieldDescriptions = """
        orderId - Mã của đơn hàng (ID của đơn hàng trong hệ thống)
        userId - Mã của người dùng đã đặt đơn hàng (Khóa ngoại tham chiếu đến bảng người dùng)
        orderStatus - Trạng thái của đơn hàng (PENDING, PROCESSING, COMPLETED, DELIVERING, DELIVERED, CANCELLED)
        orderTotalCostAfterDiscount - Tổng giá trị đơn hàng sau khi áp dụng giảm giá (đã bao gồm thuế)
        orderDiscountCost - Tổng giá trị giảm giá của đơn hàng
        orderTotalCost - Tổng giá trị đơn hàng trước khi áp dụng giảm giá (đã bao gồm thuế)
        orderTrackingNumber - Số theo dõi đơn hàng để khách hàng có thể theo dõi đơn hàng
        employeeId - Mã của nhân viên xử lý đơn hàng (Khóa ngoại tham chiếu đến bảng nhân viên)
        orderPaymentId - Mã của thanh toán của đơn hàng (Khóa ngoại tham chiếu đến bảng thanh toán)
        shippingAddressId - Mã của địa chỉ giao hàng (Khóa ngoại tham chiếu đến bảng địa chỉ giao hàng)
        branchId - Mã của chi nhánh nơi đơn hàng được đặt (Khóa ngoại tham chiếu đến bảng chi nhánh)
        orderCreatedAt - Thời gian tạo đơn hàng
        orderUpdatedAt - Thời gian cập nhật đơn hàng gần nhất
    """;
        List<Order> orders = orderRepository.findAll();
        List<OrderAdminResponse> orderAdminResponses = OrderAdminResponse.convert(orders);
        createPDFFile(tableName, description, fieldDescriptions, orderAdminResponses);
    }

    private void createOrderDetail() {
        String tableName = "order_details - Chi tiết đơn hàng";
        String description = "Bảng chứa thông tin chi tiết về các sản phẩm trong đơn hàng";
        String fieldDescriptions = """
        orderId - Mã đơn hàng (Khóa ngoại tham chiếu đến bảng Order)
        productVariantId - Mã biến thể sản phẩm (Khóa ngoại tham chiếu đến bảng ProductVariant)
        orderDetailQuantity - Số lượng sản phẩm trong đơn hàng
        orderDetailUnitPrice - Đơn giá của biến thể sản phẩm trong đơn hàng
        orderDetailUnitPriceAfterDiscount - Đơn giá của biến thể sản phẩm sau khi áp dụng giảm giá
        orderDetailDiscountCost - Chi phí giảm giá của biến thể sản phẩm trong đơn hàng
    """;
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<OrderDetailAdminResponse> orderDetailAdminResponses = OrderDetailAdminResponse.convert(orderDetails);
        createPDFFile(tableName, description, fieldDescriptions, orderDetailAdminResponses);
    }
    private void createOrderPayment() {
        String tableName = "order_payments - Thanh toán đơn hàng";
        String description = "Bảng chứa thông tin thanh toán của đơn hàng";
        String fieldDescriptions = """
        orderId - Mã đơn hàng (Khóa ngoại tham chiếu đến bảng Order)
        paymentMethodId - Mã phương thức thanh toán (Khóa ngoại tham chiếu đến bảng PaymentMethod)
        amount - Tổng tiền thanh toán (đã áp dụng thuế và giảm giá)
        status - Trạng thái thanh toán (PENDING, COMPLETED, FAILED, REFUNDED)
        transactionId - Mã giao dịch thanh toán (Chỉ có khi thanh toán thành công)
        paypalApprovalUrl - Link thanh toán Paypal (Dành cho thanh toán Paypal)
        paypalPaymentId - Mã Payment PayPal (Dành cho thanh toán Paypal)
        momoResultCode - Mã kết quả của MoMo (Dành cho thanh toán MoMo)
        momoPayUrl - Link thanh toán MoMo (Dành cho thanh toán MoMo)
        momoDeepLink - Deep link MoMo (Dành cho thanh toán MoMo)
        vnpayPayUrl - Link thanh toán VNPay (Dành cho thanh toán VNPay)
        vnpBankCode - Mã ngân hàng VNPay (Dành cho thanh toán VNPay)
        vnpCardType - Loại thẻ VNPay (Dành cho thanh toán VNPay)
        failureReason - Lý do thất bại (Nếu thanh toán thất bại)
        createdAt - Thời gian tạo thanh toán
        updatedAt - Thời gian cập nhật thanh toán gần nhất
    """;
        List<OrderPayment> orderPayments = orderPaymentRepository.findAll();
        List<OrderPaymentAdminResponse> orderPaymentAdminResponses = OrderPaymentAdminResponse.convert(orderPayments);
        createPDFFile(tableName, description, fieldDescriptions, orderPaymentAdminResponses);
    }

    private void createPaymentMethod() {
        String tableName = "payment_methods - Phương thức thanh toán";
        String description = "Bảng chứa thông tin về các phương thức thanh toán được hỗ trợ";
        String fieldDescriptions = """
        paymentMethodId - Mã phương thức thanh toán (ID của phương thức thanh toán)
        paymentMethodName - Tên phương thức thanh toán (CASH, PAYPAL, VNPAY, MOMO, ZALOPAY)
        paymentMethodDescription - Mô tả phương thức thanh toán
        isActive - Trạng thái hoạt động của phương thức thanh toán (true/false)
    """;
        List<PaymentMethods> paymentMethods = paymentMethodsRepository.findAll();
        List<PaymentMethodAdminResponse> paymentMethodAdminResponses = PaymentMethodAdminResponse.convert(paymentMethods);
        createPDFFile(tableName, description, fieldDescriptions, paymentMethodAdminResponses);
    }

    private void createProduct() {
        String tableName = "products - Sản phẩm";
        String description = "Bảng chứa thông tin về các sản phẩm trong hệ thống";
        String fieldDescriptions = """
        productId - Mã sản phẩm (ID của sản phẩm trong hệ thống)
        productName - Tên sản phẩm
        productThumb - Ảnh đại diện sản phẩm (đường dẫn URL)
        productDescription - Mô tả sản phẩm
        productPrice - Giá sản phẩm (tính theo VNĐ)
        productSlug - Slug sản phẩm (dùng để tạo URL thân thiện, được tạo tự động)
        productCommentCount - Số lượng bình luận về sản phẩm
        productRatingsAverage - Điểm đánh giá trung bình của sản phẩm (từ 0.00 đến 5.00)
        productIsPublished - Trạng thái sản phẩm: đã xuất bản hay chưa (true/false)
        productIsDeleted - Trạng thái sản phẩm: đã bị xóa hay chưa (true/false)
        productCategoryId - Mã của loại sản phẩm (Khóa ngoại tham chiếu đến bảng loại sản phẩm)
        productCreatedAt - Thời gian tạo sản phẩm
        productUpdatedAt - Thời gian cập nhật sản phẩm gần nhất
    """;
        List<Product> products = productRepository.findAll();
        List<ProductAdminResponse> productAdminResponses = ProductAdminResponse.convert(products);
        createPDFFile(tableName, description, fieldDescriptions, productAdminResponses);
    }
    private void createProductCategory() {
        String tableName = "product_categories - Loại sản phẩm";
        String description = "Bảng chứa thông tin về các loại sản phẩm trong hệ thống";
        String fieldDescriptions = """
        categoryId - Mã loại sản phẩm (ID của loại sản phẩm)
        categoryName - Tên loại sản phẩm
        categoryDescription - Mô tả loại sản phẩm
        catalogId - Mã danh mục sản phẩm (Khóa ngoại tham chiếu đến bảng danh mục sản phẩm)
        createdAt - Thời gian tạo loại sản phẩm
        updatedAt - Thời gian cập nhật loại sản phẩm
    """;
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        List<ProductCategoryAdminResponse> productCategoryAdminResponses = ProductCategoryAdminResponse.convert(productCategories);
        createPDFFile(tableName, description, fieldDescriptions, productCategoryAdminResponses);
    }

    private void createProductVariant() {
        String tableName = "product_variants - Biến thể sản phẩm";
        String description = "Bảng chứa thông tin về các biến thể của sản phẩm";
        String fieldDescriptions = """
        variantId - Mã biến thể sản phẩm (ID của biến thể trong hệ thống)
        variantTierIdx - Chỉ mục của biến thể (small, medium, large, default)
        variantDefault - Biến thể có phải là mặc định hay không (true nếu small hoặc default)
        variantSlug - Slug của biến thể sản phẩm (tạo tự động từ tên sản phẩm + chỉ mục biến thể)
        variantSort - Sắp xếp của biến thể (small/default: 1, medium: 2, large: 3)
        variantPrice - Giá của biến thể sản phẩm
        variantIsPublished - Trạng thái xuất bản của biến thể (true/false)
        variantIsDeleted - Trạng thái xóa của biến thể (true/false)
        productId - Mã của sản phẩm (Khóa ngoại tham chiếu đến bảng sản phẩm)
    """;
        List<ProductVariant> productVariants = productVariantRepository.findAll();
        List<ProductVariantAdminResponse> productVariantAdminResponses = ProductVariantAdminResponse.convert(productVariants);
        createPDFFile(tableName, description, fieldDescriptions, productVariantAdminResponses);
    }

    private void createRecipe() {
        String tableName = "recipes - Công thức";
        String description = "Bảng chứa thông tin về công thức sản xuất các sản phẩm";
        String fieldDescriptions = """
        productVariantId - Mã sản phẩm biến thể (Khóa ngoại tham chiếu đến ProductVariant)
        ingredientId - Mã nguyên liệu (Khóa ngoại tham chiếu đến Ingredient)
        recipeQuantity - Số lượng nguyên liệu trong công thức
        recipeUnit - Đơn vị của nguyên liệu trong công thức (gram hoặc ml)
        recipeIsTopping - Biến thể công thức có phải là topping hay không (true/false)
    """;
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeAdminResponse> recipeAdminResponses = RecipeAdminResponse.convert(recipes);
        createPDFFile(tableName, description, fieldDescriptions, recipeAdminResponses);
    }
    private void createRole() {
        String tableName = "roles - Vai trò";
        String description = "Bảng chứa thông tin về các vai trò trong hệ thống";
        String fieldDescriptions = """
        roleId - Mã định danh của vai trò (ID của vai trò trong hệ thống)
        name - Tên của vai trò (ADMIN: quản trị viên hệ thống, EMPLOYEE: nhân viên chi nhánh, MANAGER: quản lý chi nhánh, CUSTOMER: khách hàng)
    """;
        List<Role> roles = roleRepository.findAll();
        List<RoleAdminResponse> roleAdminResponses = RoleAdminResponse.convert(roles);
        createPDFFile(tableName, description, fieldDescriptions, roleAdminResponses);
    }

    private void createSalary() {
        String tableName = "salaries - Lương";
        String description = "Bảng chứa thông tin về lương của nhân viên";
        String fieldDescriptions = """
        salaryId - Mã định danh của bảng lương (ID của bảng lương trong hệ thống)
        employeeId - Mã định danh của nhân viên (Khóa ngoại tham chiếu đến bảng nhân viên)
        month - Tháng của bảng lương
        year - Năm của bảng lương
        monthSalary - Lương tháng của nhân viên
        createdAt - Thời gian tạo bản ghi
        updatedAt - Thời gian cập nhật bản ghi gần nhất
    """;
        List<Salary> salaries = salaryRepository.findAll();
        List<SalaryAdminResponse> salaryAdminResponses = SalaryAdminResponse.convert(salaries);
        createPDFFile(tableName, description, fieldDescriptions, salaryAdminResponses);
    }

    private void createShift() {
        String tableName = "shifts - Ca làm việc";
        String description = "Bảng chứa thông tin về ca làm việc của nhân viên";
        String fieldDescriptions = """
        shiftId - Mã định danh của ca làm việc (ID của ca làm việc trong hệ thống)
        shiftStartTime - Thời gian bắt đầu của ca làm việc
        shiftEndTime - Thời gian kết thúc của ca làm việc
        employeeId - Mã định danh của nhân viên (Khóa ngoại tham chiếu đến bảng nhân viên)
        dayOfWeek - Ngày trong tuần của ca làm việc (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)
        month - Tháng của ca làm việc
        year - Năm của ca làm việc
        shiftSalary - Lương của ca làm việc
    """;
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftAdminResponse> shiftAdminResponses = ShiftAdminResponse.convert(shifts);
        createPDFFile(tableName, description, fieldDescriptions, shiftAdminResponses);
    }

    private void createShippingAddresses() {
        String tableName = "shipping_addresses - Địa chỉ giao hàng";
        String description = "Bảng chứa thông tin về địa chỉ giao hàng của người dùng";
        String fieldDescriptions = """
        addressId - Mã định danh của địa chỉ giao hàng (ID của địa chỉ trong hệ thống)
        addressLine - Địa chỉ giao hàng chi tiết
        addressCity - Thành phố của địa chỉ giao hàng
        addressDistrict - Quận/huyện của địa chỉ giao hàng
        addressIsDefault - Trạng thái địa chỉ giao hàng có phải là địa chỉ mặc định hay không (true/false)
        userId - Mã định danh của người dùng (Khóa ngoại tham chiếu đến bảng người dùng)
    """;
        List<ShippingAddresses> shippingAddresses = shippingAddressesRepository.findAll();
        List<ShippingAddressAdminResponse> shippingAddressAdminResponses = ShippingAddressAdminResponse.convert(shippingAddresses);
        createPDFFile(tableName, description, fieldDescriptions, shippingAddressAdminResponses);
    }

    private void createStock() {
        String tableName = "stocks - Tồn kho nhà kho";
        String description = "Bảng chứa thông tin về tồn kho của các nguyên liệu tại nhà kho";
        String fieldDescriptions = """
        warehouseId - Mã định danh của kho hàng (Khóa ngoại tham chiếu đến bảng Warehouse)
        ingredientId - Mã định danh của nguyên liệu (Khóa ngoại tham chiếu đến bảng Ingredient)
        stockQuantity - Số lượng nguyên liệu trong kho
        stockUnit - Đơn vị của nguyên liệu trong kho (gram hoặc ml)
    """;
        List<Stock> stocks = stockRepository.findAll();
        List<StockAdminResponse> stockAdminResponses = StockAdminResponse.convert(stocks);
        createPDFFile(tableName, description, fieldDescriptions, stockAdminResponses);
    }
    private void createSubCheckin() {
        String tableName = "sub_checkins - Chấm công thay thế";
        String description = "Bảng chứa thông tin về các lượt chấm công thay thế của nhân viên";
        String fieldDescriptions = """
        shiftId - Mã định danh của lượt chấm công (ID của lượt chấm công trong hệ thống)
        employeeId - Mã định danh của nhân viên chấm công thay thế (Khóa ngoại tham chiếu đến bảng nhân viên)
        checkinTime - Thời gian chấm công thay thế
    """;
        List<SubCheckin> subCheckins = subCheckinRepository.findAll();
        List<SubCheckinAdminResponse> subCheckinAdminResponses = SubCheckinAdminResponse.convert(subCheckins);
        createPDFFile(tableName, description, fieldDescriptions, subCheckinAdminResponses);
    }

    private void createSupplier() {
        String tableName = "suppliers - Nhà cung cấp";
        String description = "Bảng chứa thông tin về các nhà cung cấp nguyên liệu";
        String fieldDescriptions = """
        supplierId - Mã định danh của nhà cung cấp (ID của nhà cung cấp trong hệ thống)
        supplierName - Tên của nhà cung cấp
        supplierPhone - Số điện thoại của nhà cung cấp
        supplierEmail - Email của nhà cung cấp
        supplierAddress - Địa chỉ của nhà cung cấp
        createdAt - Thời gian tạo bản ghi nhà cung cấp
        updatedAt - Thời gian cập nhật bản ghi nhà cung cấp gần nhất
    """;
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierAdminResponse> supplierAdminResponses = SupplierAdminResponse.convert(suppliers);
        createPDFFile(tableName, description, fieldDescriptions, supplierAdminResponses);
    }

    private void createTransfer() {
        String tableName = "transfers - Phiếu chuyển kho";
        String description = "Bảng chứa thông tin về các phiếu chuyển kho từ nhà kho đến chi nhánh";
        String fieldDescriptions = """
        transferId - Mã định danh của phiếu chuyển kho (ID của phiếu chuyển kho trong hệ thống)
        warehouseId - Mã định danh của kho hàng nơi được chuyển đi (Khóa ngoại tham chiếu đến bảng kho)
        branchId - Mã định danh của chi nhánh nơi được chuyển đến (Khóa ngoại tham chiếu đến bảng chi nhánh)
        transferDescription - Mô tả của phiếu chuyển kho
        transferTrackingNumber - Mã theo dõi của phiếu chuyển kho
        transferTotalCost - Tổng chi phí của phiếu chuyển kho
        createdTime - Thời gian tạo phiếu chuyển kho
        updatedTime - Thời gian cập nhật phiếu chuyển kho gần nhất
    """;
        List<Transfer> transfers = transferRepository.findAll();
        List<TransferAdminResponse> transferAdminResponses = TransferAdminResponse.convert(transfers);
        createPDFFile(tableName, description, fieldDescriptions, transferAdminResponses);
    }
    private void createTransferDetail() {
        String tableName = "transfer_details - Chi tiết phiếu chuyển kho";
        String description = "Bảng chứa thông tin chi tiết về các nguyên liệu trong phiếu chuyển kho";
        String fieldDescriptions = """
        transferId - Mã của phiếu chuyển kho (Khóa ngoại tham chiếu đến Transfer)
        ingredientId - Mã của nguyên liệu (Khóa ngoại tham chiếu đến Ingredient)
        transferDetailQuantity - Số lượng nguyên liệu trong phiếu chuyển kho
        transferDetailUnit - Đơn vị của nguyên liệu trong phiếu chuyển kho (gram hoặc ml)
    """;
        List<TransferDetail> transferDetails = transferDetailRepository.findAll();
        List<TransferDetailAdminResponse> transferDetailAdminResponses = TransferDetailAdminResponse.convert(transferDetails);
        createPDFFile(tableName, description, fieldDescriptions, transferDetailAdminResponses);
    }

    private void createUsedDiscount() {
        String tableName = "used_discounts - Phiếu giảm giá đã sử dụng";
        String description = "Bảng chứa thông tin về các phiếu giảm giá đã được sử dụng trong đơn hàng";
        String fieldDescriptions = """
        discountId - Mã định danh của phiếu giảm giá (Khóa ngoại tham chiếu đến bảng Discount)
        orderDetailId - Mã định danh của chi tiết đơn hàng (Khóa ngoại tham chiếu đến bảng OrderDetail)
        timeUsed - Số lần sử dụng phiếu giảm giá
    """;
        List<UsedDiscount> usedDiscounts = usedDiscountRepository.findAll();
        List<UsedDiscountAdminResponse> usedDiscountAdminResponses = UsedDiscountAdminResponse.convert(usedDiscounts);
        createPDFFile(tableName, description, fieldDescriptions, usedDiscountAdminResponses);
    }

    private void createWarehouse() {
        String tableName = "warehouses - Nhà kho";
        String description = "Bảng chứa thông tin về các nhà kho trong hệ thống";
        String fieldDescriptions = """
        warehouseId - Mã định danh của nhà kho (ID của nhà kho trong hệ thống)
        warehouseName - Tên của nhà kho
        warehousePhone - Số điện thoại của nhà kho
        warehouseEmail - Email của nhà kho
        warehouseAddress - Địa chỉ của nhà kho
    """;
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<WarehouseAdminResponse> warehouseAdminResponses = WarehouseAdminResponse.convert(warehouses);
        createPDFFile(tableName, description, fieldDescriptions, warehouseAdminResponses);
    }
    private void createComment() {
        String tableName = "comments - Bình luận";
        String description = "Bảng chứa thông tin về các bình luận và đánh giá sản phẩm";
        String fieldDescriptions = """
        commentId - Mã của bình luận (ID của bình luận trong hệ thống)
        createdAt - Thời gian tạo bình luận
        updatedAt - Thời gian cập nhật bình luận gần nhất
        commentContent - Nội dung bình luận
        commentLeft - Vị trí bên trái trong cây bình luận
        commentRight - Vị trí bên phải trong cây bình luận
        commentIsDeleted - Trạng thái xóa của bình luận (true/false)
        commentRating - Đánh giá sao của bình luận, giá trị từ 0.00 đến 5.00
        productId - Mã của sản phẩm được bình luận (Khóa ngoại tham chiếu đến bảng sản phẩm)
    """;
        List<Comment> comments = commentRepository.findAll();
        List<CommentAdminResponse> commentAdminResponses = CommentAdminResponse.convert(comments);
        createPDFFile(tableName, description, fieldDescriptions, commentAdminResponses);
    }

    private void createDiscountProductVariant() {
        String tableName = "discount_product_variants - Sản phẩm biến thể áp dụng giảm giá";
        String description = "Bảng chứa thông tin về các biến thể sản phẩm được áp dụng phiếu giảm giá";
        String fieldDescriptions = """
        discountId - Mã của phiếu giảm giá (Khóa ngoại tham chiếu đến Discount)
        variantId - Mã của biến thể sản phẩm (Khóa ngoại tham chiếu đến ProductVariant)
    """;

        // Get all discounts
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountProductVariantAdminResponse> discountProductVariantResponses = new ArrayList<>();

        // Extract the many-to-many relationships
        for (Discount discount : discounts) {
            // Get variants associated with this discount (assuming discount has getProductVariants() method)
            if (discount.getProductVariants() != null) {
                for (ProductVariant variant : discount.getProductVariants()) {
                    // Create a response object for each discount-variant pair
                    DiscountProductVariantAdminResponse response = new DiscountProductVariantAdminResponse();
                    response.setDiscountId(discount.getId().toString());
                    response.setVariantId(variant.getId().toString());
                    discountProductVariantResponses.add(response);
                }
            }
        }

        createPDFFile(tableName, description, fieldDescriptions, discountProductVariantResponses);
    }
    private void createDiscount() {
        String tableName = "discounts - Phiếu giảm giá";
        String description = "Bảng chứa thông tin về các phiếu giảm giá trong hệ thống";
        String fieldDescriptions = """
        discountId - Mã của phiếu giảm giá (ID của phiếu giảm giá trong hệ thống)
        createdAt - Thời gian tạo phiếu giảm giá
        updatedAt - Thời gian cập nhật phiếu giảm giá
        discountName - Tên của phiếu giảm giá
        discountDescription - Mô tả về phiếu giảm giá
        discountType - Loại phiếu giảm giá (PERCENTAGE: phần trăm, AMOUNT: tiền cố định)
        discountValue - Giá trị của phiếu giảm giá (phần trăm hoặc tiền cố định, nếu là tiền cố định thì giá trị theo VNĐ)
        discountCode - Mã của phiếu giảm giá (để khách hàng nhập khi thanh toán)
        discountStartDate - Ngày bắt đầu áp dụng phiếu giảm giá
        discountEndDate - Ngày kết thúc áp dụng phiếu giảm giá
        discountMaxUsers - Số lượng người dùng tối đa có thể sử dụng phiếu giảm giá này
        discountUserCount - Số lượng người dùng đã sử dụng phiếu giảm giá này
        discountMaxPerUser - Số lần sử dụng tối đa của mỗi người dùng
        discountMinOrderValue - Giá trị đơn hàng tối thiểu để áp dụng phiếu giảm giá
        discountIsActive - Trạng thái của phiếu giảm giá (còn hiệu lực hay không)
        branchId - Mã của chi nhánh áp dụng phiếu giảm giá (Khóa ngoại tham chiếu đến bảng chi nhánh)
    """;
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountAdminResponse> discountAdminResponses = DiscountAdminResponse.convert(discounts);
        createPDFFile(tableName, description, fieldDescriptions, discountAdminResponses);
    }
    private void createEmployee() {
        String tableName = "employees - Nhân viên";
        String description = "Bảng chứa thông tin về các nhân viên làm việc tại các chi nhánh";
        String fieldDescriptions = """
        employeeId - Mã nhân viên (ID của nhân viên trong hệ thống)
        createdAt - Thời gian tạo bản ghi nhân viên
        updatedAt - Thời gian cập nhật bản ghi nhân viên gần nhất
        employeeHireDate - Ngày tuyển dụng nhân viên
        branchId - Mã chi nhánh nhân viên làm việc (Khóa ngoại tham chiếu đến bảng chi nhánh)
        userId - Mã người dùng của nhân viên (Khóa ngoại tham chiếu đến bảng người dùng)
        managedBranchId - Mã chi nhánh mà nhân viên quản lý nếu nhân viên là quản lý (Khóa ngoại tham chiếu đến bảng chi nhánh)
    """;
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeAdminResponse> employeeAdminResponses = EmployeeAdminResponse.convert(employees);
        createPDFFile(tableName, description, fieldDescriptions, employeeAdminResponses);
    }
    private void createUser() {
        String tableName = "users - Người dùng";
        String description = "Bảng chứa thông tin về người dùng trong hệ thống";
        String fieldDescriptions = """
        userId - Mã người dùng (ID của người dùng trong hệ thống)
        email - Email của người dùng
        password - Mật khẩu của người dùng (đã được mã hóa)
        name - Tên của người dùng
        lastName - Họ của người dùng
        avatar - Ảnh đại diện của người dùng (đường dẫn URL)
        phoneNumber - Số điện thoại của người dùng
        gender - Giới tính của người dùng
        birthDate - Ngày sinh của người dùng
        roleId - Mã vai trò của người dùng (Khóa ngoại tham chiếu đến bảng vai trò)
        createdAt - Ngày tạo người dùng
        updatedAt - Thời gian cập nhật người dùng gần nhất
    """;
        List<User> users = userRepository.findAll();
        List<UserAdminResponse> userAdminResponses = UserAdminResponse.convert(users);
        createPDFFile(tableName, description, fieldDescriptions, userAdminResponses);
    }


    private void createPDFFile(String tableName, String description, String fieldDescriptions, List<?> data) {
        try {
            // Create directory if it doesn't exist
            Path directory = Paths.get(pathToKnowledge);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String filePath = pathToKnowledge + "/" + tableName.split(" - ")[0] + ".pdf";

            try (PDDocument document = new PDDocument()) {
                // Load Unicode font from resources
                InputStream fontStream = getClass().getResourceAsStream("/static/assets/font/Arial-Unicode.ttf");
                if (fontStream == null) {
                    throw new IOException("Could not find font file in resources");
                }
                PDFont unicodeFont = PDType0Font.load(document, fontStream);

                float margin = 50;
                float pageWidth = PDRectangle.A4.getWidth();
                float maxTextWidth = pageWidth - 2 * margin;
                float yPosition = 0;
                PDPage currentPage = null;
                PDPageContentStream contentStream = null;

                // Create first page
                currentPage = new PDPage(PDRectangle.A4);
                document.addPage(currentPage);
                yPosition = currentPage.getMediaBox().getHeight() - margin;
                contentStream = new PDPageContentStream(document, currentPage);
                contentStream.setFont(unicodeFont, 8);  // Set font initially

                // Write table name
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Tên bảng: " + tableName);
                contentStream.endText();
                yPosition -= 20;

                // Write description
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Mô tả: " + description);
                contentStream.endText();
                yPosition -= 20;

                // Set font for field descriptions
                contentStream.setFont(unicodeFont, 8);

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Thuộc tính bảng:");
                contentStream.endText();
                yPosition -= 15;

                String[] fieldLines = fieldDescriptions.split("\n");
                for (String line : fieldLines) {
                    if (yPosition < margin) {
                        contentStream.close();
                        currentPage = new PDPage(PDRectangle.A4);
                        document.addPage(currentPage);
                        contentStream = new PDPageContentStream(document, currentPage);
                        contentStream.setFont(unicodeFont, 8);
                        yPosition = currentPage.getMediaBox().getHeight() - margin;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + 10, yPosition);
                    contentStream.showText(line.trim());
                    contentStream.endText();
                    yPosition -= 15;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Số lượng bản ghi trong bảng: " + data.size() );
                contentStream.endText();
                yPosition -= 15;

                // Write data
                yPosition -= 10;
                contentStream.setFont(unicodeFont, 8);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Dữ liệu:");
                contentStream.endText();
                yPosition -= 15;

                for (Object item : data) {
                    // Convert object to JSON format with indentation
                    String jsonItem = toJsonFormat(item);

                    // Split the JSON by lines to write it line by line
                    String[] jsonLines = jsonItem.split("\n");

                    // Check if we need to start a new page
                    if (yPosition - (jsonLines.length * 15) < margin) {
                        contentStream.close();
                        currentPage = new PDPage(PDRectangle.A4);
                        document.addPage(currentPage);
                        contentStream = new PDPageContentStream(document, currentPage);
                        contentStream.setFont(unicodeFont, 8);
                        yPosition = currentPage.getMediaBox().getHeight() - margin;
                    }

                    // Write each line of the JSON representation
                    for (String line : jsonLines) {
                        try {
                            line = line.trim();
                            // Handle long lines by wrapping text
                            if (line.length() > 0) {
                                List<String> wrappedLines = wrapText(line, unicodeFont, 8, maxTextWidth - 10);
                                for (String wrappedLine : wrappedLines) {
                                    if (yPosition < margin) {
                                        contentStream.close();
                                        currentPage = new PDPage(PDRectangle.A4);
                                        document.addPage(currentPage);
                                        contentStream = new PDPageContentStream(document, currentPage);
                                        contentStream.setFont(unicodeFont, 8);
                                        yPosition = currentPage.getMediaBox().getHeight() - margin;
                                    }

                                    contentStream.beginText();
                                    contentStream.newLineAtOffset(margin + 10, yPosition);
                                    contentStream.showText(wrappedLine);
                                    contentStream.endText();
                                    yPosition -= 15;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error writing line: " + line);
                            e.printStackTrace();
                        }
                    }

                    // Add an extra line between items
                    yPosition -= 5;
                }

                contentStream.close();
                document.save(filePath);
                System.out.println("PDF created successfully at: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }

    /**
     * Wrap text to fit within a specified width
     */
    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        float stringWidth = 0;

        StringBuilder currentLine = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float charWidth = font.getStringWidth(String.valueOf(c)) / 1000 * fontSize;

            if (c == ' ') {
                lastSpace = i;
            }

            if (stringWidth + charWidth > maxWidth) {
                if (lastSpace != -1) {
                    // Cut at the last space
                    currentLine.setLength(lastSpace - (i - currentLine.length()));
                    i = lastSpace;
                } else if (currentLine.length() > 0) {
                    // If no space, just cut at the current position
                    i--;
                } else {
                    // If the line is empty, force add the current character
                    currentLine.append(c);
                    i++;
                }

                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
                stringWidth = 0;
                lastSpace = -1;
            } else {
                currentLine.append(c);
                stringWidth += charWidth;
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    /**
     * Converts an object to a formatted JSON-like string representation
     */
    private String toJsonFormat(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                Object value = field.get(obj);
                String valueStr = (value == null) ? "null" :
                        (value instanceof String) ? "\"" + value + "\"" :
                                value.toString();

                sb.append("  \"").append(field.getName()).append("\": ").append(valueStr);

                if (i < fields.length - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        sb.append("}");
        return sb.toString();
    }
}
