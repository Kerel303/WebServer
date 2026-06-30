public class Main {
    public static Server server;
    public static void main(String[] args) {
        StartServer();
    }

    private static void StartServer(){
        server = new Server();
    }
}