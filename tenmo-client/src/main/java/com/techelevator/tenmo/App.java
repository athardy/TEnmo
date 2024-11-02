package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    // Base URL for API communication
    private static final String API_BASE_URL = "http://localhost:8080";
    // Transfer types and status IDs
    private static final int SEND_TYPE_ID = 2;
    private static final int REQUEST_TYPE_ID = 1;
    private static final int PENDING_STATUS_ID = 1;

    // Services for console interaction, authentication, account, and transfer operations
    private ConsoleService consoleService = new ConsoleService(-1);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    // Tracks the currently authenticated user
    private AuthenticatedUser currentUser;

    // Entry point of the application
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    // Starts the application by printing initial messages and displaying login menu
    private void run() {
        consoleService.printBanner();
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    // Handles user login and registration selection
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option (#): ");
            if (menuSelection == 1) {
                handleRegister();  // Registers a new user
            } else if (menuSelection == 2) {
                handleLogin();  // Logs in an existing user
            } else if (menuSelection == 0) {
                System.out.println("Closing app...");
            } else if (menuSelection == 66) {
                consoleService.print66();  // Displays ASCII art easter egg
            } else {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    // Registers a new user based on console input and displays a message on success or failure
    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    // Logs in the user by obtaining credentials and authenticating with the server
    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        if (currentUser != null) {
            // Set auth tokens for future requests
            String authToken = currentUser.getToken();
            accountService.setAuthToken(authToken);
            transferService.setAuthToken(authToken);

            // Get and set the account ID for the current user
            int accountId = accountService.getAccountIdByUserId(currentUser.getUser().getId());
            consoleService = new ConsoleService(accountId);
        } else {
            consoleService.printErrorMessage();
        }
    }

    // Displays the main menu after login and handles user actions
    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option (#): ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewTransferHistory();
                viewTransferDetails();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 6) {
                viewPendingRequests();
            } else if (menuSelection == 9) {
                handleLogout();
                loginMenu();  // Return to login menu after logout
            } else if (menuSelection == 0) {
                System.out.println("Closing App...");
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    // Logs out the user by resetting tokens and user details
    private void handleLogout() {
        // Sets the current user to null, effectively logging the user out
        currentUser = null;

        // Clears the authentication token in the account service to prevent further access
        accountService.setAuthToken(null);

        // Clears the authentication token in the transfer service for the same reason
        transferService.setAuthToken(null);

        // Displays a confirmation message indicating successful logout
        System.out.println("You have been logged out.");
    }


    // Retrieves and displays the current balance for the authenticated user
    private void viewCurrentBalance() {
        try {
            // Calls the account service to get the balance of the current user
            BigDecimal balance = accountService.getBalance();

            // Prints the retrieved balance to the console in a formatted display
            consoleService.printBalance(balance);
        } catch (Exception e) {
            // Informs the user of any errors encountered during balance retrieval
            System.out.println("Error retrieving balance. Please try again.");

            // Prints the exception's stack trace to help diagnose issues during debugging
            e.printStackTrace();
        }
    }


    // Displays the user's transfer history by fetching and printing all transfers
    private void viewTransferHistory() {
        // Retrieve the current user's ID to fetch associated transfers
        int userId = currentUser.getUser().getId();

        // Fetch all transfers associated with the current user
        List<TransferDTO> transfers = transferService.getTransfersByUserId(userId);

        // Print the list of transfers using the console service for display formatting
        consoleService.printTransfers(transfers);
    }


    // Displays pending transfer requests and allows the user to approve or reject them
    private void viewPendingRequests() {
        try {
            // Retrieve the current user's ID and fetch their pending transfers
            int currentUserId = currentUser.getUser().getId();
            List<TransferDTO> pendingTransfers = transferService.getPendingTransfersByUserId(currentUserId);

            // Check if there are any pending transfers to display
            if (pendingTransfers.isEmpty()) {
                System.out.println("You have no pending transfers.");
                return;  // Exit if no pending transfers exist
            }

            // Display the list of pending transfers for the user to review
            consoleService.printTransfers(pendingTransfers);

            // Prompt the user to select a transfer ID to approve or reject
            int transferId = consoleService.promptForInt("Enter the ID of the transfer to approve/reject (or 0 to cancel): ");
            if (transferId == 0) {
                System.out.println("Cancelled.");  // Exit if user chooses to cancel
                return;
            }

            // Search for the selected transfer ID in the pending transfers list
            TransferDTO selectedTransfer = pendingTransfers.stream()
                    .filter(t -> t.getTransferId() == transferId)
                    .findFirst()
                    .orElse(null);

            // Validate that the selected transfer ID exists
            if (selectedTransfer == null) {
                System.out.println("Invalid transfer ID.");
                return;
            }

            // Check if the current balance is sufficient to approve the transfer
            BigDecimal balance = accountService.getBalance();
            if (selectedTransfer.getAmount().compareTo(balance) > 0) {
                System.out.println("Insufficient balance to approve this transfer.");
                return;  // Exit if balance is insufficient
            }

            // Prompt the user to approve ('A') or reject ('R') the transfer
            String action = consoleService.promptForString("Type 'A' to approve or 'R' to reject: ");
            if (action.equalsIgnoreCase("A")) {
                // Approve the transfer and notify the user
                transferService.approveTransfer(transferId);
                System.out.println("Transfer approved.");
            } else if (action.equalsIgnoreCase("R")) {
                // Reject the transfer and notify the user
                transferService.rejectTransfer(transferId);
                System.out.println("Transfer rejected.");
            } else {
                // Handle invalid input for the action selection
                System.out.println("Invalid choice. Cancelling action.");
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            System.out.println("Error retrieving pending transfers.");
            e.printStackTrace();
        }
    }


    // Allows the user to send TE bucks to another user
    private void sendBucks() {
        try {
            // Retrieve the current user's ID and a list of all users (excluding current user)
            int currentUserId = currentUser.getUser().getId();
            List<UserDTO> users = transferService.getAllUsers(currentUserId);

            // Display the list of users for selection
            consoleService.printUsers(users, currentUserId);

            // Prompt the user to enter the recipient's account ID and validate the selection
            int recipientId = consoleService.promptForInt("Enter the account ID of the recipient: ");
            int accountFrom = accountService.getAccountIdByUserId(currentUserId);

            // Verify that the recipient account exists in the list of users
            boolean validRecipient = users.stream().anyMatch(user -> user.getAccountId() == recipientId);
            if (!validRecipient) {
                System.out.println("That account doesn't exist. Please try again.");
                return;  // Exit if the recipient ID is invalid
            }

            // Prevent the user from sending TE bucks to their own account
            if (accountFrom == recipientId) {
                System.out.println("You cannot send TE bucks to your own account.");
                return;
            }

            // Prompt for the amount to transfer and check for sufficient balance
            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
            BigDecimal balance = accountService.getBalance();

            // Ensure the transfer amount is greater than zero
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return;
            }

            // Check if the user has sufficient balance for the transfer
            if (amount.compareTo(balance) > 0) {
                System.out.println("Insufficient balance. Please enter an amount within your available balance.");
                return;
            }

            // Create a new transfer request object with the specified details
            CreateTransferDTO transferRequest = new CreateTransferDTO();
            transferRequest.setAccountFrom(accountFrom);
            transferRequest.setAccountTo(recipientId);
            transferRequest.setAmount(amount);
            transferRequest.setTransferTypeId(SEND_TYPE_ID);

            // Submit the transfer request through the transfer service
            TransferDTO transfer = transferService.createTransfer(transferRequest);

            // Display the transfer details for confirmation
            consoleService.printTransferDetails(transfer);

        } catch (Exception e) {
            // Handle any errors that occur during the transfer process
            System.out.println("Error processing transfer. Please try again.");
            e.printStackTrace();
        }
    }


    // Allows the user to view detailed information about a specific transfer
    private void viewTransferDetails() {
        // Prompt the user to enter the ID of the transfer they want details for
        int transferId = consoleService.promptForInt("Enter a Transfer ID from the List: ");

        try {
            // Use the transfer service to retrieve details for the specified transfer ID
            TransferDTO transfer = transferService.getTransferDetails(transferId);

            // Display the detailed information of the retrieved transfer using the console service
            consoleService.printTransferDetails(transfer);
        } catch (Exception e) {
            // Handle cases where the transfer ID is invalid or details could not be retrieved
            System.out.println("Transfer not found.");
        }
    }


    // Allows the user to request TE bucks from another user
    private void requestBucks() {
        try {
            // Retrieve the ID of the currently authenticated user
            int currentUserId = currentUser.getUser().getId();

            // Fetch a list of all other users (excluding the current user) for potential requests
            List<UserDTO> users = transferService.getAllUsers(currentUserId);
            consoleService.printUsers(users, currentUserId); // Display available users in the console

            // Prompt the user to enter the account ID of the user they wish to request TE bucks from
            int requestedFromUserId = consoleService.promptForInt("Enter the account ID of the user you want to request TE Bucks from: ");

            // Check if the entered account ID is valid by comparing it to the list of available users
            boolean validRecipient = users.stream().anyMatch(user -> user.getAccountId() == requestedFromUserId);

            // If the account ID is invalid, display an error message and exit the method
            if (!validRecipient) {
                System.out.println("That account doesn't exist. Please try again.");
                return;
            }

            // Prevent the user from requesting TE bucks from their own account
            if (requestedFromUserId == accountService.getAccountIdByUserId(currentUserId)) {
                System.out.println("You cannot request TE Bucks from your own account.");
                return;
            }

            // Prompt the user to enter the amount they wish to request, validating it's greater than zero
            BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to request: ");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Request amount must be greater than zero.");
                return;
            }

            // Create a transfer request with the necessary details: sender, receiver, amount, type, and status
            CreateTransferDTO transferRequest = new CreateTransferDTO();
            transferRequest.setAccountFrom(requestedFromUserId); // Set the account from which funds are requested
            transferRequest.setAccountTo(accountService.getAccountIdByUserId(currentUserId)); // Set current user's account as the receiver
            transferRequest.setAmount(amount); // Set the requested amount
            transferRequest.setTransferTypeId(REQUEST_TYPE_ID); // Use the constant for "Request" transfer type
            transferRequest.setTransferStatusId(PENDING_STATUS_ID); // Set initial status to "Pending"

            // Send the transfer request to the transfer service and receive a TransferDTO object in return
            TransferDTO transfer = transferService.createTransfer(transferRequest);

            // Display the details of the created transfer request to the user
            consoleService.printRequestDetails(transfer);
        } catch (Exception e) {
            // Handle any errors during the request process and print a user-friendly error message
            System.out.println("Error processing transfer request. Please try again.");
            e.printStackTrace();
        }
    }

}

