package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        System.out.println("Retrieving balance for user_id: " + userId);

        String sql = "SELECT balance FROM account WHERE user_id = ?";
        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
            System.out.println("Retrieved balance: " + balance);
            return balance;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No balance found for user_id: " + userId);
            return BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving balance for user_id: " + userId, e);
        }
    }


    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public Integer findAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }


    // think about appending sql syntax to not find yourself
    @Override
    public List<UserDTO> findAllUsers() {
        String sql = "SELECT tenmo_user.username, account.account_id, tenmo_user.user_id " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new UserDTO(
                resultSet.getString("username"),
                resultSet.getInt("account_id"),
                resultSet.getInt("user_id")
        ));
    }







}
