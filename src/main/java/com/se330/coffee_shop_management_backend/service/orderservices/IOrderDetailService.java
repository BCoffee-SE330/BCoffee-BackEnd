package com.se330.coffee_shop_management_backend.service.orderservices;

import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.order.OrderDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IOrderDetailService {
    OrderDetail findByIdOrderDetail(UUID id);
    Page<OrderDetail> findAllByIdOrder(UUID orderId, Pageable pageable);
    Page<OrderDetail> findAllOrderDetails(Pageable pageable);
    OrderDetail createOrderDetail(OrderDetailCreateRequestDTO orderDetailCreateRequestDTO);
    OrderDetail updateOrderDetail(OrderDetailUpdateRequestDTO orderDetailUpdateRequestDTO);
    void deleteOrderDetail(UUID id);
}