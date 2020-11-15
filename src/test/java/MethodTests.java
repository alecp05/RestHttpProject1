import RestHttp.EndpointHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class MethodTests {

    EndpointHandler endpointHandler;

    @Test
    public void testGetIDMethod_shouldMessage() throws FileNotFoundException {
        endpointHandler = new EndpointHandler();

        //test if the message with ID equals the given content below
        System.out.println(endpointHandler.printAllGET("/messages/1"));
        assertTrue(endpointHandler.printAllGET("/messages/1").equals("MessageID: 1 \r\nHallo, wie geht es dir?\r\n\n"),
                "The message with the ID 1 should be -MessageID 1: Hallo, wie geht es dir?");
    }

    @Test
    public void testGetAllMsg_shouldprintAllMsg() throws FileNotFoundException {
        endpointHandler = new EndpointHandler();

        //test if all messages of /messages and all messages of /messages/ are the same
        String allMSG = endpointHandler.printAllGET("/messages/");
        assertTrue(endpointHandler.printAllGET("/messages").equals(allMSG));
    }

    @Test
    public void testDelete_shouldDeleteMessage() throws FileNotFoundException {
        endpointHandler = new EndpointHandler();

        endpointHandler.deleteDEL("messages/8");

        //test if the removed file exists
        File myObj = new File("src/main/messages/8.txt");
        boolean exists = myObj.exists();
        assertFalse(exists,"Message with ID 8 should be removed");
    }

    @Test
    public void testPut_shouldReplaceContent() throws FileNotFoundException {
        endpointHandler = new EndpointHandler();
        endpointHandler.contentPUT("/messages/2","Was hast du heute gemacht und was hast du noch vor?");

        //test if the content of message ID 2 has been replaced correctly
        String message = endpointHandler.printAllGET("/messages/2");
        assertTrue(message.equals("MessageID: 2 \r\nWas hast du heute gemacht und was hast du noch vor?\r\n\n"));
    }

}
