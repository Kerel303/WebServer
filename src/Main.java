import java.io.IOException;
import javax.swing.SwingUtilities;

public class Main {
    public static Server server;
    public static Thread serverThread;
    public static Logger logger;
    public static Thread loggerThread;
    public static GUI gui;
    public static int width = 900;
    public static int height = 600;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new GUI(width, height);
        });
        server = new Server();
        logger = new Logger();
        startLogger();
    }

    public static void startServer(){
        if(serverThread == null || !serverThread.isAlive()){
            serverThread = new Thread(server::start);
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
        loggerThread.start();
    }
}