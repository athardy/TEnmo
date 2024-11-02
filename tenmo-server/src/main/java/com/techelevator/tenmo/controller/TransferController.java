package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    // DAO objects for handling transfer and account operations
    private final TransferDao transferDao;
    private final AccountDao accountDao;

    // Transfer status constants
    private static final int APPROVED_STATUS_ID = 2;
    private static final int REJECTED_STATUS_ID = 3;
    private static final int PENDING_STATUS_ID = 1;

    // Constructor for injecting DAOs
    @Autowired
    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    /**
     * Creates a new transfer based on the provided data.
     *
     * @param createTransferDTO Details for the new transfer.
     * @return ResponseEntity with the created TransferDTO and 201 status.
     */
    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(@RequestBody CreateTransferDTO createTransferDTO) {
        TransferDTO newTransfer = transferDao.createTransfer(createTransferDTO);
        return new ResponseEntity<>(newTransfer, HttpStatus.CREATED);
    }

    /**
     * Retrieves all transfers for a specified user ID.
     *
     * @param userId ID of the user whose transfers are requested.
     * @return List of TransferDTO for the user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByUserId(@PathVariable int userId) {
        List<TransferDTO> transfers = transferDao.getTransfersByUserId(userId);
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    /**
     * Retrieves detailed information for a specific transfer.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO if found, otherwise 404 status.
     */
    @GetMapping("/details/{transferId}")
    public ResponseEntity<TransferDTO> getTransferDetails(@PathVariable int transferId) {
        TransferDTO transfer = transferDao.getTransferDetails(transferId);
        if (transfer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    /**
     * Retrieves a transfer by its ID.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO if found, otherwise 404 status.
     */
    @GetMapping("/{transferId}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable int transferId) {
        TransferDTO transfer = transferDao.getTransferById(transferId);
        return transfer != null ? ResponseEntity.ok(transfer) : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all pending transfers for a specific user.
     *
     * @param userId ID of the user to retrieve pending transfers for.
     * @return List of pending TransferDTOs.
     */
    @GetMapping("/pending/{userId}")
    public List<TransferDTO> getPendingTransfers(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }

    /**
     * Approves a pending transfer if funds are available and the transfer exists.
     *
     * @param transferId ID of the transfer to approve.
     * @return ResponseEntity with the approved TransferDTO or relevant status.
     */
    @PutMapping("/{transferId}/approve")
    public ResponseEntity<?> approveTransfer(@PathVariable int transferId) {

        TransferDTO transfer = transferDao.getTransferById(transferId); // Retrieve transfer by ID

        if (transfer == null) {
            return ResponseEntity.notFound().build();
        }

        if (transfer.getTransferStatusId() != PENDING_STATUS_ID) {
            return ResponseEntity.badRequest().body(null); // Reject if not pending
        }

        // Retrieve account balance and verify it meets transfer amount
        BigDecimal accountBalance = accountDao.getBalanceByAccountId(transfer.getAccountFrom());
        if (accountBalance == null || accountBalance.compareTo(transfer.getAmount()) < 0) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        }

        // Approve transfer and update balances
        transfer.setTransferStatusId(APPROVED_STATUS_ID);
        transferDao.updateTransferStatus(transferId, APPROVED_STATUS_ID);
        accountDao.updateBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        return ResponseEntity.ok(transfer);
    }

    /**
     * Rejects a pending transfer.
     *
     * @param transferId ID of the transfer to reject.
     * @return ResponseEntity with the rejected TransferDTO or relevant status.
     */
    @PutMapping("/{transferId}/reject")
    public ResponseEntity<TransferDTO> rejectTransfer(@PathVariable int transferId) {

        TransferDTO transfer = transferDao.getTransferById(transferId); // Retrieve transfer by ID

        if (transfer == null) {
            return ResponseEntity.notFound().build();
        }

        if (transfer.getTransferStatusId() != PENDING_STATUS_ID) {
            return ResponseEntity.badRequest().body(null); // Reject if not pending
        }

        // Set status to rejected and update the database
        transfer.setTransferStatusId(REJECTED_STATUS_ID);
        transferDao.updateTransferStatus(transferId, REJECTED_STATUS_ID);

        return ResponseEntity.ok(transfer);
    }

}

