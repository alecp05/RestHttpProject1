package RestHttp;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class EndpointHandler {

    private int messageID;

    public void saveMessagePOST(String message, String path){

        int messageID_temp = 1;
        String pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
        //searching through all files in specified directory
        File dir = new File("src/main/messages");
        String[] allFiles = dir.list();

        if (allFiles == null) {
            System.out.println("does not exist or is not a directory");
        } else {

            //search for free messageID
            for (int i = 0; i < allFiles.length; i++) {
                String filename = allFiles[i];
                filename = "src/main/messages/" + filename;
                if(filename.equals(pathFile)) {
                    messageID_temp++;
                }
                pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {
            writer.write(message);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        setMessageID(messageID_temp);

    }
    public String responsePOST(){
        String response = "POST Request SUCCESS\nYour message is saved in Textfile ID: " + messageID;
        String httpResponse = "HTTP/1.1 200 OK\r\n"
                +"Server: Alec\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Content-Lenght: 0 \r\n\r\n" + response;
        return httpResponse;
    }

    public String printAllGET(String path) throws FileNotFoundException {

        String pathName = "src/main" + path;
        StringBuilder allMsg = new StringBuilder();

        //searching through all files in specified directory
        File dir = new File(pathName);
        String[] children = dir.list();

        if (children == null) {
            System.out.println("does not exist or is not a directory");
        } else {
            int messageID = 1;
            for (int i = 0; i < children.length; i++) {

                String filename = children[i];
                allMsg.append(filename + "\r\n\n");

                //read every textfile
                filename = "src/main/messages/" + filename;
                File myObj = new File(filename);
                Scanner myReader = new Scanner(myObj);

                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    allMsg.append(data+ "\r\n\n");
                }
                myReader.close();

            }
        }
        return allMsg.toString();

    }

    public String responseGET(){
        String httpResponse = "HTTP/1.1 200 OK\r\n"
                +"Server: Alec\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Content-Lenght: 0 \r\n\r\n";
        return httpResponse;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
