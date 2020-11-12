package RestHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException{

        ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ...");
        while(true){
            /*Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }*/

            try(Socket socket = server.accept()) {
                    /*Date today = new Date();
                    String httpResponse = "HTTP/1.1 200 OK\r\n"
                            +"Server: Alec\r\n"
                            + "Content-Type: text/html\r\n"
                            + "Accept-Ranges: bytes \r\n"
                            + "Conent-Lenght: 0 \r\n\r\n" + today;
                    socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                     */
                handleClient(socket);

            }
        }
    }

    private static void handleClient(Socket client) throws IOException{
        System.out.println("Debug: got new client" + client.toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()){
            requestBuilder.append(line + "\r\n");
        }

        String request = requestBuilder.toString();
        System.out.println(request);

        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>();
        for (int h = 2; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);
    }
}