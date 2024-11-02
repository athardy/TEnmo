package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.model.UserDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final int currentAccountId;

    // Initializes ConsoleService with the current user's account ID.
    public ConsoleService(int currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    private final Scanner scanner = new Scanner(System.in);

    // Prompts the user for menu selection and handles invalid input by returning -1.
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

    // Prints a custom banner ASCII art for the console application. A.H. is the stylemaster.
    public void printBanner() {
        System.out.println("      __________");
        System.out.println("     /_  __/ __/__  __ _  ___");
        System.out.println("      / / / _// _ \\/  ' \\/ _ \\");
        System.out.println("     /_/ /___/_//_/_/_/_/\\___/");
        System.out.println("              C A S H  A P P ™   ");
        System.out.println("     Hardy & Laning NLR 2024 ©");
    }

    // Prints a greeting message with a decorative border.
    public void printGreeting() {
        System.out.println("╭───────────────────────────────╮");
        System.out.println("│      Welcome to TEnmo App!    │");
        System.out.println("╰───────────────────────────────╯");
    }

    // Prints the login menu options for user registration, login, or exit.
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

    // Prints the main menu options for various account actions, such as viewing balance or sending TE bucks.
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

    // Prompts the user to enter a transfer ID and returns it as an integer.
    public int promptForTransferId() {
        return promptForInt("Enter the Transfer ID: ");
    }

    // Prompts the user for login credentials, creating and returning a UserCredentials object.
    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        System.out.println("* ↓↓ *Case Sensitive* ↓↓ *");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    // Prompts the user to enter a string and returns it.
    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Prompts the user to enter an integer, re-prompting if the input is invalid.
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

    // Prompts the user to enter a BigDecimal value, re-prompting if the input is invalid.
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

    // Pauses the console and waits for the user to press Enter to continue.
    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Displays a generic error message for the user.
    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    // Prints the user's account balance in a formatted display.
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

    // Prints a list of all transfers with details, including ID, amount, and status.
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

    // Prints the details of a single transfer, including sender, recipient, type, and amount.
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

    // Returns the current user's account ID.
    private int getCurrentAccountId() {
        return currentAccountId;
    }

    // Prints a list of available users for TE bucks transactions.
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

    // Prints the details of a transfer request, including ID, sender, recipient, and amount.
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

    // Prints a large ASCII art image to the console, simulating the design for the application.
    public void print66() {
        {
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓▓▓▓▓▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▒▒▓▒▓▓▓▓▓▓▓████▓▓▓▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▒▒▓▒▒▒▒▒▒▒▒████▓▓▓▓███████▒▒▓▓▓▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▒▒▓▒▒▒▒▒▒▒▓████████▓▓█████████▒▓▒▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓▒▒▒▒▒▒▒▒▒▒███████████▓▓████████▓▓▓▓▒▒▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓▒▒▒▒▒▒▒▒▒▒▒█████████████▓████████▓▓█▒▒▒▒▒▒▓▒▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▒▒▒▒▒");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒███████████████████████▓▓█▒▒▒▒▓▒▒▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▒▓▒▓▒▒");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒▒▓▓██▓███████████████████▓▓▓█▒▒▒▒▓▓▒▓▒▓▓▓▓▓▓▓▓▒▒▒▒▒▓▓▓▓▓▓▓▓▓▒▓▒▒▒");
            System.out.println("▒▓▓▓▒▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓████████████▓▒████████▓▓▓▓█▒▒▒▒▒▓▓▒▓▒▓▓▓▒▒▒▒▒▒▒▒▒▒▓▓▓▓▓▒▓▒▒▒▒▒▓");
            System.out.println("▒▒▒▓▓▓▓▓▒▒▒▓▒▒▓▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓███████████▓░▓▓████████▓█▓▓▓█▒▒▒▒▒▒▓▒▓▒▒▒▒▒▒   ░▒▒▒▒▓▒▒▒▓▒▒▓▓▓▒");
            System.out.println("▒▒▓▒▒▒▒▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓████████████░░▒░▒▓████████▓▓▓▓█▒▒▒▒▒▒▒▒▒▒▒▒░    ▒▒▒▒▒▓▒▒▓▒▒▓▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓██████▓████▒░░░░▒▒▒▒▒████████▓▓▓▓██▓▒▒▒▒▒▒░    ░▒▒▒▒▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓██████▓███▒█▒▒░░▒█▒████████▓██▓▓▓▓▓▓▓▒▒░    ░▒▒▒▒▒▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█████████▓████▒▒█▓▓███▒▒░▓█████▓▓▓▓▓▓▓▓▒▒    ░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██████████████▓▒░░░░░░▒▒▒▒▓████▓▓▓▓▓▓▒▒░    ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒███████████████████▓░▒▒▒▒▒▒▓██████▓▓▓▓▒░    ▒▒▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓███████████████████████▒▒▒▒▒███████▓▓▓░    ░▒▓▓▓▓▓███▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓█▓██████████████████████████████████▓▓▓▒    ░▒▓▓▓▓▓▓▓█▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓███████████████████▓▓▓▓▓████████████▓▓▒    ░▒▓▓▓█▓▓▓▓███▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒██████▒████▓████████████▓▓█▓██████▓██▓▓▒░    ▒▒▓▓▓▓████████▓▓█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒████████████████▓█████████████▓▓▓████▓▓▓▒░    ▒▓▓▓██████▓▓█████▓███▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒████████▓██████████▓███████████▓▓▓▓▓▓▒▒    ░▒▓▓████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒█████████▓███████▓█████████████▓▒▓▒    ░▒▓▓▓██████████████████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒█████████████████████████████▓▓▓▒░    ▒▒▓▓██████████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒█▓███▓█████████████████████▓▓▓▒░    ░▒▒▓█████████████████████████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▓██▓████████████████████▓▓▓▒░    ░▒▒▓▓█████████████████████████████▓█▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒█████▓█████████████▓▓▓▓▒▒    ░▒▓▓▓▓█████████████████████████████████▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒███████████████░▒▒▓▓▒▒▒     ▒▓▓▓▓█▓█████████████▓████▓▓▓██████████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▓███████████▒▒▒▒░░░░    ▒▒▓▓▓███▓██████▓░░██████▓█████▓▓▓▓█████████▓██▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒███████▒▒▒░▒░    ░▒▓▓▓█████▓▓██████▓▓▓▒░░░░████████████▓███████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒████▒░░▒░  ░░▒▒▓▓██████▓▓███████████▒▒▒▒▒▒░████████████▓█▓█████▓█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒▒▒▒▒▓▓██████▓▓▓█▓███████████▒▒▒░▒███████████████▓█████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▒▓████▓█████▓▓▓▓▓█████████████▒▒░▒▒▓███████████████▓▓██▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒████████▓▓▓▓▓█████▓▒░▒████▓███████████████████████▓██▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█████████▓▓▓▓████████▒░░░░░█▒██▒██████████████▓████▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓███████▓▓▓▓█████████▒▓▓▓▓███████████████▓███████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██████████▓▓▓█████████▓███████████████████▓▓██████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓████████████▓███▓███████████████████████████▓█████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█████████████████▓██████████████████████████████████▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▒▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓████████████████████████████████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▒▒▒▒▓▓▓▒▓▓▒▒▒▒▒▒▒▒▒▒▒▒███████████████████████████████████████████████▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
            System.out.println("▒▒▓▓▓▓▓▓▓▓▒▓▓▒▒▓▒▒▒▒▒▒▒▒███████████████████████████████████████████████▓▓▓▓▓▓▒▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▒▒▒▒");
            System.out.println("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓▒▒▒▒█████████████████████████████████████████████████▓▓▓▓▓▓▓▒▓▓▓▓▒▓▒▒▒▒▒▒▒▒▒▒▓▒▒▓");
        }
    }
}
