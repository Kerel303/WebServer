public class Main {
    public static Server server;
    public static Thread serverThread;
    public static void main(String[] args) {
        startServer();
    }

    private static void startServer(){
        server = new Server();
        serverThread = new Thread(server::start);
        serverThread.start();
    }
}