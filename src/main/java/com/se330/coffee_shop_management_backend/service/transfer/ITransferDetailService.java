package com.se330.coffee_shop_management_backend.service.transfer;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferDetailUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.TransferDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITransferDetailService {
    TransferDetail findByIdTransferDetail(UUID id);
    Page<TransferDetail> findAllTransferDetails(Pageable pageable);
    TransferDetail createTransferDetail(TransferDetailCreateRequestDTO transferDetailCreateRequestDTO);
    TransferDetail updateTransferDetail(TransferDetailUpdateRequestDTO transferDetailUpdateRequestDTO);
    void deleteTransferDetail(UUID id);
}
