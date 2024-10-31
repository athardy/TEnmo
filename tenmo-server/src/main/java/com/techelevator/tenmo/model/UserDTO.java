package com.techelevator.tenmo.model;



public class UserDTO {

    private String username;
    private int accountId;
    private int userId;

    public UserDTO() {
    }

    public UserDTO(String username, int accountId) {
        this.username = username;
        this.accountId = accountId;
    }

    public UserDTO(String username, int accountId, int userId) {
        this.username = username;
        this.accountId = accountId;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", accountId=" + accountId +
                ", userId=" + userId +
                '}';
    }
}
