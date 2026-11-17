package eclpz.chatzy;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageTest {

    private Message msg1;
    private Message msg2;
    private Message msg3;
    private Message msg4;
    private Message msg5;

    
    public void setUp() {
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();

        // Populate test data from Table 3 & 4
        msg1 = new Message("0011111111", 0, "+27834557896", "Did you get the cake?");
        msg1.SentMessage(1);

        msg2 = new Message("0022222222", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.SentMessage(3);

        msg3 = new Message("0033333333", 2, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.SentMessage(2);

        msg4 = new Message("0838884567", 3, "0838884567", "It is dinner time !");
        msg4.SentMessage(1);

        msg5 = new Message("0055555555", 4, "+27838884567", "Ok, I am leaving without you.");
        msg5.SentMessage(3);
    }

  
    public void testSentMessagesArrayPopulation() {
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getMessageText());
        assertEquals("It is dinner time !", Message.sentMessages.get(1).getMessageText());
    }

    
    public void testDisplayLongestMessage() {
        String longest = Message.getLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    
    public void testSearchByMessageID() {
        String result = Message.searchByMessageID("0838884567");
        assertEquals("It is dinner time !", result);
    }

    
    public void testSearchByRecipient() {
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

   
    public void testDeleteByMessageHash() {
        String hash = msg2.getMessageHash();
        String deleteResult = Message.deleteByMessageHash(hash);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", deleteResult);
    }

    
    public void testDisplayReport() {
        String report = Message.displayReport();
        assertTrue(report.contains("=== MESSAGES REPORT ==="));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
    }
}
