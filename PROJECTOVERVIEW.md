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

## Data Transfer Objects (DTOs)

**Data Transfer Objects (DTOs)** play a central role in the TEnmo application by structuring data for communication between the client and server. DTOs are lightweight, serializable objects specifically designed to hold data without additional processing logic. They ensure that only necessary data is sent over the network, improving performance and security.

### Key DTOs in TEnmo

- **`TransferDTO`**: Contains information about each transfer, such as transfer ID, type, status, sender and recipient account IDs, and transfer amount.
- **`CreateTransferDTO`**: Designed for creating new transfer requests, holding the origin and destination account IDs, the transfer amount, type, and status. This DTO simplifies the process of constructing requests on the client side for server-side processing.
- **`UserDTO`**: Holds user account data like `userId` and `username`, allowing client-side interactions with other users without exposing sensitive information.

### How DTOs Enhance Client-Server Communication

- **Data Integrity**: DTOs ensure consistent data structure across requests and responses, so the client and server understand the exact format of data being sent or received.
- **Encapsulation**: By using DTOs, sensitive fields are shielded from exposure, and the server only receives relevant data for each operation.
- **Error Reduction**: With DTOs, data mismatches or unnecessary data fields in the payload are minimized, reducing potential errors during data transmission.

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

## Client-Server Communication

The client and server interact via HTTP requests, exchanging data in a structured JSON format. Each client action (e.g., sending funds, viewing balance) maps to a specific API endpoint on the server, ensuring each request is well-defined and secure.

1. **Request Headers**: For authenticated actions, the client includes an `Authorization` header, embedding the user's token for secure access to protected endpoints. This allows the server to verify the user’s identity without repeated logins.
   
2. **Endpoint Calls**: Client-side methods initiate API calls to endpoints based on user selections. For example:
   - **View Balance**: The client sends a `GET` request to `/account/balance`, with the server returning the user's current balance as a JSON response.
   - **Send TE Bucks**: The client prepares and sends a `POST` request to `/transfer`, including transfer details in the request body. The server then processes and stores the transaction.

3. **Error Handling and Responses**: The server sends structured responses, including status codes and messages. The client interprets these responses to provide feedback, such as success confirmation or error messages when a request fails (e.g., insufficient balance, invalid account).

4. **Session Management**: By storing the user's auth token after login, the client can maintain a session with the server, allowing seamless access to services without repetitive login prompts.

## Unique Aspects and Additional Notes

- **Token-Based Security**: All sensitive operations are protected by a token-based authentication system, ensuring secure access to endpoints.
- **DTO Integration**: By using DTOs, data transfer between client and server is streamlined, with precise fields for each operation and encapsulation to ensure security and consistency.
- **ASCII Art and Custom Menus**: Custom ASCII art and structured menu layouts improve the user experience, adding a unique and engaging visual style.
- **Error Handling**: Comprehensive error handling is integrated, providing specific messages for common issues such as invalid accounts, insufficient funds, or invalid selections.

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

4. **Logout**: Once finished, the user logs out, clearing the token and ending the session.

---

This documentation provides an overview of the TEnmo application’s architecture, main features, and user interaction flow. It serves as a reference for developers and users to understand the application's functionality, API endpoints, and overall design.

