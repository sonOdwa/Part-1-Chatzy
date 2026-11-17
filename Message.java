package eclpz.chatzy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String sendStatus;

    // Global arrays required by Part 3
    public static List<Message> sentMessages = new ArrayList<>();
    public static List<Message> disregardedMessages = new ArrayList<>();
    public static List<Message> storedMessages = new ArrayList<>();
    public static List<String> messageHashes = new ArrayList<>();
    public static List<String> messageIDs = new ArrayList<>();

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

    public String createMessageHash() {
        return createMessageHash(this.messageID, this.messageNumber, this.messageText);
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.sendStatus = "Sent";
                addMessageToArrays(this, "Sent");
                return "Message successfully sent.";
            case 2:
                this.sendStatus = "Disregard";
                addMessageToArrays(this, "Disregard");
                return "Press 0 to delete the message.";
            case 3:
                this.sendStatus = "Stored";
                addMessageToArrays(this, "Stored");
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public static void addMessageToArrays(Message msg, String status) {
        if (msg == null) return;
        msg.setSendStatus(status);
        if ("Sent".equalsIgnoreCase(status)) {
            sentMessages.add(msg);
        } else if ("Disregard".equalsIgnoreCase(status) || "Disregarded".equalsIgnoreCase(status)) {
            disregardedMessages.add(msg);
        } else if ("Stored".equalsIgnoreCase(status)) {
            storedMessages.add(msg);
        }
        if (msg.getMessageHash() != null) messageHashes.add(msg.getMessageHash());
        if (msg.getMessageID() != null) messageIDs.add(msg.getMessageID());
    }

    public static String displaySenderAndRecipientOfStored() {
        if (storedMessages.isEmpty()) return "No stored messages available.";
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Recipient: ").append(m.getRecipientCell()).append("\n");
        }
        return sb.toString().trim();
    }

    public static String getLongestMessage() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        if (all.isEmpty()) return "No messages available.";

        Message longest = all.get(0);
        for (Message m : all) {
            if (m.getMessageText() != null && m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    public static String searchByMessageID(String searchID) {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        for (Message m : all) {
            if (m.getMessageID() != null && m.getMessageID().equalsIgnoreCase(searchID)) {
                return m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipient) {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        List<String> results = new ArrayList<>();
        for (Message m : all) {
            if (m.getRecipientCell() != null && m.getRecipientCell().equalsIgnoreCase(recipient)) {
                results.add(m.getMessageText());
            }
        }

        if (results.isEmpty()) return "No messages found for recipient: " + recipient;
        return String.join(", ", results);
    }

    public static String deleteByMessageHash(String hash) {
        Message toRemove = null;
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        for (Message m : all) {
            if (m.getMessageHash() != null && m.getMessageHash().equalsIgnoreCase(hash)) {
                toRemove = m;
                break;
            }
        }

        if (toRemove != null) {
            sentMessages.remove(toRemove);
            storedMessages.remove(toRemove);
            disregardedMessages.remove(toRemove);
            messageHashes.remove(toRemove.getMessageHash());
            messageIDs.remove(toRemove.getMessageID());
            return "Message: \"" + toRemove.getMessageText() + "\" successfully deleted.";
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) return "No messages in report.";
        StringBuilder sb = new StringBuilder();
        sb.append("=== MESSAGES REPORT ===\n");
        List<Message> reportList = new ArrayList<>();
        reportList.addAll(sentMessages);
        reportList.addAll(storedMessages);

        for (Message m : reportList) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipientCell()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("Status: ").append(m.getSendStatus()).append("\n")
              .append("----------------------------------------\n");
        }
        return sb.toString().trim();
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) return "No sent messages to display.";
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipientCell()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("----------------------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() { return sentMessages.size(); }

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