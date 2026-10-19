package eclpz.chatzy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles message creation, validation rules, hash generation,
 * JSON storage, and status tracking for Part 2.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String sendStatus;

    private static int totalSentMessages = 0;
    private static List<Message> sentMessagesList = new ArrayList<>();

    public Message() {}

    public Message(int messageNumber, String recipientCell, String messageText) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public Message(String messageID, int messageNumber, String recipientCell, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random rand = new Random();
        long number = (long) (rand.nextDouble() * 9000000000L) + 1000000000L;
        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        return checkRecipientCell(this.recipientCell);
    }

    public static String checkRecipientCell(String cellNumber) {
        if (cellNumber != null && cellNumber.startsWith("+") && cellNumber.length() <= 10) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public static String checkMessageLength(String message) {
        if (message == null || message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extra = message.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    public String createMessageHash() {
        return createMessageHash(this.messageID, this.messageNumber, this.messageText);
    }

    public static String createMessageHash(String id, int number, String text) {
        String idPrefix = "00";
        if (id != null && id.length() >= 2) {
            idPrefix = id.substring(0, 2);
        }

        String firstWord = "";
        String lastWord = "";

        if (text != null && !text.trim().isEmpty()) {
            String[] words = text.trim().split("\\s+");
            firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
            lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        }

        return idPrefix + ":" + number + ":" + firstWord + lastWord;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.sendStatus = "Sent";
                totalSentMessages++;
                sentMessagesList.add(this);
                return "Message successfully sent.";
            case 2:
                this.sendStatus = "Disregarded";
                return "Press 0 to delete the message.";
            case 3:
                this.sendStatus = "Stored";
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public static String printMessages() {
        if (sentMessagesList.isEmpty()) {
            return "No sent messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessagesList) {
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipientCell()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("----------------------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return totalSentMessages;
    }

    public String storeMessage() {
        String json = "{\n" +
                "  \"messageID\": \"" + messageID + "\",\n" +
                "  \"messageNumber\": " + messageNumber + ",\n" +
                "  \"recipient\": \"" + recipientCell + "\",\n" +
                "  \"message\": \"" + (messageText != null ? messageText.replace("\"", "\\\"") : "") + "\",\n" +
                "  \"messageHash\": \"" + messageHash + "\",\n" +
                "  \"status\": \"" + sendStatus + "\"\n" +
                "}";

        try (FileWriter writer = new FileWriter("messages.json", true)) {
            writer.write(json + "\n");
        } catch (IOException e) {
            System.err.println("Error saving JSON: " + e.getMessage());
        }
        return json;
    }

    // Getters and Setters
    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public int getMessageNumber() { return messageNumber; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }
    public String getRecipientCell() { return recipientCell; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }
    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public String getMessageHash() { return messageHash; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }
    public String getSendStatus() { return sendStatus; }
    public void setSendStatus(String sendStatus) { this.sendStatus = sendStatus; }
}