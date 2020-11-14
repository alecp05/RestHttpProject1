package RestHttp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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

        //only HTTP Form Terms
        while (!(lines = breader.readLine()).isBlank()) {
            build.append(lines + "\r\n");
        }
        String request = build.toString();
        System.out.println(request);

        RequestContext requestClient = new RequestContext(request);
        requestClient.printContexts();

        //--------------------------------
        //which Request is being received
        String usedMethod = requestClient.getMethod();

        if(usedMethod.equals("POST")){
            //check if correct Directory
            if(!requestClient.getPath().equals("/messages") && !requestClient.getPath().equals("/messages/")){
                System.out.println("POST Request failed\n");
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
                System.out.println("Message:");
                System.out.println(requestClient.getPayload());
                //payload sent to handlerPOST
                EndpointHandler post = new EndpointHandler();
                post.saveMessagePOST(requestClient.getPayload(), requestClient.getPath());
                client.getOutputStream().write(post.responsePOST().getBytes(StandardCharsets.UTF_8));
            }

        }else if (usedMethod.equals("GET")) {
            EndpointHandler getAll = new EndpointHandler();
            String allmsg;
            //check correct Directory
            if(requestClient.getPath().length()<9){
                EndpointHandler get = new EndpointHandler();
                client.getOutputStream().write(get.responseErrorGET().getBytes(StandardCharsets.UTF_8));
            }else {
                allmsg = getAll.printAllGET(requestClient.getPath());
                //check if File found
                if(allmsg.equals("NOTFOUND")){
                    String httpResponse = getAll.responseErrorGET2();
                    client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                }else {
                    //all msg printed
                    String httpResponse = getAll.responseGET() + allmsg;
                    client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                }
            }
        }else if(usedMethod.equals("DELETE")){
            EndpointHandler delete = new EndpointHandler();
            //check if deleteFunction was successful
            if(delete.deleteDEL(requestClient.getPath())){
                String httpResponse = delete.responseDELETE();
                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }else{
                String httpResponse = delete.responseErrorDELETE();
                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }
        }else if(usedMethod.equals("PUT")){
            EndpointHandler put = new EndpointHandler();

            int counter = Integer.parseInt(requestClient.getContentLength());
            int value;
            //saving payload by characters
            for (int j = 0; j < counter; j++) {
                value = breader.read();
                content.append((char) value);
            }
            //payload being set
            requestClient.setPayload(content.toString());
            System.out.println("Message:");
            System.out.println(requestClient.getPayload());

            //check if putFunction was successful
            if(put.contentPUT(requestClient.getPath(),requestClient.getPayload())){
                String httpResponse = put.responsePUT();
                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }else{
                String httpResponse = put.responseErrorPUT();
                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }
        }
        client.close();
    }

}