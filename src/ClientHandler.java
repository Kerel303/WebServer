import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final String body;

    public ClientHandler(Socket clientSocket, String body){
        this.clientSocket = clientSocket;
        this.body = body;
    }

    @Override
    public void run() {
        Server.log("Detected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))){
                
            String line;
            while((line = in.readLine()) != null && !line.isEmpty()){
                Server.logIncoming(line);
            }

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
    
}
