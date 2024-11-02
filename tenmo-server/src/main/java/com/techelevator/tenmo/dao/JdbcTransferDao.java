package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.CreateTransferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for handling transfer-related operations using JDBC.
 */
@Component
public class JdbcTransferDao implements TransferDao {

    // Constants for transfer types and statuses
    private static final int SEND_TYPE_ID = 2;
    private static final int REQUEST_TYPE_ID = 1;
    public static final int APPROVED_STATUS_ID = 2;
    private static final int PENDING_STATUS_ID = 1;

    private final JdbcTemplate jdbcTemplate;

    // Constructor for injecting JdbcTemplate dependency
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates a new transfer based on the provided data and updates balances if approved.
     *
     * @param createTransferDTO Data transfer object containing transfer details.
     * @return TransferDTO with full transfer details after creation.
     * @throws IllegalArgumentException if an invalid transfer type is provided.
     */
    @Override
    @Transactional
    public TransferDTO createTransfer(CreateTransferDTO createTransferDTO) {
        int transferTypeId = createTransferDTO.getTransferTypeId();
        int transferStatusId;

        // Determine transfer status based on type
        if (transferTypeId == SEND_TYPE_ID) {
            transferStatusId = APPROVED_STATUS_ID;
        } else if (transferTypeId == REQUEST_TYPE_ID) {
            transferStatusId = PENDING_STATUS_ID;
        } else {
            throw new IllegalArgumentException("Invalid transfer type.");
        }

        // Insert new transfer and retrieve the generated transfer ID
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId,
                createTransferDTO.getAccountFrom(), createTransferDTO.getAccountTo(),
                createTransferDTO.getAmount());

        // If transfer is approved, immediately update balances
        if (transferStatusId == APPROVED_STATUS_ID) {
            updateBalances(createTransferDTO.getAccountFrom(), createTransferDTO.getAccountTo(), createTransferDTO.getAmount());
        }

        // Retrieve and return detailed transfer information
        return getTransferDetails(transferId);
    }

    /**
     * Retrieves a list of transfers associated with a specific user ID.
     *
     * @param userId ID of the user whose transfers are requested.
     * @return List of TransferDTO objects containing transfer details.
     */
    @Override
    public List<TransferDTO> getTransfersByUserId(int userId) {
        // SQL query to retrieve transfers for a specific user, joining related tables to include necessary details
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, ts.transfer_status_desc AS transfer_status, " +
                "t.account_from, u_from.username AS from_username, " +
                "t.account_to, u_to.username AS to_username, " +
                "t.amount " +
                "FROM transfer t " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE a_from.user_id = ? OR a_to.user_id = ?";

        List<TransferDTO> transfers = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        // Iterate through each result row and map it to a TransferDTO object
        while (results.next()) {
            transfers.add(mapRowToTransferDTO(results));
        }
        return transfers;
    }

    /**
     * Retrieves detailed information for a specific transfer by its ID.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO containing detailed transfer information.
     */
    public TransferDTO getTransferDetails(int transferId) {
        // SQL query to fetch detailed information about a transfer by joining relevant tables
        String sql = "SELECT t.transfer_id, t.amount, t.account_from, u_from.username AS from_username, " +
                "t.account_to, u_to.username AS to_username, ts.transfer_status_desc AS transfer_status, " +
                "tt.transfer_type_desc AS transfer_type " +
                "FROM transfer t " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?";

        // Execute query and use lambda to map result set to a TransferDTO object
        return jdbcTemplate.queryForObject(sql, new Object[]{transferId}, (rs, rowNum) -> {
            TransferDTO transfer = new TransferDTO();
            transfer.setTransferId(rs.getInt("transfer_id"));
            transfer.setAccountFrom(rs.getInt("account_from"));
            transfer.setAccountTo(rs.getInt("account_to"));
            transfer.setAmount(rs.getBigDecimal("amount"));
            transfer.setFromUsername(rs.getString("from_username"));
            transfer.setToUsername(rs.getString("to_username"));
            transfer.setTransferStatus(rs.getString("transfer_status"));
            transfer.setTransferType(rs.getString("transfer_type"));

            return transfer;
        });
    }

    /**
     * Maps a row from a SqlRowSet to a TransferDTO object.
     *
     * @param rs SqlRowSet containing the row data.
     * @return TransferDTO with populated fields from the row data.
     */
    private TransferDTO mapRowToTransferDTO(SqlRowSet rs) {
        // Initialize a TransferDTO and populate it with data from the result set
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setFromUsername(rs.getString("from_username"));
        transfer.setToUsername(rs.getString("to_username"));
        transfer.setTransferStatus(rs.getString("transfer_status"));
        return transfer;
    }

    /**
     * Updates the balances of two accounts for a transfer.
     *
     * @param accountFromId ID of the account sending funds.
     * @param accountToId ID of the account receiving funds.
     * @param amount Amount to transfer.
     */
    private void updateBalances(int accountFromId, int accountToId, BigDecimal amount) {
        // SQL statements to adjust balances: deduct from sender and add to receiver
        String deductBalanceSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        String addBalanceSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";

        // Deduct amount from the sender's account
        jdbcTemplate.update(deductBalanceSql, amount, accountFromId);

        // Add amount to the receiver's account
        jdbcTemplate.update(addBalanceSql, amount, accountToId);
    }

    /**
     * Retrieves a transfer by its ID.
     *
     * @param transferId ID of the transfer to retrieve.
     * @return TransferDTO with basic transfer details.
     */
    public TransferDTO getTransferById(int transferId) {
        // SQL query to select basic details of a transfer by its ID
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";

        // Execute the query and map the result set to a TransferDTO using a lambda
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            TransferDTO transfer = new TransferDTO();
            transfer.setTransferId(rs.getInt("transfer_id"));
            transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
            transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
            transfer.setAccountFrom(rs.getInt("account_from"));
            transfer.setAccountTo(rs.getInt("account_to"));
            transfer.setAmount(rs.getBigDecimal("amount"));
            return transfer;
        }, transferId);
    }

    /**
     * Retrieves all pending transfers for a specific user.
     *
     * @param userId ID of the user to retrieve pending transfers for.
     * @return List of TransferDTOs representing pending transfers.
     */
    @Override
    public List<TransferDTO> getPendingTransfers(int userId) {
        // SQL query to select pending transfers involving the specified user
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, ts.transfer_status_desc AS transfer_status, " +
                "t.account_from, u_from.username AS from_username, " +
                "t.account_to, u_to.username AS to_username, t.amount " +
                "FROM transfer t " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE t.transfer_status_id = ? AND (a_from.user_id = ? OR a_to.user_id = ?)";

        List<TransferDTO> pendingTransfers = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, PENDING_STATUS_ID, userId, userId);

        // Iterate through each result row and map it to a TransferDTO object
        while (results.next()) {
            pendingTransfers.add(mapRowToTransferDTO(results));
        }
        return pendingTransfers;
    }

    /**
     * Updates the status of a specific transfer.
     *
     * @param transferId ID of the transfer to update.
     * @param statusId New status ID to set for the transfer.
     */
    @Override
    public void updateTransferStatus(int transferId, int statusId) {
        // SQL query to update the status of a transfer
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";

        // Execute update to set the new status ID for the specified transfer ID
        jdbcTemplate.update(sql, statusId, transferId);
    }
}

