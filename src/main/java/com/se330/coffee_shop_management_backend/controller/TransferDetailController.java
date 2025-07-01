package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.transfer.TransferDetailResponseDTO;
import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import com.se330.coffee_shop_management_backend.service.transfer.ITransferDetailService;
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
@RequestMapping("/transfer-detail")
public class TransferDetailController {

    private final ITransferDetailService transferDetailService;

    public TransferDetailController(ITransferDetailService transferDetailService) {
        this.transferDetailService = transferDetailService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get transfer detail by id",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transfer detail",
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
                            description = "Transfer detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<TransferDetailResponseDTO>> findById(@PathVariable UUID id) {
        TransferDetailResponseDTO detail = TransferDetailResponseDTO.convert(transferDetailService.findByIdTransferDetail(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Transfer detail retrieved successfully",
                        detail
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get all transfer details with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transfer detail list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<TransferDetailResponseDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<TransferDetail> detailPage = transferDetailService.findAllTransferDetails(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Transfer details retrieved successfully",
                        TransferDetailResponseDTO.convert(detailPage.getContent()),
                        new PageResponse.PagingResponse(
                                detailPage.getNumber(),
                                detailPage.getSize(),
                                detailPage.getTotalElements(),
                                detailPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Create new transfer detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transfer detail created successfully",
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
    public ResponseEntity<SingleResponse<TransferDetailResponseDTO>> create(@RequestBody TransferDetailCreateRequestDTO dto) {
        TransferDetailResponseDTO detail = TransferDetailResponseDTO.convert(transferDetailService.createTransferDetail(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Transfer detail created successfully",
                        detail
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Update transfer detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transfer detail updated successfully",
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
                            description = "Transfer detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<TransferDetailResponseDTO>> update(@RequestBody TransferDetailUpdateRequestDTO dto) {
        TransferDetailResponseDTO detail = TransferDetailResponseDTO.convert(transferDetailService.updateTransferDetail(dto));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Transfer detail updated successfully",
                        detail
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Delete transfer detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Transfer detail deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transfer detail not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transferDetailService.deleteTransferDetail(id);
        return ResponseEntity.noContent().build();
    }
}