package RestHttp;

import java.io.*;
import java.util.*;

public class EndpointHandler {

    private int messageID;

    public void saveMessagePOST(String message, String path){

        int messageID_temp = 1;
        String pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
        //creating a list for the ID in Directory
        List<Integer> list = new ArrayList<Integer>();
        int numbers;

        //searching through all files in specified directory
        File dir = new File("src/main/messages");
        String[] allFiles = dir.list();

        if (allFiles == null) {
            System.out.println("does not exist or is not a directory");
        } else {

            //add all ID in the List
            for (int i = 0; i < allFiles.length; i++) {
                String filename = allFiles[i];

                String[] splits = filename.split("\\.");
                numbers = Integer.parseInt(splits[0]);
                list.add(numbers);

            }
                Collections.sort(list);
            //search in the sorted List for a free ID
            for(int j = 0; j<list.size();j++)
            {
                if(list.get(j) == (j+1))
                    messageID_temp++;
            }
            pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
            //System.out.println(list);
            System.out.println("\nThis is the given MessageID: " + messageID_temp + "\n");

        }
        //write the message in the File
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
        String httpResponse = "HTTP/1.1 201 Created\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 201 \r\n"
                + "Content-Lenght: 61 \r\n\r\n" + response;
        return httpResponse;
    }
    public String responseErrorPOST(){

        String httpResponse = "HTTP/1.1 404 not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 404 \r\n"
                + "Content-Lenght: 16 \r\n\r\n" + "Wrong Directory!";
        return httpResponse;
    }

    public String printAllGET(String path) throws FileNotFoundException {

        StringBuilder allMsg = new StringBuilder();

        //if the ID is given in the Path
        //then just save the correct Message into allMsg
        if(path.length()>10){
            String[] splitting = path.split("/");
            String messageID;
            if(splitting.length>2){
                messageID = splitting[2];
                //appending the message ID
                //allMsg.append("MessageID: "+ messageID + " \r\n");
                String pathName = "src/main" + path + ".txt" ;


                File myObj = new File(pathName);
                boolean exists = myObj.exists();
                if(exists) {
                    allMsg.append("MessageID: "+ messageID + " \r\n");
                    Scanner myReader = new Scanner(myObj);


                    //appending the content in the textfile
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        allMsg.append(data + "\r\n\n");
                    }
                    myReader.close();
                    System.out.println("The message with the ID: " + messageID + " is sent to the Client\n");
                }else {
                    System.out.println("File doesn't exist!\n");
                    allMsg.append("NOTFOUND");
                }
            }


        }else{
            String pathName = "src/main" + path;
            //if Client wants to read all Messages
            //searching through all files in specified directory
            File dir = new File(pathName);
            String[] children = dir.list();

            if (children == null) {
                System.out.println("does not exist or is not a directory");
            } else {

                for (int i = 0; i < children.length; i++) {

                    //append all filenames
                    String filename = children[i];
                    allMsg.append(filename + "\r\n\n");

                    //read every textfile and append it
                    filename = "src/main/messages/" + filename;
                    File myObj = new File(filename);
                    Scanner myReader = new Scanner(myObj);

                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        allMsg.append(data+ "\r\n\n");
                    }
                    myReader.close();

                }
                System.out.println("All messages is sent to the Client\n");
            }
        }
        return allMsg.toString();

    }

    public String responseGET(){
        String httpResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 200 \r\n"
                + "Content-Lenght: 0 \r\n\r\n";
        return httpResponse;
    }
    public String responseErrorGET(){
        String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 404 \r\n"
                + "Content-Lenght: 36 \r\n\r\n" + "Wrong Directory! No Content found...";
        return httpResponse;
    }
    public String responseErrorGET2(){
        String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 404 \r\n"
                + "Content-Lenght: 35 \r\n\r\n" + "File not found or doesn't exists...";
        return httpResponse;
    }

    public boolean deleteDEL(String path){
        String pathName = "src/main/" + path +".txt";
        File myObj = new File(pathName);
        boolean exists = myObj.exists();
        //check if file exists
        if(exists){
            myObj.delete();
                System.out.println("File: " + myObj.getName() + " has been deleted\n");
                return true;
        }else{
            System.out.println("File could not be deleted\n");
        } return false;
    }
    public String responseDELETE(){
        String httpResponse = "HTTP/1.1 202 Accepted\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 202 \r\n"
                + "Content-Lenght: 31 \r\n\r\n" + "The Message has been deleted...";
        return httpResponse;
    }
    public String responseErrorDELETE(){
        String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 404 \r\n"
                + "Content-Lenght: 35 \r\n\r\n" + "File not found or doesn't exists...";
        return httpResponse;
    }

    public boolean contentPUT(String path, String message){
        String pathName = "src/main" + path +".txt";
        File myObj = new File(pathName);
        boolean exists = myObj.exists();

        if(exists){
            myObj.delete();
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
                writer.write(message);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            System.out.println("--> Message has been updated\n");
            return true;
        }else{
            System.out.println("File could not be replaced\n");
        } return false;

    }
    public String responsePUT(){
        String httpResponse = "HTTP/1.1 202 Accepted\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 202 \r\n"
                + "Content-Lenght: 32 \r\n\r\n" + "The Message has been replaced...";
        return httpResponse;
    }
    public String responseErrorPUT(){
        String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Server: Alec \r\n"
                + "Status: 404 \r\n"
                + "Content-Lenght: 35 \r\n\r\n" + "File not found or doesn't exists...";
        return httpResponse;
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
