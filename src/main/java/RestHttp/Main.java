package RestHttp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException{


        ServerSocket serverSocket;
        try {
            //ready to listen incoming connection on port 8080
            serverSocket = new ServerSocket(8080);
            System.out.println("Listening for connection on port 8080 ...");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (true) {
                //accepting incoming connection
                Socket socket = serverSocket.accept();
                    System.out.println("---1");
                    handleRequest(socket);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket client) throws IOException{

        //read the content of the InputStream with BufferedReader
        BufferedReader breader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder build = new StringBuilder();
        String lines;
        StringBuilder content = new StringBuilder();

        while (!(lines = breader.readLine()).isBlank()) {
            build.append(lines + "\r\n");
        }
        String request = build.toString();
        System.out.println("---2\n" + request);

        RequestContext requestClient = new RequestContext(request);
        requestClient.printContexts();

        //--------------------------------
        //which Request is being received
        String usedMethod = requestClient.getMethod();
        if(usedMethod.equals("POST")){
            if(requestClient.getPath().length()>9){
                EndpointHandler post = new EndpointHandler();
                client.getOutputStream().write(post.responseErrorPOST().getBytes(StandardCharsets.UTF_8));
            }else {
                int counter = Integer.parseInt(requestClient.getContentLength());
                int value;
                //saving payload by characters
                for (int j = 0; j < counter; j++) {
                    value = breader.read();
                    content.append((char) value);
                }
                //payload being set
                requestClient.setPayload(content.toString());
                System.out.println(requestClient.getPayload());
                //payload sent to handlerPOST
                EndpointHandler post = new EndpointHandler();
                post.saveMessagePOST(requestClient.getPayload(), requestClient.getPath());
                client.getOutputStream().write(post.responsePOST().getBytes(StandardCharsets.UTF_8));
            }

        }else if (usedMethod.equals("GET")) {
            EndpointHandler getAll = new EndpointHandler();
            String allmsg;
            allmsg = getAll.printAllGET(requestClient.getPath());

            //all msg printed
            String httpResponse = getAll.responseGET() + allmsg;
            client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
        }
        client.close();
    }

}