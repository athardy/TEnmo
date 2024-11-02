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

    public void printBanner() {
        System.out.println("      __________");
        System.out.println("     /_  __/ __/__  __ _  ___");
        System.out.println("      / / / _// _ \\/  ' \\/ _ \\");
        System.out.println("     /_/ /___/_//_/_/_/_/\\___/");
        System.out.println("              C A S H  A P P ™   ");
        System.out.println("     Hardy & Laning NLR 2024 ©");






    }

    public void printGreeting() {
        System.out.println("╭───────────────────────────────╮");
        System.out.println("│      Welcome to TEnmo App!    │");
        System.out.println("╰───────────────────────────────╯");
    }

    public void printLoginMenu() {
        System.out.println("╭───────────────────────────────╮");
        System.out.println("│        Login Menu             │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  1: Register New User         │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  2: Login                     │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  0: Exit Program              │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│        ◁      ○      ◼        │");
        System.out.println("╰───────────────────────────────╯");
        System.out.println();
    }


    public void printMainMenu() {
        System.out.println("╭───────────────────────────────╮");
        System.out.println("│           Main Menu           │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  1: View your current balance │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  2: View past transactions    │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  3: View transaction details  │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  4: Send TE bucks             │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  5: Request TE bucks          │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  6: View pending requests     │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  9: Logout                    │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│  0: Exit Program              │");
        System.out.println("├───────────────────────────────┤");
        System.out.println("│        ◁      ○      ◼        │");
        System.out.println("╰───────────────────────────────╯");
        System.out.println();
    }


    public int promptForTransferId() {
        return promptForInt("Enter the Transfer ID: ");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        System.out.println("* ↓↓ *Case Sensitive* ↓↓ *");
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
        System.out.println("╭───────────────────────────────╮");
        System.out.println("│        Account Balance        │");
        System.out.println("├───────────────────────────────┤");
        System.out.printf("│    Your balance is: $%-8s │%n", balance);
        System.out.println("├───────────────────────────────┤");
        System.out.println("│        ◁      ○      ◼        │");
        System.out.println("╰───────────────────────────────╯");
        System.out.println();
    }



    public void printTransfers(List<TransferDTO> transfers) {
        System.out.println("╭────────────────────────────────────────────────────────────────╮");
        System.out.println("│                            Transfers                           │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-10s %-21s %-12s %-16s │%n", "ID", "From / To", "Amount", "Status");
        System.out.println("├────────────────────────────────────────────────────────────────┤");

        for (TransferDTO transfer : transfers) {
            String fromTo = transfer.getFromUsername() + " ➔ " + transfer.getToUsername();
            System.out.printf("│ %-10d %-21s $%-10.2f %-17s │%n",
                    transfer.getTransferId(),
                    fromTo,
                    transfer.getAmount(),
                    transfer.getTransferStatus());
        }

        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│                         ◁      ○      ◼                        │");
        System.out.println("╰────────────────────────────────────────────────────────────────╯");
    }




    public void printTransferDetails(TransferDTO transfer) {
        System.out.println("╭───────────────────────────────────────────────────────────────╮");
        System.out.println("│                    Transaction Details                        │");
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-15s : %-43d │%n", "ID", transfer.getTransferId());
        System.out.printf("│ %-15s : %-43s │%n", "From Account", transfer.getFromUsername());
        System.out.printf("│ %-15s : %-43s │%n", "To Account", transfer.getToUsername());
        System.out.printf("│ %-15s : %-43s │%n", "Type", transfer.getTransferType());
        System.out.printf("│ %-15s : %-43s │%n", "Status", transfer.getTransferStatus());
        System.out.printf("│ %-15s : $%-42.2f │%n", "Amount", transfer.getAmount());
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("│                          ◁      ○      ◼                      │");
        System.out.println("╰───────────────────────────────────────────────────────────────╯");
    }



    private int getCurrentAccountId() {
        return currentAccountId;
    }

    public void printUsers(List<UserDTO> users, int currentUserId) {
        System.out.println("╭───────────────────────────────────────────────────────────────╮");
        System.out.println("│                       Available Users                         │");
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-15s %-45s │%n", "Account ID", "Username");

        System.out.println("├───────────────────────────────────────────────────────────────┤");
        for (UserDTO user : users) {
            if (user.getUserId() != currentUserId) {
                System.out.printf("│ %-15d %-45s │%n", user.getAccountId(), user.getUsername());
            }
        }
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("│                        ◁      ○      ◼                        │");
        System.out.println("╰───────────────────────────────────────────────────────────────╯");
    }


    public void printRequestDetails(TransferDTO transfer) {
        System.out.println("╭───────────────────────────────────────────────────────────────╮");
        System.out.println("│                      Request Details                          │");
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-15s : %-43d │%n", "ID", transfer.getTransferId());
        System.out.printf("│ %-15s : %-43s │%n", "From Account", transfer.getFromUsername());
        System.out.printf("│ %-15s : %-43s │%n", "To Account", transfer.getToUsername());
        System.out.printf("│ %-15s : %-43s │%n", "Status", transfer.getTransferStatus());
        System.out.printf("│ %-15s : $%-42.2f │%n", "Amount", transfer.getAmount());
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("│                        ◁      ○      ◼                        │");
        System.out.println("╰───────────────────────────────────────────────────────────────╯");
    }



}
