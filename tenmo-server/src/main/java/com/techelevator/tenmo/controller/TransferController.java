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

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private static final int APPROVED_STATUS_ID = 2;
    private static final int REJECTED_STATUS_ID = 3;
    private static final int PENDING_STATUS_ID = 1;

    @Autowired
    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(@RequestBody CreateTransferDTO createTransferDTO) {
        TransferDTO newTransfer = transferDao.createTransfer(createTransferDTO);
        return new ResponseEntity<>(newTransfer, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransferDTO>> getTransfersByUserId(@PathVariable int userId) {
        List<TransferDTO> transfers = transferDao.getTransfersByUserId(userId);
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    @GetMapping("/details/{transferId}")
    public ResponseEntity<TransferDTO> getTransferDetails(@PathVariable int transferId) {
        TransferDTO transfer = transferDao.getTransferDetails(transferId);
        if (transfer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }


    @GetMapping("/{transferId}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable int transferId) {
        TransferDTO transfer = transferDao.getTransferById(transferId);
        if (transfer != null) {
            return ResponseEntity.ok(transfer);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pending/{userId}")
    public List<TransferDTO> getPendingTransfers(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }

//    @Autowired
//    private TransferService transferService;
//
////    @PutMapping("/{transferId}/updateStatus")
////    public ResponseEntity<TransferDTO> updateTransferStatus(@PathVariable int transferId, @RequestParam String action) {
////        System.out.println("Received request - Transfer ID: " + transferId + ", Action: " + action);
////        try {
////            TransferDTO updatedTransfer = transferService.updateTransferStatus(transferId, action);
////            return ResponseEntity.ok(updatedTransfer);
////        } catch (IllegalArgumentException e) {
////            return ResponseEntity.badRequest().body(null);
////        }
////    }

    @PutMapping("/{transferId}/approve")
    public ResponseEntity<?> approveTransfer(@PathVariable int transferId) {

        TransferDTO transfer = transferDao.getTransferById(transferId);

        if (transfer == null){
            return ResponseEntity.notFound().build();
        }

        if(transfer.getTransferStatusId() != PENDING_STATUS_ID) {
            return ResponseEntity.badRequest().body(null);
        }

//        //checking that you're not sending money to yourself
//        int currentUserId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
//        if (transfer.getAccountTo() != currentUserId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You cannot approve your own transfer requests!!!!! Naughty.");
//        }

        BigDecimal accountBalance = accountDao.getBalanceByAccountId(transfer.getAccountFrom());
        if(accountBalance.equals(null)){
            System.out.println("Account Balance retreival failed or returned empty for user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if(accountBalance.compareTo(transfer.getAmount()) < 0){
            System.out.println("Insufficient Funds.");
            return ResponseEntity.badRequest().body(null);
        }

        transfer.setTransferStatusId(APPROVED_STATUS_ID);
        transferDao.updateTransferStatus(transferId, APPROVED_STATUS_ID);
        accountDao.updateBalances(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        return ResponseEntity.ok(transfer);

    }

    @PutMapping("/{transferId}/reject")
    public ResponseEntity<TransferDTO> rejectTransfer(@PathVariable int transferId) {

        TransferDTO transfer = transferDao.getTransferById(transferId);

        if (transfer == null) {
            return ResponseEntity.notFound().build();
        }

        if (transfer.getTransferStatusId() != PENDING_STATUS_ID) {
            return ResponseEntity.badRequest().body(null);
        }

        transfer.setTransferStatusId(REJECTED_STATUS_ID);
        transferDao.updateTransferStatus(transferId, REJECTED_STATUS_ID);

        return ResponseEntity.ok(transfer);
    }

}
