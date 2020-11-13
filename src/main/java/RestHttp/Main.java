package RestHttp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@SuppressWarnings("InfiniteLoopStatement")

public class Main {

    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException{


        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Listening for connection on port 8080 ...");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (true) {

                Socket socket = serverSocket.accept();
                    System.out.println("---1");
                    handleRequest(socket);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket client) throws IOException{

        //System.out.println("Debug: got new client" + client.toString());
        //InputStreamReader readContent = new InputStreamReader(client.getInputStream());
        //BufferedReader breader = new BufferedReader(readContent);
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder build = new StringBuilder();
        String lines;
        while (!(lines = br.readLine()).isBlank()) {
            build.append(lines + "\r\n");
        }

        /*while((lines= breader.readLine())!=null){
            if(lines.isBlank())
                break;
            build.append(lines + "\r\n");
            System.out.println(build);
        }*/

        /*InputStream is = client.getInputStream();
        StringBuilder result = new StringBuilder();
        while(is.available() > 0) {
            result.append((char) is.read());
        }*/


        String request = build.toString();
        System.out.println("---2\n" + request);

        String[] allTerms = request.split("\r\n");
        String[] oneTerm = allTerms[0].split(" ");
        String method = oneTerm[0];
        String path = oneTerm[1];
        String version = oneTerm[2];
        String host = allTerms[1].split(" ")[1];

        List<String> headers = new ArrayList<>();
        for (int h = 2; h < allTerms.length; h++) {
            String header = allTerms[h];
            headers.add(header + "\r\n");
        }

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
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