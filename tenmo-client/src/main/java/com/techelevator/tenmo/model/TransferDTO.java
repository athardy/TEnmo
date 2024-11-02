package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    // Unique identifier for the transfer.
    private int transferId;

    // Identifier for the type of transfer (e.g., send or request).
    private int transferTypeId;

    // Identifier for the status of the transfer (e.g., pending, approved, rejected).
    private int transferStatusId;

    // Account ID from which the funds are transferred.
    private int accountFrom;

    // Account ID to which the funds are transferred.
    private int accountTo;

    // Amount of money involved in the transfer.
    private BigDecimal amount;

    // Username of the account sending the funds.
    private String fromUsername;

    // Username of the account receiving the funds.
    private String toUsername;

    // Descriptive status of the transfer (e.g., "Approved" or "Pending").
    private String transferStatus;

    // Descriptive type of the transfer (e.g., "Send" or "Request").
    private String transferType;

    // Returns the descriptive type of the transfer.
    public String getTransferType() {
        return transferType;
    }

    // Sets the descriptive type of the transfer.
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    // Returns the transfer ID.
    public int getTransferId() {
        return transferId;
    }

    // Sets the transfer ID.
    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    // Returns the type ID for the transfer.
    public int getTransferTypeId() {
        return transferTypeId;
    }

    // Sets the type ID for the transfer.
    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    // Returns the status ID for the transfer.
    public int getTransferStatusId() {
        return transferStatusId;
    }

    // Sets the status ID for the transfer.
    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    // Returns the account ID from which funds are transferred.
    public int getAccountFrom() {
        return accountFrom;
    }

    // Sets the account ID from which funds are transferred.
    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    // Returns the account ID to which funds are transferred.
    public int getAccountTo() {
        return accountTo;
    }

    // Sets the account ID to which funds are transferred.
    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    // Returns the amount of the transfer.
    public BigDecimal getAmount() {
        return amount;
    }

    // Sets the amount of the transfer.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns the username of the sender.
    public String getFromUsername() {
        return fromUsername;
    }

    // Sets the username of the sender.
    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    // Returns the username of the recipient.
    public String getToUsername() {
        return toUsername;
    }

    // Sets the username of the recipient.
    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    // Returns the descriptive status of the transfer.
    public String getTransferStatus() {
        return transferStatus;
    }

    // Sets the descriptive status of the transfer.
    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}

