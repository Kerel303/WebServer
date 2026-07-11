import java.util.concurrent.CompletableFuture;

public class DatabaseJob {
    private String command;
    private CompletableFuture<String> futureResponse;

    DatabaseJob(String command){
        this.command = command;
        this.futureResponse = new CompletableFuture<>();
    }

    public String getCommand(){
        return command;
    }
    public CompletableFuture<String> getResponse(){
        return futureResponse;
    }
}
