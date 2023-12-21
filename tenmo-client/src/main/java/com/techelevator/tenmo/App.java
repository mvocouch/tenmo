package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
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
        if (currentUser == null) {
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
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance();
        if (balance != null) {
            System.out.println("Your current balance is: " + balance);
        } else consoleService.printErrorMessage();
	}

	private void viewTransferHistory() {
        Transfer[] transfers = accountService.retrieveAllTransfers();
        if (transfers != null) {

        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		Transfer[] transfers = accountService.retrieveAllTransfers();
        if (transfers != null) {
            consoleService.printPendingTransferMenu(transfers, currentUser.getUser(), userService);
            int selectedTransferId = consoleService.promptForInt("Please select Id of transfer you wish to accept/reject (0 to cancel): ");
            if (selectedTransferId != 0) {
                consoleService.printAcceptRejectMenu();
                int menuSelection = consoleService.promptForMenuSelection("Please select an option: ");
                if (menuSelection == 1) {
                    //make a method for approval
                } else if (menuSelection == 2) {
                    //make a method for rejection
                }
            }
        }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        if (users != null) {
            consoleService.printUserMenu(users);
            int toUserId = consoleService.promptForInt("Enter the Id of the User you wish to send bucks to (0 to cancel)");
            if (toUserId != 0) {
                BigDecimal amount = consoleService.promptForBigDecimal("Please enter amount sent: ");
                int fromUserId = currentUser.getUser().getId();
                TransferDto dto = new TransferDto(fromUserId, toUserId, amount, TransferType.SEND);
                Transfer transfer = transferService.createTransfer(dto);
                if (transfer != null) {
                    System.out.println(amount+" TENMO BUCKS were sent to user: " + toUserId);
                } else {
                    consoleService.printErrorMessage();
                }
            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        if (users != null) {
            consoleService.printUserMenu(users);
            int fromUserId = consoleService.promptForInt("Enter the Id of the User you wish to request bucks from (0 to cancel)");
            if (fromUserId != 0) {
                BigDecimal amount = consoleService.promptForBigDecimal("Please enter amount sent: ");
                int toUserId = currentUser.getUser().getId();
                TransferDto dto = new TransferDto(fromUserId, toUserId, amount, TransferType.REQUEST);
                Transfer transfer = transferService.createTransfer(dto);
                if (transfer != null) {
                    System.out.println(amount+" TENMO BUCKS were requested from user: " + fromUserId);
                } else {
                    consoleService.printErrorMessage();
                }
            }
        }
	}

}
