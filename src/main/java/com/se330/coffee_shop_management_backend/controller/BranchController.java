package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.branch.BranchCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.branch.BranchUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.branch.BranchIdWithRevenueResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.branch.BranchResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.service.branchservices.IBranchService;
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

import java.math.BigDecimal;
import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/branch")
public class BranchController {

    private final IBranchService branchService;

    public BranchController(IBranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get branch detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branch",
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
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BranchResponseDTO>> findByIdBranch(@PathVariable UUID id) {
        BranchResponseDTO branch = BranchResponseDTO.convert(branchService.findByIdBranch(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Branch retrieved successfully",
                        branch
                )
        );
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all branches with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branch list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BranchResponseDTO>> findAllBranches(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Branch> branchPages = branchService.findAllBranches(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Branches retrieved successfully",
                        BranchResponseDTO.convert(branchPages.getContent()),
                        new PageResponse.PagingResponse(
                                branchPages.getNumber(),
                                branchPages.getSize(),
                                branchPages.getTotalElements(),
                                branchPages.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Create new branch",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Branch created successfully",
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
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Branch already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BranchResponseDTO>> createBranch(@RequestBody BranchCreateRequestDTO branchRequestDTO) {
        BranchResponseDTO branch = BranchResponseDTO.convert(branchService.createBranch(branchRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Branch created successfully",
                        branch
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Update branch",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Branch updated successfully",
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
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BranchResponseDTO>> updateBranch(@RequestBody BranchUpdateRequestDTO branchRequestDTO) {
        BranchResponseDTO branch = BranchResponseDTO.convert(branchService.updateBranch(branchRequestDTO));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Branch updated successfully",
                        branch
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Delete branch",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Branch deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/revenue/year")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @Operation(
            summary = "Get branch total revenue by year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branch revenue",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Requires MANAGER role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BranchIdWithRevenueResponseDTO>> getcreatedAtByYear(
            @RequestParam UUID branchId,
            @RequestParam int year
    ) {
        BranchIdWithRevenueResponseDTO revenue = branchService.getTotalOrderCostByBranchAndYear(branchId, year);
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Branch revenue retrieved successfully",
                        revenue
                )
        );
    }

    @GetMapping("/revenue/month-year")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(
            summary = "Get branch total revenue by month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branch revenue",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Requires MANAGER role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BigDecimal>> getcreatedAtByMonthAndYear(
            @RequestParam UUID branchId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        BigDecimal revenue = branchService.getTotalOrderCostByBranchAndMonthAndYear(branchId, month, year);
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Branch revenue retrieved successfully",
                        revenue
                )
        );
    }

    @GetMapping("/revenue/day-month-year")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(
            summary = "Get branch total revenue by day, month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved branch revenue",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied - Requires MANAGER role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<BigDecimal>> getcreatedAtByDayMonthAndYear(
            @RequestParam UUID branchId,
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year
    ) {
        BigDecimal revenue = branchService.getTotalOrderCostByBranchAndDayAndMonthAndYear(branchId, day, month, year);
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Branch revenue retrieved successfully",
                        revenue
                )
        );
    }

    @GetMapping("/all-with-revenue/year")
    @Operation(
            summary = "Get all branches with revenue by year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<BranchIdWithRevenueResponseDTO>> findAllBranchesWithRevenueByYear(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam int year
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BranchIdWithRevenueResponseDTO> branchPages = branchService.findAllBranchesWithRevenueWithYear(pageable, year);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Branches with revenue retrieved successfully",
                        branchPages.getContent(),
                        new PageResponse.PagingResponse(
                                branchPages.getNumber(),
                                branchPages.getSize(),
                                branchPages.getTotalElements(),
                                branchPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/all-with-revenue/month-year")
    @Operation(
            summary = "Get all branches with revenue by month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<BranchIdWithRevenueResponseDTO>> findAllBranchesWithRevenueByMonthYear(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam int month,
            @RequestParam int year
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BranchIdWithRevenueResponseDTO> branchPages = branchService.findAllBranchesWithRevenueWithMonthYear(pageable, month, year);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Branches with revenue retrieved successfully",
                        branchPages.getContent(),
                        new PageResponse.PagingResponse(
                                branchPages.getNumber(),
                                branchPages.getSize(),
                                branchPages.getTotalElements(),
                                branchPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/all-with-revenue/day-month-year")
    @Operation(
            summary = "Get all branches with revenue by day, month and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<BranchIdWithRevenueResponseDTO>> findAllBranchesWithRevenueByDayMonthYear(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BranchIdWithRevenueResponseDTO> branchPages = branchService.findAllBranchesWithRevenueWithDayMonthYear(pageable, day, month, year);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Branches with revenue retrieved successfully",
                        branchPages.getContent(),
                        new PageResponse.PagingResponse(
                                branchPages.getNumber(),
                                branchPages.getSize(),
                                branchPages.getTotalElements(),
                                branchPages.getTotalPages()
                        )
                )
        );
    }
}