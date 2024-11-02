# Overview of the TEnmo Application

## Application Description

The **TEnmo Application** is a virtual transaction service that enables users to send and receive "TE Bucks" securely. Built with a client-server architecture, the application supports key functionalities for balance inquiries, sending funds, and managing requests, all while ensuring security with token-based authentication.

## Architecture and Workflow

### Client-Side
- **Platform**: Java Console Application
- **Functions**:
  - **User Interface Management**: Handles all user interactions through a text-based menu system.
  - **API Requests**: Communicates with server-side endpoints using RESTful HTTP requests for actions like viewing balances, initiating transfers, and managing pending requests.
  - **Data Display**: Presents data responses such as balance details and transaction history in a formatted and user-friendly way, including custom ASCII art for enhanced engagement.

### Server-Side
- **Platform**: Spring Boot RESTful API
- **Core Responsibilities**:
  - **Authentication**: Manages user login and registration, issuing tokens upon successful login for secure API access.
  - **Data Processing**: Handles requests for account details, processes transactions, and manages transfer statuses.
  - **Security**: Utilizes token-based authorization to secure endpoints and ensure only authenticated users can access sensitive data.

## Key Features and Functionalities

- **Balance Check**: Allows users to retrieve and view their current TE Bucks balance.
- **Send TE Bucks**: Users can send funds to other users by entering the recipient's account ID and specifying an amount.
- **Request TE Bucks**: Users can request funds from others, which remain in a "Pending" status until approved or rejected.
- **Transfer History**: Displays the user's transfer history with details on each transaction, including status, type, and participants.
- **Pending Transfer Management**: Users can view, approve, or reject pending requests, with built-in validation to prevent self-approvals.

## API Endpoints

| Endpoint                         | Method | Purpose                                            |
|----------------------------------|--------|----------------------------------------------------|
| `/login`                         | POST   | Authenticates a user and returns an auth token     |
| `/account/balance`               | GET    | Retrieves the user's current account balance       |
| `/account/user/{userId}`         | GET    | Retrieves the account ID associated with the user  |
| `/account/account/{accountId}`   | GET    | Retrieves the user ID associated with the account  |
| `/transfer`                      | POST   | Creates a new transfer request                     |
| `/transfer/user/{userId}`        | GET    | Retrieves all transfers associated with a user     |
| `/transfer/pending/{userId}`     | GET    | Retrieves pending transfers for a user             |
| `/transfer/{transferId}/approve` | PUT    | Approves a pending transfer                        |
| `/transfer/{transferId}/reject`  | PUT    | Rejects a pending transfer                         |

## Application Workflow

1. **User Login**:
   - The user logs in or registers through the command-line interface.
   - On successful login, the server issues an authentication token, stored client-side for secure requests.
   
2. **Accessing the Main Menu**:
   - Users can view their balance, manage transfers, view transaction history, or review pending requests.
   - Each action sends an API request to the server, receiving data back to display or further process based on the user's choice.

3. **Executing Transfers**:
   - **Send TE Bucks**: Users initiate a transfer by selecting a recipient and specifying an amount, which updates both accounts if the transfer is successful.
   - **Request TE Bucks**: Users can send a request for TE Bucks from another user, which the recipient can then approve or reject.
   - **Pending Requests**: Users can view and manage incoming requests through a custom interface that checks account balances and validates approvals.

## Unique Aspects and Additional Notes

- **Token-Based Security**: All sensitive operations are protected by a token-based authentication system, ensuring secure access to endpoints.
- **ASCII Art and Custom Menus**: Custom ASCII art and structured menu layouts improve the user experience, adding a unique and engaging visual style.
- **Error Handling**: Comprehensive error handling is integrated, providing specific messages for common issues such as invalid accounts, insufficient funds, or invalid selections.

## Example User Interaction Flow

1. **Login and Setup**: The user logs in, receives an auth token, and accesses the main menu.
2. **Balance Inquiry**: The user selects the option to view their balance, which is retrieved from the server.
3. **Transaction Execution**:
   - **Send TE Bucks**: The user selects a recipient, enters an amount, and completes the transaction.
   - **Pending Transfers**: The user reviews any pending requests and either approves or rejects them as appropriate.
4. **Logout**: Once finished, the user logs out, clearing the token and ending the session.

---

This documentation provides an overview of the TEnmo application’s architecture, main features, and user interaction flow. It serves as a reference for developers and users to understand the application's functionality, API endpoints, and overall design.

