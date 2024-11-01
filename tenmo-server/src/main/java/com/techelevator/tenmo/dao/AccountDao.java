package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByUserId(int userId);
    int getUserIdByUsername(String username);
    Integer findAccountIdByUserId(int userId);
    List<UserDTO> findAllUsers();
    void updateBalances(int accountFromId, int accountToId, BigDecimal amount);
    BigDecimal getBalanceByAccountId(int userId);
    int getUserIdByAccountId(int accountId);
}


