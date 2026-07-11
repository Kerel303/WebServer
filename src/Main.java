import java.io.IOException;
import javax.swing.SwingUtilities;

public class Main {
    public static Server server;
    public static Thread serverThread;
    public static Logger logger;
    public static Thread loggerThread;
    public static Router router;
    public static DatabaseHandler dataBaseHandler;
    public static GUI gui;
    public static int width = 900;
    public static int height = 600;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new GUI(width, height);
        });
        try{
            router = new Router();
        }catch(Exception e){
            // Everything is handled internally
        }
        dataBaseHandler = new DatabaseHandler();
        logger = new Logger();
        startLogger();
        server = new Server();
    }

    public static void startServer(){
        if(serverThread == null || !serverThread.isAlive()){
            serverThread = new Thread(server::start);
            serverThread.setName("Web-Server-Thread");
            serverThread.start();
        }
    }
    public static void stopServer(){
        try{
            server.stop();
            Server.log("Server stopped.");
        }catch(IOException e){
            Server.logError(e.getMessage());
        }
    }
    private static void startLogger(){
        loggerThread = new Thread(logger);
        loggerThread.setName("Logger-Thread");
        loggerThread.start();
    }
}