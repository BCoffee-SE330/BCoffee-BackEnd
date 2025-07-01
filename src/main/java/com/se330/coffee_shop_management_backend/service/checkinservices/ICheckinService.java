package com.se330.coffee_shop_management_backend.service.checkinservices;

import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.CheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.SubCheckinCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.checkin.SubCheckinUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Checkin;
import com.se330.coffee_shop_management_backend.entity.SubCheckin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICheckinService {
    Checkin findById(UUID id);
    Page<Checkin> findAll(Pageable pageable);
    Page<Checkin> findAllByShiftId(UUID shiftId, Pageable pageable);
    Page<Checkin> findAllByEmployeeId(UUID employeeId, Pageable pageable);
    Page<SubCheckin> findAllSubCheckinsByShiftId(UUID shiftId, Pageable pageable);
    SubCheckin createSubCheckin(SubCheckinCreateRequestDTO subCheckinCreateRequestDTO);
    SubCheckin updateSubCheckin(SubCheckinUpdateRequestDTO subCheckinUpdateRequestDTO);
    Page<Checkin> findAllByBranchId(Pageable pageable);
    Page<Checkin> findAllByShiftIdAndYear(UUID shiftId, int year, Pageable pageable);
    Page<Checkin> findAllByShiftIdAndYearAndMonth(UUID shiftId, int year, int month, Pageable pageable);
    Page<Checkin> findAllByShiftIdAndYearAndMonthAndDay(UUID shiftId, int year, int month, int day, Pageable pageable);
    Page<Checkin> findAllByEmployeeIdAndYear(UUID employeeId, int year, Pageable pageable);
    Page<Checkin> findAllByEmployeeIdAndYearAndMonth(UUID employeeId, int year, int month, Pageable pageable);
    Page<Checkin> findAllByEmployeeIdAndYearAndMonthAndDay(UUID employeeId, int year, int month, int day, Pageable pageable);
    Page<Checkin> findAllByBranchIdAndYear(int year, Pageable pageable);
    Page<Checkin> findAllByBranchIdAndYearAndMonth(int year, int month, Pageable pageable);
    Page<Checkin> findAllByBranchIdAndYearAndMonthAndDay(int year, int month, int day, Pageable pageable);
    Checkin create(CheckinCreateRequestDTO checkinCreateRequestDTO);
    Checkin update(CheckinUpdateRequestDTO checkinUpdateRequestDTO);
    void delete(UUID id);


    boolean isCheckin(UUID shiftId, int day, int month, int year);
}