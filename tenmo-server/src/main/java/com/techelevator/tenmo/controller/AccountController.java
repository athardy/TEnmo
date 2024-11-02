package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.UserDTO;
import com.techelevator.tenmo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    // Injected AccountDao for handling account data access
    @Autowired
    private AccountDao accountDao;

    /**
     * Retrieves the balance of the currently authenticated user.
     *
     * @return ResponseEntity containing the user's balance as BigDecimal.
     */
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        String username = SecurityUtils.getCurrentUsername(); // Obtain current user's username
        int userId = accountDao.getUserIdByUsername(username); // Retrieve user ID from username
        BigDecimal balance = accountDao.getBalanceByUserId(userId); // Get user balance by user ID
        return ResponseEntity.ok(balance); // Return balance as response
    }

    /**
     * Fetches the account ID associated with a given user ID.
     *
     * @param userId ID of the user whose account ID is requested.
     * @return ResponseEntity containing the account ID, or 404 if not found.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Integer> getAccountIdByUserId(@PathVariable int userId) {
        Integer accountId = accountDao.findAccountIdByUserId(userId); // Find account ID for user
        return accountId != null ? ResponseEntity.ok(accountId) : ResponseEntity.notFound().build(); // Return account ID or 404
    }

    /**
     * Returns a list of all users, including usernames and account IDs.
     *
     * @return List<UserDTO> containing all users' usernames and account IDs.
     */
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        // Retrieve all users and map them to UserDTO objects
        return accountDao.findAllUsers()
                .stream()
                .map(user -> new UserDTO(user.getUsername(), user.getAccountId()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the user ID associated with a specified account ID.
     *
     * @param accountId ID of the account to look up.
     * @return ResponseEntity containing the user ID, or 404 if not found.
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Integer> getUserIdByAccountId(@PathVariable int accountId) {
        Integer userId = accountDao.getUserIdByAccountId(accountId); // Find user ID for account ID
        return userId != null ? ResponseEntity.ok(userId) : ResponseEntity.notFound().build(); // Return user ID or 404
    }

}


