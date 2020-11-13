package RestHttp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException{


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
        InputStream iS = client.getInputStream();

        StringBuilder build = new StringBuilder();
        String lines;
        String[] lines2;
        StringBuilder content = new StringBuilder();

        while (!(lines = breader.readLine()).isBlank()) {
            build.append(lines + "\r\n");
        }
        String request = build.toString();
        System.out.println("---2\n" + request);

        //splitting the content into terms
        String[] allTerms = request.split("\r\n");
        String[] oneTerm = allTerms[0].split(" ");
        String method = oneTerm[0];
        String path = oneTerm[1];
        String version = oneTerm[2];
        String host = allTerms[1].split(" ")[1];
        String contleng = allTerms[4].split(" ")[1];
        int contentLength = Integer.parseInt(contleng);

        List<String> headers = new ArrayList<>();
        for (int i = 2; i < allTerms.length; i++) {
            String header = allTerms[i];
            headers.add(header + "\r\n");
        }

        if(method.equals("POST")){

            System.out.println(contentLength);

            int value;
            for(int j = 0; j <contentLength ;j++) {
                value = breader.read();
                content.append((char) value);
            }
            System.out.println(content.toString());



        }

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers);
        System.out.println(accessLog);

        Date today = new Date();
        String httpResponse = "HTTP/1.1 200 OK\r\n"
                +"Server: Alec\r\n"
                + "Content-Type: text/html\r\n"
                + "Accept-Ranges: bytes \r\n"
                + "Conent-Lenght: 0 \r\n\r\n" + today;
        client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
        client.close();
    }

}