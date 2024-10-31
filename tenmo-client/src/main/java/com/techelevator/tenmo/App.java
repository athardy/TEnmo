package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080";
    private static final int SEND_TYPE_ID = 2;
    private static final int REQUEST_TYPE_ID = 1;
    private static final int PENDING_STATUS_ID = 1;

    private ConsoleService consoleService = new ConsoleService(-1);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);


    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();  // Now consoleService is initialized at the start
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser != null) {
            String authToken = currentUser.getToken();
            accountService.setAuthToken(authToken);
            transferService.setAuthToken(authToken);


            int accountId = accountService.getAccountIdByUserId(currentUser.getUser().getId());
            consoleService = new ConsoleService(accountId);
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewTransferDetails();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 6) {
                viewPendingRequests();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        try {
            BigDecimal balance = accountService.getBalance();
            consoleService.printBalance(balance);
        } catch (Exception e) {
            System.out.println("Error retrieving balance. Please try again.");
            e.printStackTrace();
        }
    }

    private void viewTransferHistory() {
        int userId = currentUser.getUser().getId();
        List<TransferDTO> transfers = transferService.getTransfersByUserId(userId);
        consoleService.printTransfers(transfers);
    }

	private void viewPendingRequests() {
		try {
            int userId = currentUser.getUser().getId();
            List<TransferDTO> pendingTransfers = transferService.getPendingTransfersByUserId(userId);
            if (pendingTransfers.isEmpty()) {
                System.out.println("You have no pending transfers");
            } else {
                consoleService.printTransfers(pendingTransfers);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving pending transfers.");
            e.printStackTrace();
        }
	}

    private void sendBucks() {
        try {
            List<UserDTO> users = transferService.getAllUsers();
            consoleService.printUsers(users, currentUser.getUser().getId());

            int recipientId = consoleService.promptForInt("Enter the account ID of the recipient: ");
            int accountFrom = accountService.getAccountIdByUserId(currentUser.getUser().getId());

            if (accountFrom == recipientId) {
                System.out.println("You cannot send TE bucks to your own account.");
                return;
            }

            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
            BigDecimal balance = accountService.getBalance();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return;
            }
            if (amount.compareTo(balance) > 0) {
                System.out.println("Insufficient balance. Please enter an amount within your available balance.");
                return;
            }

            CreateTransferDTO transferRequest = new CreateTransferDTO();
            transferRequest.setAccountFrom(accountFrom);
            transferRequest.setAccountTo(recipientId);
            transferRequest.setAmount(amount);
            transferRequest.setTransferTypeId(SEND_TYPE_ID); 

            TransferDTO transfer = transferService.createTransfer(transferRequest);
            consoleService.printTransferDetails(transfer);

        } catch (Exception e) {
            System.out.println("Error processing transfer. Please try again.");
            e.printStackTrace();
        }
    }


    private void viewTransferDetails() {
        int transferId = consoleService.promptForInt("Enter the Transfer ID: ");
        try {
            TransferDTO transfer = transferService.getTransferDetails(transferId);
            consoleService.printTransferDetails(transfer);
        } catch (Exception e) {
            System.out.println("Transfer not found.");
        }
    }




    private void requestBucks() {
        try {
            List<UserDTO> users = transferService.getAllUsers();
            int currentUserId = currentUser.getUser().getId();

            consoleService.printUsers(users, currentUserId);

            int requestedFromUserId = consoleService.promptForInt("Enter the account ID of the user you want to request TE Bucks from: ");

            if (requestedFromUserId == accountService.getAccountIdByUserId(currentUserId)) {
                System.out.println("You cannot request TE Bucks from your own account.");
                return;
            }

            BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to request: ");

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Request amount must be greater than zero.");
                return;
            }

            CreateTransferDTO transferRequest = new CreateTransferDTO();
            transferRequest.setAccountFrom(requestedFromUserId);
            transferRequest.setAccountTo(accountService.getAccountIdByUserId(currentUserId));
            transferRequest.setAmount(amount);
            transferRequest.setTransferTypeId(REQUEST_TYPE_ID);
            transferRequest.setTransferStatusId(PENDING_STATUS_ID);


            TransferDTO transfer = transferService.createTransfer(transferRequest);
            consoleService.printRequestDetails(transfer);

        } catch (Exception e) {
            System.out.println("Error processing transfer request. Please try again.");
            e.printStackTrace();
        }
    }




}
