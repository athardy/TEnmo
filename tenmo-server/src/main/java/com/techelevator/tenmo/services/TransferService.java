package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public static final int APPROVED_STATUS_ID = 2;
    public static final int REJECTED_STATUS_ID = 3;

    public TransferService(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    public TransferDTO createTransfer(CreateTransferDTO createTransferDTO) {
        return transferDao.createTransfer(createTransferDTO);
    }

    public List<TransferDTO> getTransfersByUserId(int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    public TransferDTO getTransferDetails(int transferId) {
        return transferDao.getTransferDetails(transferId);
    }

    @Transactional
    public TransferDTO updateTransferStatus(int transferId, String action) {
        TransferDTO transfer = transferDao.getTransferById(transferId);

        if (!action.equalsIgnoreCase("approve") && !action.equalsIgnoreCase("reject")) {
            throw new IllegalArgumentException("Invalid action parameter");
        }

        if (transfer.getTransferStatus() == null || !transfer.getTransferStatus().equals("Pending")) {
            throw new IllegalArgumentException("Only pending transfers can be updated.");
        }

        if (action.equalsIgnoreCase("approve")) {
            BigDecimal accountBalance = accountDao.getBalanceByUserId(transfer.getAccountFrom());
            if (accountBalance.compareTo(transfer.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            transfer.setTransferStatusId(APPROVED_STATUS_ID);
            transferDao.updateTransferStatus(transferId, APPROVED_STATUS_ID);
            accountDao.updateBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        } else if (action.equalsIgnoreCase("reject")) {
            transfer.setTransferStatusId(REJECTED_STATUS_ID);
            transferDao.updateTransferStatus(transferId, REJECTED_STATUS_ID);
        } else {
            throw new IllegalArgumentException("Invalid input.");
        }
        return transfer;
    }

}
