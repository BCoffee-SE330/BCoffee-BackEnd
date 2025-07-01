package com.se330.coffee_shop_management_backend.service.transfer;

import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.transfer.TransferUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITransferService {
    Transfer findByIdTransfer(UUID id);
    Page<Transfer> findAllTransfers(Pageable pageable);
    Transfer createTransfer(TransferCreateRequestDTO transferCreateRequestDTO);
    Transfer updateTransfer(TransferUpdateRequestDTO transferUpdateRequestDTO);
    void deleteTransfer(UUID id);
}
