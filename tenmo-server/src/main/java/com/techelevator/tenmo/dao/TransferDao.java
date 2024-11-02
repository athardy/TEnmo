package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;

import java.util.List;

/**
 * Interface defining data access methods for transfer-related operations.
 */
public interface TransferDao {

    /**
     * Creates a new transfer based on the provided details.
     *
     * @param createTransferDTO Data Transfer Object containing details for the new transfer,
     *                          such as sender and receiver account IDs, transfer amount, and type.
     * @return TransferDTO representing the created transfer, including the transfer ID and status.
     */
    TransferDTO createTransfer(CreateTransferDTO createTransferDTO);

    /**
     * Retrieves a list of all transfers associated with a specific user ID.
     *
     * @param userId ID of the user whose transfers are requested.
     * @return List of TransferDTO objects, each representing a transfer involving the user
     *         as either the sender or receiver.
     */
    List<TransferDTO> getTransfersByUserId(int userId);

    /**
     * Retrieves detailed information about a specific transfer by its ID.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO containing detailed information about the specified transfer,
     *         including the sender and receiver details, amount, status, and type.
     */
    TransferDTO getTransferDetails(int transferId);

    /**
     * Retrieves basic information about a transfer by its ID.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO with basic details of the specified transfer, such as
     *         transfer ID, type, status, and involved accounts.
     */
    TransferDTO getTransferById(int transferId);

    /**
     * Retrieves all pending transfers for a specific user.
     *
     * @param userId ID of the user whose pending transfers are requested.
     * @return List of TransferDTOs representing transfers with a pending status
     *         involving the specified user as either the sender or receiver.
     */
    List<TransferDTO> getPendingTransfers(int userId);

    /**
     * Updates the status of a specific transfer.
     *
     * @param transferId ID of the transfer to update.
     * @param statusId New status ID to assign to the transfer, indicating
     *                 whether it is approved, pending, or rejected.
     */
    void updateTransferStatus(int transferId, int statusId);
}
