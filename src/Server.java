import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private boolean isOn = true;
    private String body = """
        <html>
            <head>
                <title>Server Home</title>
            </head>
            <body>
                <h1>Hello.</h1>
                You are connected.
            </body>
        </html>
    """;

    // Constructor
    public Server(){
        try{
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            log("Started on port: " + port);

            while(isOn){
                Socket clientSocket = serverSocket.accept();
                log("Detected from " + clientSocket.getPort());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                
                String line;
                while((line = in.readLine()) != null && !line.isEmpty()){
                    logIncoming(line);
                }

                int length = body.length();
                String now = getHttpTime();

                out.write("HTTP/1.0 200 OK\r\n");
                out.write("Date: " + now + "\r\n");
                out.write("Server: Custom Server\r\n");
                out.write("Content-Type: text/html\r\n");
                out.write("Content-Length: " + length + "\r\n");
                out.write("\r\n");

                out.write(body);
                out.flush();

                logBorder();
            }

        }catch(IOException e){
            logError(e.getMessage());
        }
    }

    // Listeners

    // Functions

    private void log(String msg){
        System.out.println("[LOG] Server Message: " + msg);
    }
    private void logError(String err){
        System.out.println("[LOG] Server Error: " + err);
    }
    private void logIncoming(String msg){
        System.out.println("[LOG] Received: " + msg);
    }
    private void logBorder(){
        System.out.println("------------------------------------------------------------");
    }
    private String getHttpTime(){
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
    
    
    // Setters
    public void TurnOnOff(boolean TurnOff){
        if(TurnOff){
            isOn = false;
        }else{
            isOn = true;
        }
    }
    // Getters
    public int getPort(){
        return port;
    }
}
