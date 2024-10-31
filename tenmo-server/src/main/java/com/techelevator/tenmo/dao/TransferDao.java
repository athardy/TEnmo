package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;

import java.util.List;

public interface TransferDao {
    TransferDTO createTransfer(CreateTransferDTO createTransferDTO);

    List<TransferDTO> getTransfersByUserId(int userId);

    TransferDTO getTransferDetails(int transferId);

    TransferDTO getTransferById(int transferId);

    List<TransferDTO> getPendingTransfers(int userId);

}
