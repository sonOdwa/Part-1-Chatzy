package eclpz.chatzy;

import java.util.Scanner;

/**
 * Main application driver for Chat Application PoE (Parts 1, 2, and 3).
 */
public class Main {
    public static void main(String[] args) {
        // ==========================================
        // PART 1: REGISTRATION & LOGIN
        // ==========================================
        try (Scanner scanner = new Scanner(System.in)) {
            // ==========================================
            // PART 1: REGISTRATION & LOGIN
            // ==========================================
            System.out.println("=== CHAT APPLICATION REGISTRATION (PART 1) ===");
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            Login login = new Login(firstName, lastName);
            boolean registered = false;
            while (!registered) {
                System.out.print("Enter Username (must contain '_' and <= 5 chars): ");
                String username = scanner.nextLine();
                
                System.out.print("Enter Password (>= 8 chars, 1 uppercase, 1 digit, 1 special char): ");
                String password = scanner.nextLine();
                
                System.out.print("Enter Cell Phone Number (with international code, <= 10 chars): ");
                String cellNumber = scanner.nextLine();
                
                String regResult = login.registerUser(username, password, cellNumber);
                System.out.println(regResult);
                
                if (regResult.contains("successfully captured")) {
                    registered = true;
                } else {
                    System.out.println("Please try registration again.\n");
                }
            }
            System.out.println("\n=== CHAT APPLICATION LOGIN (PART 1) ===");
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.print("Enter Username: ");
                String loginUser = scanner.nextLine();
                
                System.out.print("Enter Password: ");
                String loginPass = scanner.nextLine();
                
                boolean success = login.loginUser(loginUser, loginPass);
                String loginMessage = login.returnLoginStatus(success);
                System.out.println(loginMessage);
                
                if (success) {
                    loggedIn = true;
                }
            }
            // ==========================================
            // PARTS 2 & 3: QUICKCHAT MESSAGING & REPORTS
            // ==========================================
            System.out.println("\n=== Welcome to QuickChat. ===");
            System.out.print("How many messages would you like to enter? ");
            int maxMessages = 0;
            try {
                maxMessages = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                maxMessages = 1;
            }
            int messageCounter = 0;
            boolean quit = false;
            while (!quit) {
                System.out.println("\nSelect an option from the menu:");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Stored Messages & Reports");
                System.out.println("4) Quit");
                System.out.print("Enter choice (1-4): ");
                
                int choice = 0;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    choice = 0;
                }
                
                switch (choice) {
                    case 1 -> {
                        if (messageCounter >= maxMessages) {
                            System.out.println("\nYou have reached your maximum message entry limit of " + maxMessages + ".");
                            break;
                        }
                        
                        System.out.println("\n--- Entering Message " + (messageCounter + 1) + " of " + maxMessages + " ---");
                        
                        String recipient = "";
                        boolean recipientValid = false;
                        while (!recipientValid) {
                            System.out.print("Enter Recipient Cell Phone Number (e.g., +27718693002): ");
                            recipient = scanner.nextLine();
                            String cellCheck = Message.checkRecipientCell(recipient);
                            System.out.println(cellCheck);
                            if (cellCheck.contains("successfully captured")) {
                                recipientValid = true;
                            }
                        }
                        
                        String messagePayload = "";
                        boolean payloadValid = false;
                        while (!payloadValid) {
                            System.out.print("Enter Message Text (max 250 characters): ");
                            messagePayload = scanner.nextLine();
                            String lengthCheck = Message.checkMessageLength(messagePayload);
                            System.out.println(lengthCheck);
                            if (lengthCheck.equals("Message ready to send.")) {
                                payloadValid = true;
                            }
                        }
                        
                        Message msg = new Message(messageCounter, recipient, messagePayload);
                        
                        System.out.println("\nSelect Message Action:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Enter choice (1-3): ");
                        
                        int actionChoice = 1;
                        try {
                            actionChoice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            actionChoice = 1;
                        }
                        
                        String actionResult = msg.SentMessage(actionChoice);
                        System.out.println("\nStatus: " + actionResult);
                        
                        if (actionChoice == 1 || actionChoice == 3) {
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipientCell());
                            System.out.println("Message: " + msg.getMessageText());
                            
                            if (actionChoice == 3) {
                                msg.storeMessage();
                            }
                        }
                        
                        messageCounter++;
                    }
                        
                    case 2 -> {
                        System.out.println("\n=== RECENTLY SENT MESSAGES ===");
                        System.out.println(Message.printMessages());
                    }
                        
                    case 3 -> {
                        boolean backToMain = false;
                        while (!backToMain) {
                            System.out.println("\n--- STORED MESSAGES & REPORTS SUBMENU ---");
                            System.out.println("a) Display Sender and Recipient of stored messages");
                            System.out.println("b) Display longest message");
                            System.out.println("c) Search for a message by Message ID");
                            System.out.println("d) Search for messages by Recipient");
                            System.out.println("e) Delete a message by Message Hash");
                            System.out.println("f) Display Messages Report");
                            System.out.println("g) Return to Main Menu");
                            System.out.print("Select sub-option (a-g): ");
                            
                            String subChoice = scanner.nextLine().trim().toLowerCase();
                            
                            switch (subChoice) {
                                case "a" -> {
                                    System.out.println("\n[Stored Messages - Sender & Recipient]");
                                    System.out.println(Message.displaySenderAndRecipientOfStored());
                                }
                                case "b" -> {
                                    System.out.println("\n[Longest Message]");
                                    System.out.println(Message.getLongestMessage());
                                }
                                case "c" -> {
                                    System.out.print("Enter Message ID to search: ");
                                    String searchID = scanner.nextLine();
                                    System.out.println("\nSearch Result: " + Message.searchByMessageID(searchID));
                                }
                                case "d" -> {
                                    System.out.print("Enter Recipient Cell Number to search: ");
                                    String searchRecipient = scanner.nextLine();
                                    System.out.println("\nSearch Result: " + Message.searchByRecipient(searchRecipient));
                                }
                                case "e" -> {
                                    System.out.print("Enter Message Hash to delete: ");
                                    String deleteHash = scanner.nextLine();
                                    System.out.println("\n" + Message.deleteByMessageHash(deleteHash));
                                }
                                case "f" -> System.out.println("\n" + Message.displayReport());
                                case "g" -> backToMain = true;
                                default -> System.out.println("Invalid option. Please choose a-g.");
                            }
                        }
                    }
                        
                    case 4 -> {
                        quit = true;
                        System.out.println("\nExiting QuickChat...");
                        System.out.println("Total Messages Sent: " + Message.returnTotalMessagess());
                    }
                        
                    default -> System.out.println("\nInvalid menu option. Please select 1, 2, 3, or 4.");
                }
            }
            }
    }
}