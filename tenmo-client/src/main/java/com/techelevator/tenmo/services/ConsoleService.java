package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.model.UserDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final int currentAccountId;

    public ConsoleService(int currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View transfer details"); // New option
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public int promptForTransferId() {
        return promptForInt("Enter the Transfer ID: ");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("---------------------------------");
        System.out.println("Your current balance is: $" + balance);
        System.out.println("---------------------------------");
    }

    public void printTransfers(List<TransferDTO> transfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To         Amount");
        System.out.println("-------------------------------------------");

        for (TransferDTO transfer : transfers) {
            String fromTo = (transfer.getAccountFrom() == getCurrentAccountId()) ? "To: " + transfer.getAccountTo() : "From: " + transfer.getAccountFrom();
            System.out.printf("%-12d %-15s $%.2f%n", transfer.getTransferId(), fromTo, transfer.getAmount());
        }

        System.out.println("-------------------------------------------");
    }

    public void printTransferDetails(TransferDTO transfer) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From Account: " + transfer.getFromUsername());
        System.out.println("To Account: " + transfer.getToUsername());
        System.out.println("Status: " + transfer.getTransferStatus());
        System.out.printf("Amount: $%.2f%n", transfer.getAmount());
        System.out.println("-------------------------------------------");
    }


    private int getCurrentAccountId() {
        return currentAccountId;
    }

    public void printUsers(List<UserDTO> users, int currentUserId) {
        System.out.println("Available Users:");
        System.out.println("-------------------------------------------");
        for (UserDTO user : users) {
            if (user.getUserId() != currentUserId) {
                System.out.printf("Account ID: %-10d Username: %s%n", user.getAccountId(), user.getUsername());
            }
        }
        System.out.println("-------------------------------------------");
    }

    public void printRequestDetails(TransferDTO transfer) {
        System.out.println("-------------------------------------------");
        System.out.println("Request Details");
        System.out.println("-------------------------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From Account: " + transfer.getFromUsername());
        System.out.println("To Account: " + transfer.getToUsername());
        System.out.println("Status: " + transfer.getTransferStatus());
        System.out.printf("Amount: $%.2f%n", transfer.getAmount());
        System.out.println("-------------------------------------------");
    }


}
