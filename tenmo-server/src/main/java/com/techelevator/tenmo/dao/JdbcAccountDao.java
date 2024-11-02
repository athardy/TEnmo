package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of AccountDao using JDBC for database interactions.
 */
@Component
public class JdbcAccountDao implements AccountDao {

    // JdbcTemplate for executing SQL queries
    private final JdbcTemplate jdbcTemplate;

    // Constructor to inject JdbcTemplate dependency
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves the balance of a user by their user ID.
     *
     * @param userId ID of the user whose balance is requested. Used to identify the user's account in the database.
     * @return BigDecimal balance of the user's account. Returns zero if the user ID is not found.
     */
    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        // Log to the console that the balance retrieval process has started
        System.out.println("Retrieving balance for user_id: " + userId);

        // SQL query to retrieve the balance for the specified user ID
        String sql = "SELECT balance FROM account WHERE user_id = ?";

        try {
            // Execute the query, expecting a single BigDecimal result for the balance
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

            // Log the retrieved balance for verification/debugging
            System.out.println("Retrieved balance: " + balance);

            // Return the retrieved balance to the caller
            return balance;

        } catch (EmptyResultDataAccessException e) {
            // Log that no balance was found for the specified user ID
            System.out.println("No balance found for user_id: " + userId);

            // Return BigDecimal.ZERO if the user ID is not found in the database
            return BigDecimal.ZERO;

        } catch (Exception e) {
            // Print the stack trace for any unexpected errors that occur during execution
            e.printStackTrace();

            // Throw a RuntimeException to indicate a failure in retrieving the balance, including the original exception
            throw new RuntimeException("Error retrieving balance for user_id: " + userId, e);
        }
    }


    /**
     * Retrieves the balance of an account by its account ID.
     *
     * @param userId ID of the account to look up. Used to identify the specific account in the database.
     * @return BigDecimal balance of the account. Returns zero if the account ID is not found.
     */
    @Override
    public BigDecimal getBalanceByAccountId(int userId) {
        // Log to the console that the balance retrieval process for the given account ID has started
        System.out.println("Retrieving balance for user_id: " + userId);

        // SQL query to select the balance from the account table for the specified account_id
        String sql = "SELECT balance FROM account WHERE account_id = ?";

        try {
            // Execute the query, expecting a single BigDecimal result for the balance associated with the account_id
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

            // Log the retrieved balance for confirmation and debugging purposes
            System.out.println("Retrieved balance: " + balance);

            // Return the retrieved balance to the caller
            return balance;

        } catch (EmptyResultDataAccessException e) {
            // Log a message indicating that no balance was found for the provided account ID
            System.out.println("No balance found for user_id: " + userId);

            // Return BigDecimal.ZERO if the account ID is not found in the database
            return BigDecimal.ZERO;

        } catch (Exception e) {
            // Print the stack trace for any unexpected errors encountered during the query execution
            e.printStackTrace();

            // Throw a RuntimeException to indicate a failure in retrieving the balance, including the original exception
            throw new RuntimeException("Error retrieving balance for user_id: " + userId, e);
        }
    }


    /**
     * Finds the user ID for a specified username.
     *
     * @param username Username to look up. Used to locate the user in the database.
     * @return int user ID associated with the provided username.
     * @throws EmptyResultDataAccessException if no user is found with the given username.
     */
    @Override
    public int getUserIdByUsername(String username) {
        // SQL query to select the user_id from the tenmo_user table where the username matches the provided parameter
        String sql = "SELECT user_id FROM tenmo_user WHERE username = ?";

        // Execute the query and return the user ID as an Integer
        // jdbcTemplate.queryForObject expects the query to return a single result; throws an exception if none or multiple results
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }


    /**
     * Finds the account ID associated with a user ID.
     *
     * @param userId ID of the user whose account ID is requested. Used to locate the account in the database.
     * @return Integer account ID if found, or null if no matching user ID exists.
     * @throws EmptyResultDataAccessException if no account is found for the given user ID.
     */
    @Override
    public Integer findAccountIdByUserId(int userId) {
        // SQL query to select the account_id from the account table where the user_id matches the provided parameter
        String sql = "SELECT account_id FROM account WHERE user_id = ?";

        // Execute the query and return the account ID as an Integer
        // jdbcTemplate.queryForObject expects a single result and will throw an exception if no results are found
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }


    /**
     * Retrieves all users with their usernames, account IDs, and user IDs.
     *
     * @return List<UserDTO> containing user details including username, account ID, and user ID.
     */
    @Override
    public List<UserDTO> findAllUsers() {
        // SQL query to select usernames, account IDs, and user IDs by joining the account and tenmo_user tables
        String sql = "SELECT tenmo_user.username, account.account_id, tenmo_user.user_id " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id";

        // Execute the query and map each row of the result set to a new UserDTO object
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new UserDTO(
                resultSet.getString("username"),       // Extract username from the result set
                resultSet.getInt("account_id"),        // Extract account ID from the result set
                resultSet.getInt("user_id")            // Extract user ID from the result set
        ));
    }


    /**
     * Updates the balances of two accounts involved in a transfer.
     *
     * @param accountFromId ID of the account sending funds. The balance of this account will be decreased.
     * @param accountToId ID of the account receiving funds. The balance of this account will be increased.
     * @param amount Amount to transfer between accounts, represented as a BigDecimal to maintain precision.
     */
    public void updateBalances(int accountFromId, int accountToId, BigDecimal amount) {
        // SQL statement to deduct the specified amount from the balance of the sender's account
        String deductBalanceSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";

        // SQL statement to add the specified amount to the balance of the recipient's account
        String addBalanceSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";

        // Execute the deduction for the sender's account balance
        jdbcTemplate.update(deductBalanceSql, amount, accountFromId);

        // Execute the addition for the recipient's account balance
        jdbcTemplate.update(addBalanceSql, amount, accountToId);
    }


    /**
     * Retrieves the user ID associated with a specific account ID.
     *
     * @param accountId ID of the account to look up. Used to locate the associated user in the database.
     * @return int user ID associated with the provided account ID.
     * @throws EmptyResultDataAccessException if no user is found for the given account ID.
     */
    public int getUserIdByAccountId(int accountId) {
        // SQL query to select the user_id from the account table where the account_id matches the provided parameter
        String sql = "SELECT user_id FROM account WHERE account_id = ?";

        // Execute the query and return the user ID as an Integer
        // jdbcTemplate.queryForObject expects a single result and will throw an exception if no results are found
        return jdbcTemplate.queryForObject(sql, Integer.class, accountId);
    }

}
