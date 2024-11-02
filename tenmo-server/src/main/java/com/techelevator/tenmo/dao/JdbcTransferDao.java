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

@Component
public class JdbcTransferDao implements TransferDao {
    private static final int SEND_TYPE_ID = 2;
    private static final int REQUEST_TYPE_ID = 1;

    public static final int APPROVED_STATUS_ID = 2;
    private static final int PENDING_STATUS_ID = 1;

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public TransferDTO createTransfer(CreateTransferDTO createTransferDTO) {
        int transferTypeId = createTransferDTO.getTransferTypeId();
        int transferStatusId;

        if (transferTypeId == SEND_TYPE_ID) {
            transferStatusId = APPROVED_STATUS_ID;
        } else if (transferTypeId == REQUEST_TYPE_ID) {
            transferStatusId = PENDING_STATUS_ID;
        } else {
            throw new IllegalArgumentException("Invalid transfer type.");
        }

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId,
                createTransferDTO.getAccountFrom(), createTransferDTO.getAccountTo(),
                createTransferDTO.getAmount());

        if (transferStatusId == APPROVED_STATUS_ID) {
            updateBalances(createTransferDTO.getAccountFrom(), createTransferDTO.getAccountTo(), createTransferDTO.getAmount());
        }

        return getTransferDetails(transferId);
    }

    @Override
    public List<TransferDTO> getTransfersByUserId(int userId) {
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
        while (results.next()) {
            transfers.add(mapRowToTransferDTO(results));
        }
        return transfers;
    }

    public TransferDTO getTransferDetails(int transferId) {
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


    private TransferDTO mapRowToTransferDTO(SqlRowSet rs) {
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

    private void updateBalances(int accountFromId, int accountToId, BigDecimal amount) {
        String deductBalanceSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        String addBalanceSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";

        jdbcTemplate.update(deductBalanceSql, amount, accountFromId);
        jdbcTemplate.update(addBalanceSql, amount, accountToId);
    }

    public TransferDTO getTransferById(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";

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

    @Override
    public List<TransferDTO> getPendingTransfers(int userId) {
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

        while (results.next()) {
            pendingTransfers.add(mapRowToTransferDTO(results));
        }
        return pendingTransfers;
    }

    @Override
    public void updateTransferStatus(int transferId, int statusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, statusId, transferId);
    }
}
