package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for handling user-related operations using JDBC.
 */
@Component
public class JdbcUserDao implements UserDao {

    // Default starting balance for new user accounts
    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private final JdbcTemplate jdbcTemplate;

    // Constructor to inject JdbcTemplate dependency
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId ID of the user to retrieve.
     * @return User object if found; null if no user with the specified ID exists.
     * @throws DaoException if unable to connect to the database.
     */
    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE user_id = ?";
        try {
            // Execute query and retrieve result set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                // Map result to User object if a match is found
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            // Handle database connection errors
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return List of User objects representing all users.
     * @throws DaoException if unable to connect to the database.
     */
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user";
        try {
            // Execute query to fetch all users
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                // Map each result row to a User object and add it to the list
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            // Handle database connection errors
            throw new DaoException("Unable to connect to server or database", e);
        }
        return users;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username Username to look up.
     * @return User object if found; null if no user with the specified username exists.
     * @throws DaoException if unable to connect to the database.
     * @throws IllegalArgumentException if the username is null.
     */
    @Override
    public User getUserByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        User user = null;
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username = LOWER(TRIM(?));";
        try {
            // Execute query with trimmed, lowercase username
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
            if (rowSet.next()) {
                // Map result to User object if a match is found
                user = mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            // Handle database connection errors
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    /**
     * Creates a new user in the system and assigns a default starting balance.
     *
     * @param user RegisterUserDto containing new user details.
     * @return User object for the newly created user.
     * @throws DaoException if unable to connect to the database or if there is a data integrity violation.
     */
    @Override
    public User createUser(RegisterUserDto user) {
        User newUser = null;
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (LOWER(TRIM(?)), ?) RETURNING user_id";

        // Encrypt the provided password for secure storage
        String password_hash = new BCryptPasswordEncoder().encode(user.getPassword());
        try {
            // Insert user and retrieve the generated user ID
            int newUserId = jdbcTemplate.queryForObject(sql, int.class, user.getUsername(), password_hash);
            newUser = getUserById(newUserId);

            if (newUser != null) {
                // Create an associated account with a starting balance if user creation was successful
                sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";
                jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
            }
        } catch (CannotGetJdbcConnectionException e) {
            // Handle database connection errors
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            // Handle data integrity violations, such as duplicate usernames
            throw new DaoException("Data integrity violation", e);
        }
        return newUser;
    }

    /**
     * Maps a row from a SqlRowSet to a User object.
     *
     * @param rs SqlRowSet containing the row data.
     * @return User object with populated fields from the row data.
     */
    private User mapRowToUser(SqlRowSet rs) {
        // Initialize User object and populate it with data from the result set
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));

        // Set default values for activation status and authority role
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
