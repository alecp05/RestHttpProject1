package RestHttp;

import java.io.*;
import java.util.*;

public class EndpointHandler {

    private int messageID;

    public void saveMessagePOST(String message, String path){

        int messageID_temp = 1;
        String pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
        List<Integer> list = new ArrayList<Integer>();
        int numbers;

        //searching through all files in specified directory
        File dir = new File("src/main/messages");
        String[] allFiles = dir.list();

        if (allFiles == null) {
            System.out.println("does not exist or is not a directory");
        } else {

            //search for free messageID
            for (int i = 0; i < allFiles.length; i++) {
                String filename = allFiles[i];
                //filename = "src/main/messages/" + filename;

                String[] splits = filename.split("\\.");
                numbers = Integer.parseInt(splits[0]);
                list.add(numbers);
                //System.out.println(numbers);

                //System.out.println(allFiles[i]);
                //if(filename.equals(pathFile)) {
                    //messageID_temp++;
                //}
                //pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
                //System.out.println(pathFile + "+++");
            }
                Collections.sort(list);
            for(int j = 0; j<list.size();j++)
            {
                if(list.get(j) == (j+1))
                    messageID_temp++;
            }
            pathFile = "src/main"+ path + "/" + Integer.toString(messageID_temp) +".txt";
            System.out.println(list);

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
        String httpResponse = "HTTP/1.1 201 Created\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Content-Lenght: 0 \r\n\r\n" + response;
        return httpResponse;
    }
    public String responseErrorPOST(){

        String httpResponse = "HTTP/1.1 404 not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Content-Lenght: 0 \r\n\r\n" + "Wrong Directory!";
        return httpResponse;
    }

    public String printAllGET(String path) throws FileNotFoundException {

        StringBuilder allMsg = new StringBuilder();

        if(path.length()>9){
            String[] splitting = path.split("/");
            String messageID;
            if(splitting.length>2){
                messageID = splitting[2];
                allMsg.append("MessageID: "+ messageID + " \r\n");
                String pathName = "src/main" + path + ".txt" ;

                File myObj = new File(pathName);
                Scanner myReader = new Scanner(myObj);

                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    allMsg.append(data+ "\r\n\n");
                }
                myReader.close();
            }

        }else{
            String pathName = "src/main" + path;
            //searching through all files in specified directory
            File dir = new File(pathName);
            String[] children = dir.list();

            if (children == null) {
                System.out.println("does not exist or is not a directory");
            } else {

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
        }
        return allMsg.toString();

    }

    public String responseGET(){
        String httpResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Content-Lenght: 0 \r\n\r\n";
        return httpResponse;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
