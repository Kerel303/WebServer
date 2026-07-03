import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final String body;

    public ClientHandler(Socket clientSocket, String body){
        this.clientSocket = clientSocket;
        this.body = body;
    }

    @Override
    public void run() {
        String toLog = "Detected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        Server.log(toLog);
        Main.logger.enqueueLog(toLog);
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))){
             
            List<String> parts = new ArrayList<>();
            String line;
            StringBuilder sb = new StringBuilder();

            int wichLine = 0;
            while((line = in.readLine()) != null && !line.isEmpty()){
                wichLine++;
                String[] buffer = line.split(" ");
                if(wichLine == 1){
                    for(String el : buffer){
                        parts.add(el);
                    }
                }
                sb.append(line + "\n");
            }
            Server.logIncoming("\n" + sb.toString());

            if(parts.size() < 3){
                // We will send a code about wrong input
                return;
            }

            String method = parts.get(0);
            String path = parts.get(1);
            String typeOfConnection = parts.get(2);

            if(!method.equals("GET")){
                // Send a code forbidden
                return;
            }

            if(!typeOfConnection.equals("HTTP/1.1")){
                // Send a code about wrong type of connection
                return;
            }

            if(path.equals("/favicon.ico")){
                sendIcon(clientSocket);
                return;
            }if(path.equals("/")){
                sendHtml(out);
                return;
            }else{
                // Send a code about page non existing
                return;
            }
        }catch(IOException e) {
            Server.logError(e.getMessage());
        }finally{
            try{
                clientSocket.close();
            }catch(IOException e){
                Server.logError(e.getMessage());
            }
        }
        Server.logBorder();
    }

    private void sendHtml(BufferedWriter out) throws IOException{
        String now = Server.getHttpTime();

        out.write("HTTP/1.0 200 OK\r\n");
        out.write("Date: " + now + "\r\n");
        out.write("Server: Custom Server\r\n");
        out.write("Content-Type: text/html; charset=UTF-8\r\n");
        out.write("Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n");
        out.write("Connection: close\r\n");
        out.write("\r\n");

        out.write(body);
        out.flush();
    }


    private void sendIcon(Socket socket) throws IOException{
        File file = new File("http/favicon.ico");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        OutputStream out = socket.getOutputStream();

        out.write("HTTP/1.0 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        out.write("Content-Type: Image/x-icon\r\n".getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Length: " + fileBytes.length + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write("Connection: close\r\n".getBytes(StandardCharsets.UTF_8));
        out.write("\r\n".getBytes(StandardCharsets.UTF_8));

        out.write(fileBytes);
        out.flush();
    }
    
}
