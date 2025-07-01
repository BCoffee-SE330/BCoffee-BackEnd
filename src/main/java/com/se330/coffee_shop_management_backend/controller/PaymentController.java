package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.payment.OrderPaymentCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.payment.MomoIPNRequest;
import com.se330.coffee_shop_management_backend.dto.response.payment.OrderPaymentResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.payment.PaymentMethodResponseDTO;
import com.se330.coffee_shop_management_backend.entity.OrderPayment;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.service.paymentservices.IOrderPaymentService;
import com.se330.coffee_shop_management_backend.service.paymentservices.IPaymentMethodService;
import com.se330.coffee_shop_management_backend.util.Constants;
import feign.Body;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentMethodService paymentMethodService;
    private final IOrderPaymentService orderPaymentService;

    public PaymentController(
            IPaymentMethodService paymentMethodService,
            IOrderPaymentService orderPaymentService
    ){
        this.paymentMethodService = paymentMethodService;
        this.orderPaymentService = orderPaymentService;
    }

    @GetMapping("/payment-method/detail/{id}")
    @Operation(
            summary = "Get payment method detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved payment",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Payment not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<PaymentMethodResponseDTO>> findByIdPaymentMethod(@PathVariable UUID id) {
        PaymentMethodResponseDTO paymentMethod = PaymentMethodResponseDTO.convert(paymentMethodService.findByIdPaymentMethod(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Payment retrieved successfully",
                        paymentMethod
                )
        );
    }

    @GetMapping("/payment-method/all")
    @Operation(
            summary = "Get all payment methods with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved payment list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<PaymentMethodResponseDTO>> findAllPaymentMethods(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<PaymentMethods> paymentMethodPage = paymentMethodService.findAllPaymentMethods(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Payments retrieved successfully",
                        PaymentMethodResponseDTO.convert(paymentMethodPage.getContent()),
                        new PageResponse.PagingResponse(
                                paymentMethodPage.getNumber(),
                                paymentMethodPage.getSize(),
                                paymentMethodPage.getTotalElements(),
                                paymentMethodPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/order/by-order/{orderId}")
    @Operation(
            summary = "Get order payment by order ID",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved order payment",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid order ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order payment not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<OrderPaymentResponseDTO>> findByOrderId(@PathVariable UUID orderId) {
        OrderPayment orderPayment = orderPaymentService.findByIdOrder(orderId);
        if (orderPayment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SingleResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Order payment not found for order: " + orderId,
                            null
                    ));
        }
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Order payment retrieved successfully",
                        OrderPaymentResponseDTO.convert(orderPayment)
                )
        );
    }

    @PostMapping("/order")
    @Operation(
            summary = "Create order payment",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order payment created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<OrderPaymentResponseDTO>> createOrderPayment(
            @RequestBody OrderPaymentCreateRequestDTO orderPaymentCreateRequestDTO) throws UnsupportedEncodingException {
        OrderPayment orderPayment = orderPaymentService.createOrderPayment(orderPaymentCreateRequestDTO);
        return new ResponseEntity<>(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Order payment created successfully",
                        OrderPaymentResponseDTO.convert(orderPayment)
                ),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/order/{id}/status")
    @Operation(
            summary = "Update order payment status",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order payment status updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format or status",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order payment not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<OrderPaymentResponseDTO>> updateOrderPaymentStatus(
            @PathVariable UUID id,
            @RequestParam Constants.PaymentStatusEnum status) {
        OrderPayment orderPayment = orderPaymentService.updateOrderPaymentStatus(id, status);
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Order payment status updated successfully",
                        OrderPaymentResponseDTO.convert(orderPayment)
                )
        );
    }

    @GetMapping("/order/all")
    @Operation(
            summary = "Get all order payments with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved order payment list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<OrderPaymentResponseDTO>> findAllOrderPayments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<OrderPayment> orderPaymentPage = orderPaymentService.findAllOrderPayments(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Order payments retrieved successfully",
                        OrderPaymentResponseDTO.convert(orderPaymentPage.getContent()),
                        new PageResponse.PagingResponse(
                                orderPaymentPage.getNumber(),
                                orderPaymentPage.getSize(),
                                orderPaymentPage.getTotalElements(),
                                orderPaymentPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/order/customer/{customerId}")
    @Operation(
            summary = "Get all order payments for a customer with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved customer order payment list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid customer ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<OrderPaymentResponseDTO>> findAllOrderPaymentsByCustomerId(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<OrderPayment> orderPaymentPage = orderPaymentService.findAllOrderPaymentsByCustomerId(customerId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Customer order payments retrieved successfully",
                        OrderPaymentResponseDTO.convert(orderPaymentPage.getContent()),
                        new PageResponse.PagingResponse(
                                orderPaymentPage.getNumber(),
                                orderPaymentPage.getSize(),
                                orderPaymentPage.getTotalElements(),
                                orderPaymentPage.getTotalPages()
                        )
                )
        );
    }


    @GetMapping("/paypal/cancel")
    public ResponseEntity<SingleResponse<String>> paypalCancel(String orderId) {
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Paypal payment cancelled",
                        "Payment cancelled for order ID: " + orderId
                )
        );
    }

    @GetMapping("/paypal/success")
    public ResponseEntity<SingleResponse<?>> paypalSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam(value = "PayerID", required = false) String payerId) {

        // Handle the PayPal success callback
        OrderPayment payment = orderPaymentService.executePaypalPayment(paymentId, payerId);

        // Return appropriate response or redirect
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Paypal payment executed successfully",
                        OrderPaymentResponseDTO.convert(payment)
                )
        );
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> momoIpnRequest(@RequestBody MomoIPNRequest momoIPNRequest) {
        // Process the payment
        orderPaymentService.executeMomoPayment(momoIPNRequest);

        // Return 204 No Content - no response body
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vnpay/success")
    @Operation(
            summary = "Handle VNPay payment success callback",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "VNPay payment executed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid payment data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<?>> vnpaySuccess(
            @RequestParam String vnp_BankCode,
            @RequestParam String vnp_CardType,
            @RequestParam String vnp_TransactionNo,
            @RequestParam String vnp_ResponseCode,
            @RequestParam String vnp_TxnRef,
            @RequestParam(required = false) String vnp_Amount,
            @RequestParam(required = false) String vnp_OrderInfo,
            @RequestParam(required = false) String vnp_PayDate,
            @RequestParam(required = false) String vnp_TransactionStatus,
            @RequestParam(required = false) String vnp_TmnCode,
            @RequestParam(required = false) String vnp_SecureHash
    ) {
        OrderPayment payment = orderPaymentService.vnpayExecutePayment(
                vnp_BankCode,
                vnp_CardType,
                vnp_TransactionNo,
                vnp_ResponseCode,
                vnp_TxnRef
        );

        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "VNPay payment executed successfully",
                        OrderPaymentResponseDTO.convert(payment)
                )
        );
    }
}