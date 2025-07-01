package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.SubCheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.SubCheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.checkin.CheckinResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.checkin.SubCheckinResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Checkin;
import com.se330.coffee_shop_management_backend.entity.SubCheckin;
import com.se330.coffee_shop_management_backend.service.checkinservices.ICheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final ICheckinService checkinService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE', 'ADMIN')")
    @Operation(
            summary = "Get checkin detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkin",
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
                            description = "Checkin not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<CheckinResponseDTO>> findByIdCheckin(@PathVariable UUID id) {
        CheckinResponseDTO checkin = CheckinResponseDTO.convert(checkinService.findById(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Checkin retrieved successfully",
                        checkin
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE', 'ADMIN')")
    @Operation(
            summary = "Get all checkins with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkin list",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "vi") String lan,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAll(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Create new checkin",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Checkin created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CheckinResponseDTO.class)
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
                    )
            }
    )
    public ResponseEntity<SingleResponse<CheckinResponseDTO>> create(@RequestBody CheckinCreateRequestDTO checkinCreateRequestDTO) {
        CheckinResponseDTO checkin = CheckinResponseDTO.convert(checkinService.create(checkinCreateRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Checkin created successfully",
                        checkin
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Update checkin",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Checkin updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CheckinResponseDTO.class)
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
                            description = "Checkin not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<CheckinResponseDTO>> update(@RequestBody CheckinUpdateRequestDTO checkinUpdateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Checkin created successfully",
                        CheckinResponseDTO.convert(checkinService.update(checkinUpdateRequestDTO))
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Delete checkin",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Checkin deleted successfully"
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
                            description = "Checkin not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        checkinService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ShiftId with date components
    @GetMapping("/shift/{shiftId}/year/{year}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by shift and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByShiftIdAndYear(
            @PathVariable UUID shiftId,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByShiftIdAndYear(shiftId, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/shift/{shiftId}/year/{year}/month/{month}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by shift, year and month",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByShiftIdAndYearAndMonth(
            @PathVariable UUID shiftId,
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByShiftIdAndYearAndMonth(shiftId, year, month, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/shift/{shiftId}/year/{year}/month/{month}/day/{day}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by shift, year, month and day",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByShiftIdAndYearAndMonthAndDay(
            @PathVariable UUID shiftId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByShiftIdAndYearAndMonthAndDay(shiftId, year, month, day, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    // EmployeeId with date components
    @GetMapping("/employee/{employeeId}/year/{year}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by employee and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByEmployeeIdAndYear(
            @PathVariable UUID employeeId,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByEmployeeIdAndYear(employeeId, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/employee/{employeeId}/year/{year}/month/{month}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by employee, year and month",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByEmployeeIdAndYearAndMonth(
            @PathVariable UUID employeeId,
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByEmployeeIdAndYearAndMonth(employeeId, year, month, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/employee/{employeeId}/year/{year}/month/{month}/day/{day}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get checkins by employee, year, month and day",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByEmployeeIdAndYearAndMonthAndDay(
            @PathVariable UUID employeeId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByEmployeeIdAndYearAndMonthAndDay(employeeId, year, month, day, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    // BranchId with date components
    @GetMapping("/branch/year/{year}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @Operation(
            summary = "Get checkins by branch and year",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByBranchIdAndYear(
            
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByBranchIdAndYear(year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/branch/year/{year}/month/{month}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @Operation(
            summary = "Get checkins by branch, year and month",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByBranchIdAndYearAndMonth(
            
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByBranchIdAndYearAndMonth(year, month, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/branch/year/{year}/month/{month}/day/{day}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @Operation(
            summary = "Get checkins by branch, year, month and day",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkins",
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
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllByBranchIdAndYearAndMonthAndDay(
            
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPages = checkinService.findAllByBranchIdAndYearAndMonthAndDay(year, month, day, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPages.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPages.getNumber(),
                                checkinPages.getSize(),
                                checkinPages.getTotalElements(),
                                checkinPages.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/checkins/shift/{shiftId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get all checkins for a specific shift",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkin list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid shift ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllCheckinsByShiftId(
            @PathVariable UUID shiftId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPage = checkinService.findAllByShiftId(shiftId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPage.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPage.getNumber(),
                                checkinPage.getSize(),
                                checkinPage.getTotalElements(),
                                checkinPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/checkins/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get all checkins for a specific employee",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkin list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid employee ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllCheckinsByEmployeeId(
            @PathVariable UUID employeeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPage = checkinService.findAllByEmployeeId(employeeId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPage.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPage.getNumber(),
                                checkinPage.getSize(),
                                checkinPage.getTotalElements(),
                                checkinPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/checkins/branch")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Get all checkins for a specific branch",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved checkin list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid branch ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<CheckinResponseDTO>> findAllCheckinsByBranchId(
            
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Checkin> checkinPage = checkinService.findAllByBranchId(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Checkins retrieved successfully",
                        CheckinResponseDTO.convert(checkinPage.getContent()),
                        new PageResponse.PagingResponse(
                                checkinPage.getNumber(),
                                checkinPage.getSize(),
                                checkinPage.getTotalElements(),
                                checkinPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/subcheckin/shift/{shiftId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'EMPLOYEE')")
    @Operation(
            summary = "Get all subcheckins for a specific shift",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved subcheckin list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid shift ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<SubCheckinResponseDTO>> findAllSubCheckinsByShiftId(
            @PathVariable UUID shiftId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<SubCheckin> subCheckinPage = checkinService.findAllSubCheckinsByShiftId(shiftId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "SubCheckins retrieved successfully",
                        SubCheckinResponseDTO.convert(subCheckinPage.getContent()),
                        new PageResponse.PagingResponse(
                                subCheckinPage.getNumber(),
                                subCheckinPage.getSize(),
                                subCheckinPage.getTotalElements(),
                                subCheckinPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/subcheckin")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Create new subcheckin",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "SubCheckin created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SubCheckinResponseDTO.class)
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
    public ResponseEntity<SingleResponse<SubCheckinResponseDTO>> createSubCheckin(
            @RequestBody SubCheckinCreateRequestDTO subCheckinCreateRequestDTO
    ) {
        SubCheckinResponseDTO subCheckin = SubCheckinResponseDTO.convert(
                checkinService.createSubCheckin(subCheckinCreateRequestDTO)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "SubCheckin created successfully",
                        subCheckin
                )
        );
    }

    @PatchMapping("/subcheckin")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Update subcheckin",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SubCheckin updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SubCheckinResponseDTO.class)
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
                            description = "SubCheckin not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<SubCheckinResponseDTO>> updateSubCheckin(
            @RequestBody SubCheckinUpdateRequestDTO subCheckinUpdateRequestDTO
    ) {
        SubCheckinResponseDTO subCheckin = SubCheckinResponseDTO.convert(
                checkinService.updateSubCheckin(subCheckinUpdateRequestDTO)
        );
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "SubCheckin updated successfully",
                        subCheckin
                )
        );
    }
}