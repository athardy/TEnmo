package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class CreateTransferDTO {

    // Account ID from which funds will be transferred.
    private int accountFrom;

    // Account ID to which funds will be transferred.
    private int accountTo;

    // The amount of money to transfer.
    private BigDecimal amount;

    // The ID representing the type of transfer (e.g., send or request).
    private int transferTypeId;

    // The ID representing the status of the transfer (e.g., pending, approved).
    private int transferStatusId;

    // Returns the account ID from which funds will be transferred.
    public int getAccountFrom() {
        return accountFrom;
    }

    // Sets the account ID from which funds will be transferred.
    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    // Returns the account ID to which funds will be transferred.
    public int getAccountTo() {
        return accountTo;
    }

    // Sets the account ID to which funds will be transferred.
    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    // Returns the amount of money to transfer.
    public BigDecimal getAmount() {
        return amount;
    }

    // Sets the amount of money to transfer.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns the transfer type ID, representing the type of transfer.
    public int getTransferTypeId() {
        return transferTypeId;
    }

    // Sets the transfer type ID, representing the type of transfer.
    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    // Returns the transfer status ID, representing the current status of the transfer.
    public int getTransferStatusId() {
        return transferStatusId;
    }

    // Sets the transfer status ID, representing the current status of the transfer.
    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }
}
