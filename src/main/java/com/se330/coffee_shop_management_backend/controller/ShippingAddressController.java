package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shippingaddresses.ShippingAddressesUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.shippingadresses.ShippingAddressesResponseDTO;
import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.service.shippingaddresses.IShippingAddressesService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/shipping-address")
public class ShippingAddressController {

    private final IShippingAddressesService shippingAddressService;

    public ShippingAddressController(IShippingAddressesService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'EMPLOYEE', 'MANAGER')")
    @Operation(
            summary = "Get shipping address detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved shipping address",
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
                            description = "Shipping address not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ShippingAddressesResponseDTO>> findById(@PathVariable UUID id) {
        ShippingAddressesResponseDTO address = ShippingAddressesResponseDTO.convert(shippingAddressService.findByIdShippingAddresses(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Shipping address retrieved successfully",
                        address
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'EMPLOYEE', 'MANAGER')")
    @Operation(
            summary = "Get all shipping addresses with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved shipping address list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ShippingAddressesResponseDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<ShippingAddresses> addressPage = shippingAddressService.findAllShippingAddresses(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Shipping addresses retrieved successfully",
                        ShippingAddressesResponseDTO.convert(addressPage.getContent()),
                        new PageResponse.PagingResponse(
                                addressPage.getNumber(),
                                addressPage.getSize(),
                                addressPage.getTotalElements(),
                                addressPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new shipping address",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Shipping address created successfully",
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
    public ResponseEntity<SingleResponse<ShippingAddressesResponseDTO>> create(@RequestBody ShippingAddressesCreateRequestDTO dto) {
        ShippingAddressesResponseDTO address = ShippingAddressesResponseDTO.convert(shippingAddressService.createShippingAddresses(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Shipping address created successfully",
                        address
                )
        );
    }

    @PatchMapping("/")
    @Operation(
            summary = "Update shipping address",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Shipping address updated successfully",
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
                            description = "Shipping address not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ShippingAddressesResponseDTO>> update(@RequestBody ShippingAddressesUpdateRequestDTO dto) {
        ShippingAddressesResponseDTO address = ShippingAddressesResponseDTO.convert(shippingAddressService.updateShippingAddresses(dto));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Shipping address updated successfully",
                        address
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'EMPLOYEE', 'MANAGER')")
    @Operation(
            summary = "Delete shipping address",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Shipping address deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shipping address not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        shippingAddressService.deleteShippingAddresses(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'EMPLOYEE', 'MANAGER')")
    @Operation(
            summary = "Get all shipping addresses for current user with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user's shipping addresses",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ShippingAddressesResponseDTO>> findAllByMe(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<ShippingAddresses> addressPage = shippingAddressService.findAllShippingAddressesByMe(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "User shipping addresses retrieved successfully",
                        ShippingAddressesResponseDTO.convert(addressPage.getContent()),
                        new PageResponse.PagingResponse(
                                addressPage.getNumber(),
                                addressPage.getSize(),
                                addressPage.getTotalElements(),
                                addressPage.getTotalPages()
                        )
                )
        );
    }
}