package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    private final TransferDao transferDao;

    @Autowired
    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
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

    @Autowired
    private TransferService transferService;

    @PutMapping("/{transferId}/updateStatus")
    public ResponseEntity<TransferDTO> updateTransferStatus(@PathVariable int transferId, @RequestParam String action) {
        System.out.println("Received request - Transfer ID: " + transferId + ", Action: " + action);
        try {
            TransferDTO updatedTransfer = transferService.updateTransferStatus(transferId, action);
            return ResponseEntity.ok(updatedTransfer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{transferId}/approve")
    public ResponseEntity<TransferDTO> approveTransfer(@PathVariable int transferId){

        try {
            TransferDTO updatedTranfer = transferService.updateTransferStatus(transferId, "approve");
            return ResponseEntity.ok(updatedTranfer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{transferId}/reject")
    public ResponseEntity<TransferDTO> rejectTransfer(@PathVariable int transferId){

        try {
            TransferDTO updatedTranfer = transferService.updateTransferStatus(transferId, "reject");
            return ResponseEntity.ok(updatedTranfer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }





}
