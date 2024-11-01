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

    @Autowired
    private AccountDao accountDao;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance() {
        String username = SecurityUtils.getCurrentUsername();
        int userId = accountDao.getUserIdByUsername(username);
        BigDecimal balance = accountDao.getBalanceByUserId(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Integer> getAccountIdByUserId(@PathVariable int userId) {
        Integer accountId = accountDao.findAccountIdByUserId(userId);
        if (accountId != null) {
            return ResponseEntity.ok(accountId);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return accountDao.findAllUsers()
                .stream()
                .map(user -> new UserDTO(user.getUsername(), user.getAccountId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Integer> getUserIdByAccountId(@PathVariable int accountId) {
        Integer userId = accountDao.getUserIdByAccountId(accountId);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        }
        return ResponseEntity.notFound().build();
    }


}


