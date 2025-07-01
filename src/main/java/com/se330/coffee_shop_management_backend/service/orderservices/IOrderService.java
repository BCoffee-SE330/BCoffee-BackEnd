package com.se330.coffee_shop_management_backend.service.orderservices;

import com.se330.coffee_shop_management_backend.dto.request.cart.EmployeeCartRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.EmployeeOrderRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Order;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface IOrderService {
    Order findByIdOrder(UUID id);
    Page<Order> findAllOrders(Pageable pageable);
    Page<Order> findAllOrderByCustomerId(Pageable pageable);
    Page<Order> findAllOrderByStatusAndBranchId(Constants.OrderStatusEnum status, Pageable pageable);
    Order updateOrder(OrderUpdateRequestDTO orderUpdateRequestDTO);
    Order updateOrderStatus(UUID id, Constants.OrderStatusEnum status) throws UnsupportedEncodingException;
    Order createOrder(OrderCreateRequestDTO orderCreateRequestDTO) throws UnsupportedEncodingException;
    Order createOrderForEmployee(EmployeeOrderRequestDTO employeeOrderRequestDTO) throws UnsupportedEncodingException;
    void deleteOrder(UUID id);
}