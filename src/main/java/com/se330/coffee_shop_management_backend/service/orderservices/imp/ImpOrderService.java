package com.se330.coffee_shop_management_backend.service.orderservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.cart.CartDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.cart.EmployeeCartRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.EmployeeOrderRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.cartservices.ICartService;
import com.se330.coffee_shop_management_backend.service.discountservices.IDiscountService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderDetailService;
import com.se330.coffee_shop_management_backend.service.orderservices.IOrderService;
import com.se330.coffee_shop_management_backend.service.paymentservices.IOrderPaymentService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import com.se330.coffee_shop_management_backend.util.CreateTrackingNumber;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImpOrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ICartService cartService;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final IOrderPaymentService orderPaymentService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final IOrderDetailService orderDetailService;
    private final IDiscountService discountService;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final BranchRepository branchRepository;
    private final INotificationService notificationService;


    @Override
    @Transactional(readOnly = true)
    public Order findByIdOrder(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllOrderByCustomerId(Pageable pageable) {
        UUID customerId = userService.getUser().getId();
        return orderRepository.findAllByUser_Id(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllOrderByStatusAndBranchId(Constants.OrderStatusEnum status, Pageable pageable) {
        User currentManager = userService.getUser();
        return orderRepository.findAllByOrderStatusAndBranch_Id(status, currentManager.getEmployee().getBranch().getId(), pageable);
    }

    @Override
    @Transactional
    public Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO) {
        Order existingOrder = orderRepository.findById(orderUpdateRequestDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderUpdateRequestDTO.getOrderId()));

        Employee existingEmployee = userService.getUser().getEmployee();

        existingOrder.setEmployee(existingEmployee);

        if (existingOrder.getUser() != null) {

            if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.CANCELLED) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .notificationContent(CreateNotiContentHelper.createOrderCancelledContent(existingOrder.getId()))
                                .senderId(null)
                                .receiverId(existingOrder.getUser().getId())
                                .isRead(false)
                                .build());
            } else if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.COMPLETED) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .notificationContent(CreateNotiContentHelper.createInStorePurchaseContent(existingOrder.getId()))
                                .senderId(null)
                                .receiverId(existingOrder.getUser().getId())
                                .isRead(false)
                                .build());
            } else if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.PROCESSING) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .notificationContent(CreateNotiContentHelper.createOrderReceivedContent(existingOrder.getId()))
                                .senderId(null)
                                .receiverId(existingOrder.getUser().getId())
                                .isRead(false)
                                .build());
            } else if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.DELIVERING) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .notificationContent(CreateNotiContentHelper.orderDeliveringContent(existingOrder.getId()))
                                .senderId(null)
                                .receiverId(existingOrder.getUser().getId())
                                .isRead(false)
                                .build());
            } else if (orderUpdateRequestDTO.getOrderStatus() == Constants.OrderStatusEnum.DELIVERED) {
                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.ORDER)
                                .notificationContent(CreateNotiContentHelper.orderDeliveredContent(existingOrder.getId()))
                                .senderId(null)
                                .receiverId(existingOrder.getUser().getId())
                                .isRead(false)
                                .build());
            }
        }

        existingOrder.setOrderStatus(orderUpdateRequestDTO.getOrderStatus());

        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID id, Constants.OrderStatusEnum status) throws UnsupportedEncodingException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        User customer = existingOrder.getUser();

        if (customer != null) {
            NotificationCreateRequestDTO.NotificationCreateRequestDTOBuilder notiBuilder =
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .senderId(null)
                            .receiverId(customer.getId())
                            .isRead(false);

            switch (status) {
                case CANCELLED -> notiBuilder.notificationContent(CreateNotiContentHelper.createOrderCancelledContent(existingOrder.getId()));
                case COMPLETED -> notiBuilder.notificationContent(CreateNotiContentHelper.createInStorePurchaseContent(existingOrder.getId()));
                case PROCESSING -> notiBuilder.notificationContent(CreateNotiContentHelper.createOrderReceivedContent(existingOrder.getId()));
                case DELIVERING -> notiBuilder.notificationContent(CreateNotiContentHelper.orderDeliveringContent(existingOrder.getId()));
                case DELIVERED -> notiBuilder.notificationContent(CreateNotiContentHelper.orderDeliveredContent(existingOrder.getId()));
                default -> notiBuilder.notificationContent("Order status updated.");
            }

            notificationService.createNotification(notiBuilder.build());
        }

        existingOrder.setOrderStatus(status);
        return orderRepository.save(existingOrder);
    }

    /**
     * Creates a new order with associated order details and applies available discounts.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates and retrieves required entities (employee, user, shipping address, payment method)</li>
     *   <li>Creates a new Order with initial total cost of zero</li>
     *   <li>Creates OrderDetail entities for each product in the request</li>
     *   <li>Calculates the initial total cost</li>
     *   <li>Applies the most valuable discount to each order detail based on total order value</li>
     *   <li>Recalculates the final total cost after discounts are applied</li>
     *   <li>Updates and returns the completed order</li>
     * </ol>
     *
     * @param orderCreateRequestDTO The data transfer object containing all information needed to create the order:
     *                             employee ID, user ID, shipping address ID, payment method ID, order status,
     *                             tracking number, and list of order details with product variants and quantities
     * @return The fully created and populated Order entity with all relationships established and total cost calculated
     * @throws EntityNotFoundException If any referenced entity (employee, user, shipping address, payment method) cannot be found
     */
    @Override
    @Transactional
    public Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO) throws UnsupportedEncodingException {
        Employee existingEmployee = null;
        Branch existingBranch = null;

        if (orderCreateRequestDTO.getBranchId() != null) {
            existingBranch = branchRepository.findById(orderCreateRequestDTO.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + orderCreateRequestDTO.getBranchId()));
        }

        User existingUser = userService.getUser();

        ShippingAddresses existingShippingAddress = null;
        if (orderCreateRequestDTO.getShippingAddressId() != null) {
            existingShippingAddress = shippingAddressesRepository.findById(orderCreateRequestDTO.getShippingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with id:" + orderCreateRequestDTO.getShippingAddressId()));
        }

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(orderCreateRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id:" + orderCreateRequestDTO.getPaymentMethodId()));
        
        Cart existingCart = cartRepository.findByUser_Id(existingUser.getId());

        // create order first
        Order newOrder = orderRepository.save(
            Order.builder()
                    .employee(existingEmployee)
                    .orderStatus(Constants.OrderStatusEnum.PENDING)
                    .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"))
                    .user(existingUser)
                    .shippingAddress(existingShippingAddress)
                    .orderTotalCost(BigDecimal.ZERO)
                    .orderDiscountCost(BigDecimal.ZERO)
                    .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                    .branch(existingBranch)
                    .oCreatedAt(LocalDateTime.now())
                    .build()
        );

        List<OrderDetailCreateRequestDTO> orderDetailDtos = new ArrayList<>();
        for (CartDetail cartDetailCreateRequestDTO : existingCart.getCartDetails()) {
            OrderDetailCreateRequestDTO orderDetailCreateRequestDTO = new OrderDetailCreateRequestDTO();
            orderDetailCreateRequestDTO.setOrderDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity());
            orderDetailCreateRequestDTO.setProductVariantId(cartDetailCreateRequestDTO.getProductVariant().getId());
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailDtos.add(orderDetailCreateRequestDTO);
        }

        // now then add order details
        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderDetailDtos) {
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailCreateRequestDTO.setBranchId(orderCreateRequestDTO.getBranchId() != null ? orderCreateRequestDTO.getBranchId() : existingBranch.getId());
            newOrder.getOrderDetails().add(orderDetailService.createOrderDetail(orderDetailCreateRequestDTO));
        }

        // Calculate total cost
        BigDecimal totalCost = updateTotalCost(newOrder.getId());
        newOrder.setOrderTotalCost(totalCost);

        // now we loop for each order detail to apply discount for them
        for (OrderDetail orderDetail : newOrder.getOrderDetails()) {
            discountService.applyMostValuableDiscountOfOrderDetail(orderDetail.getId(), totalCost);
        }

        // update again the new total cost since the unit price of some order details have been changed
        totalCost = updateTotalCost(newOrder.getId());
        BigDecimal discountCost = newOrder.getOrderTotalCost().subtract(totalCost);

        // set the new total cost
        newOrder.setOrderDiscountCost(discountCost);
        newOrder.setOrderTotalCostAfterDiscount(totalCost);

        // save order again to update total cost and payment method
        orderRepository.save(newOrder);

        // now create notification
        if (existingShippingAddress != null) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .notificationContent(CreateNotiContentHelper.createOrderSuccessContentCustomer(newOrder.getId()))
                            .senderId(null)
                            .receiverId(newOrder.getUser().getId())
                            .isRead(false)
                            .build());
        } else {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .notificationContent(CreateNotiContentHelper.createInStorePurchaseContent(newOrder.getId()))
                            .senderId(null)
                            .receiverId(newOrder.getUser().getId())
                            .isRead(false)
                            .build());
        }

        Order finalOrder = orderRepository.findById(newOrder.getId()).orElseThrow();

        // now create order payment
        finalOrder.setOrderPayment(orderPaymentService.createOrderPayment(
                OrderPaymentCreateRequestDTO.builder()
                        .orderId(finalOrder.getId())
                        .paymentMethodId(orderCreateRequestDTO.getPaymentMethodId())
                        .amount(finalOrder.getOrderTotalCostAfterDiscount())
                        .build()
        ));

        // delete cart
        cartService.clearCart();

        return finalOrder;
    }

    @Override
    public Order createOrderForEmployee(EmployeeOrderRequestDTO employeeOrderRequestDTO) throws UnsupportedEncodingException {
        Employee currentEmployee = userService.getUser().getEmployee();
        Branch currentBranch = currentEmployee.getBranch();

        User customer = null;

        if (employeeOrderRequestDTO.getUserId() != null) {
            customer = userRepository.findById(employeeOrderRequestDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + employeeOrderRequestDTO.getUserId()));
        }

        PaymentMethods existingPaymentMethod = paymentMethodsRepository.findById(employeeOrderRequestDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found with id: " + employeeOrderRequestDTO.getPaymentMethodId()));

        // create order first
        Order newOrder = orderRepository.save(
                Order.builder()
                        .employee(currentEmployee)
                        .orderStatus(Constants.OrderStatusEnum.PROCESSING)
                        .orderTrackingNumber(CreateTrackingNumber.createTrackingNumber("ORDER"))
                        .user(customer)
                        .shippingAddress(null)
                        .orderTotalCost(BigDecimal.ZERO)
                        .orderDiscountCost(BigDecimal.ZERO)
                        .orderTotalCostAfterDiscount(BigDecimal.ZERO)
                        .branch(currentBranch)
                        .oCreatedAt(LocalDateTime.now())
                        .build()
        );

        List<OrderDetailCreateRequestDTO> orderDetailDtos = new ArrayList<>();
        for (CartDetailCreateRequestDTO cartDetailCreateRequestDTO : employeeOrderRequestDTO.getCartDetails()) {
            OrderDetailCreateRequestDTO orderDetailCreateRequestDTO = new OrderDetailCreateRequestDTO();
            orderDetailCreateRequestDTO.setOrderDetailQuantity(cartDetailCreateRequestDTO.getCartDetailQuantity());
            orderDetailCreateRequestDTO.setProductVariantId(cartDetailCreateRequestDTO.getVariantId());
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailDtos.add(orderDetailCreateRequestDTO);
        }

        // now then add order details
        for (OrderDetailCreateRequestDTO orderDetailCreateRequestDTO : orderDetailDtos) {
            orderDetailCreateRequestDTO.setOrderId(newOrder.getId());
            orderDetailCreateRequestDTO.setBranchId(currentBranch.getId());
            orderDetailService.createOrderDetail(orderDetailCreateRequestDTO);
        }

        // Calculate total cost
        BigDecimal totalCost = updateTotalCost(newOrder.getId());
        newOrder.setOrderTotalCost(totalCost);

        // now we loop for each order detail to apply discount for them
        if (employeeOrderRequestDTO.getUserId() != null) {
            for (OrderDetail orderDetail : newOrder.getOrderDetails()) {
                discountService.applyMostValuableDiscountOfOrderDetail(orderDetail.getId(), totalCost);
            }
        }

        // update again the new total cost since the unit price of some order details have been changed
        totalCost = updateTotalCost(newOrder.getId());
        BigDecimal discountCost = newOrder.getOrderTotalCost().subtract(totalCost);

        // set the new total cost
        newOrder.setOrderDiscountCost(discountCost);
        newOrder.setOrderTotalCostAfterDiscount(totalCost);

        // save order again to update total cost and payment method
        orderRepository.save(newOrder);

        Order finalOrder = orderRepository.findById(newOrder.getId()).orElseThrow();

        // now create order payment
        orderPaymentService.createOrderPayment(
                OrderPaymentCreateRequestDTO.builder()
                        .orderId(finalOrder.getId())
                        .paymentMethodId(employeeOrderRequestDTO.getPaymentMethodId())
                        .amount(finalOrder.getOrderTotalCostAfterDiscount())
                        .build()
        );

        if (customer != null) {
            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.ORDER)
                            .notificationContent(CreateNotiContentHelper.createInStorePurchaseContent(newOrder.getId()))
                            .senderId(null)
                            .receiverId(customer.getId())
                            .isRead(false)
                            .build());
        }

        return finalOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id:" + id));

        if (existingOrder.getUser() != null) {
            existingOrder.getUser().getOrders().remove(existingOrder);
        }

        if (existingOrder.getShippingAddress() != null) {
            existingOrder.getShippingAddress().getOrders().remove(existingOrder);
        }

        if (existingOrder.getEmployee() != null) {
            existingOrder.setEmployee(null);
        }

        orderRepository.delete(existingOrder);
    }


    private BigDecimal updateTotalCost(UUID orderId) {
        BigDecimal totalCostBeforeDiscount = BigDecimal.ZERO;
        BigDecimal totalCostAfterDiscount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder_Id(orderId);
        Order order = orderRepository.findById(orderId).orElseThrow();

        for (OrderDetail orderDetail : orderDetails) {
            // Calculate original cost before discount
            BigDecimal originalUnitPrice = orderDetail.getOrderDetailUnitPrice();
            totalCostBeforeDiscount = totalCostBeforeDiscount.add(
                    originalUnitPrice.multiply(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity())));

            // Calculate cost after discount
            BigDecimal discountedPrice = (orderDetail.getOrderDetailUnitPriceAfterDiscount() != null) ?
                    orderDetail.getOrderDetailUnitPriceAfterDiscount() : originalUnitPrice;

            totalCostAfterDiscount = totalCostAfterDiscount.add(
                    discountedPrice.multiply(BigDecimal.valueOf(orderDetail.getOrderDetailQuantity())));
        }

        // Update order with all discount information
        BigDecimal discountAmount = totalCostBeforeDiscount.subtract(totalCostAfterDiscount);
        order.setOrderTotalCost(totalCostBeforeDiscount);
        order.setOrderDiscountCost(discountAmount);
        order.setOrderTotalCostAfterDiscount(totalCostAfterDiscount);
        orderRepository.save(order);

        return totalCostAfterDiscount;
    }
}
