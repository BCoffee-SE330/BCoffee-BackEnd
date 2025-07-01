package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.invoice.InvoiceDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.invoice.InvoiceDetailResponseDTO;
import com.se330.coffee_shop_management_backend.entity.InvoiceDetail;
import com.se330.coffee_shop_management_backend.service.invoiceservices.IInvoiceDetailService;
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

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/invoice-details")
public class InvoiceDetailController {

    private final IInvoiceDetailService invoiceDetailService;

    public InvoiceDetailController(IInvoiceDetailService invoiceDetailService) {
        this.invoiceDetailService = invoiceDetailService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get invoice detail by id",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved invoice detail",
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
                            description = "Invoice detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<InvoiceDetailResponseDTO>> findByIdInvoiceDetail(@PathVariable UUID id) {
        InvoiceDetailResponseDTO detail = InvoiceDetailResponseDTO.convert(invoiceDetailService.findByIdInvoiceDetail(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Invoice detail retrieved successfully",
                        detail
                )
        );
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all invoice details with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved invoice detail list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<InvoiceDetailResponseDTO>> findAllInvoiceDetails(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<InvoiceDetail> detailPages = invoiceDetailService.findAllInvoiceDetails(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Invoice details retrieved successfully",
                        InvoiceDetailResponseDTO.convert(detailPages.getContent()),
                        new PageResponse.PagingResponse(
                                detailPages.getNumber(),
                                detailPages.getSize(),
                                detailPages.getTotalElements(),
                                detailPages.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new invoice detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Invoice detail created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<InvoiceDetailResponseDTO>> createInvoiceDetail(@RequestBody InvoiceDetailCreateRequestDTO invoiceDetailCreateRequestDTO) {
        InvoiceDetailResponseDTO detail = InvoiceDetailResponseDTO.convert(invoiceDetailService.createInvoiceDetail(invoiceDetailCreateRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Invoice detail created successfully",
                        detail
                )
        );
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update invoice detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invoice detail updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invoice detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<InvoiceDetailResponseDTO>> updateInvoiceDetail(@RequestBody InvoiceDetailUpdateRequestDTO invoiceDetailUpdateRequestDTO) {
        InvoiceDetailResponseDTO detail = InvoiceDetailResponseDTO.convert(invoiceDetailService.updateInvoiceDetail(invoiceDetailUpdateRequestDTO));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Invoice detail updated successfully",
                        detail
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete invoice detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Invoice detail deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invoice detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteInvoiceDetail(@PathVariable UUID id) {
        invoiceDetailService.deleteInvoiceDetail(id);
        return ResponseEntity.noContent().build();
    }
}