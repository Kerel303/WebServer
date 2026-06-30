import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private volatile boolean isOn = true;
    private static String body = """
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

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("http/index.html"));

            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            reader.close();

            this.body = sb.toString();
            log(body);

        }catch(IOException e){
            logError(e.getMessage());
        }
    }

    // Start / Stop
    public void start(){
        isOn = true;
        log("Started on port: " + port);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        try{
            while(isOn){
                Socket clientSocket = serverSocket.accept();

                threadPool.execute(new ClientHandler(clientSocket));
            }
        }catch(IOException e){
            logError(e.getMessage());
        }finally{
            threadPool.shutdown();
        }
    }
    public void stop() throws IOException{
        isOn = false;
        serverSocket.close();
    }

    // Functions
    public static void log(String msg){
        System.out.println("[LOG] Server Message: " + msg);
    }
    public static void logError(String err){
        System.out.println("[LOG] Server Error: " + err);
    }
    public static void logIncoming(String msg){
        System.out.println("[LOG] Received: " + msg);
    }
    public static void logBorder(){
        System.out.println("------------------------------------------------------------");
    }
    public static String getHttpTime(){
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
    public static int getByteLength(){
        return body.getBytes(StandardCharsets.UTF_8).length;
    }
    
    
    // Setters
    public void turnOnOff(boolean TurnOff){
        if(TurnOff){
            isOn = false;
        }else{
            isOn = true;
        }
    }
    // Getters
    public static String getBody(){
        return body;
    }
    public int getPort(){
        return port;
    }
}
