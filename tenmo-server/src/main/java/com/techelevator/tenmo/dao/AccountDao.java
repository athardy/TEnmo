package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface defining data access methods for account-related operations.
 */
public interface AccountDao {

    /**
     * Retrieves the balance for a user by their user ID.
     *
     * @param userId ID of the user whose balance is requested.
     * @return BigDecimal balance of the user.
     */
    BigDecimal getBalanceByUserId(int userId);

    /**
     * Retrieves the user ID based on the provided username.
     *
     * @param username Username to search for.
     * @return int user ID associated with the username.
     */
    int getUserIdByUsername(String username);

    /**
     * Finds the account ID associated with a specified user ID.
     *
     * @param userId ID of the user whose account ID is requested.
     * @return Integer account ID or null if not found.
     */
    Integer findAccountIdByUserId(int userId);

    /**
     * Retrieves a list of all users with usernames and account IDs.
     *
     * @return List<UserDTO> containing all users.
     */
    List<UserDTO> findAllUsers();

    /**
     * Updates the balances of two accounts involved in a transfer.
     *
     * @param accountFromId ID of the account sending funds.
     * @param accountToId ID of the account receiving funds.
     * @param amount Amount to transfer between accounts.
     */
    void updateBalances(int accountFromId, int accountToId, BigDecimal amount);

    /**
     * Retrieves the balance for a user by their account ID.
     *
     * @param accountId ID of the account.
     * @return BigDecimal balance of the specified account.
     */
    BigDecimal getBalanceByAccountId(int accountId);

    /**
     * Retrieves the user ID associated with a specific account ID.
     *
     * @param accountId ID of the account.
     * @return int user ID associated with the account.
     */
    int getUserIdByAccountId(int accountId);
}
