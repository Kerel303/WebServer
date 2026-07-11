import java.util.concurrent.ExecutionException;

public class DatabaseHandler {
    private DatabaseLibrarian librarian;
    private Thread librarianThread;
    public DatabaseHandler(){
        librarian = new DatabaseLibrarian();
        librarianThread = new Thread(librarian);
        librarianThread.setName("Database-Librarian-Thread");
        librarianThread.start();
    }
    
    public void pushCommand(String command){
        DatabaseJob job = new DatabaseJob(command);
        this.librarian.addJobToQueue(job);
    }

    public String getResponse(String command){
        DatabaseJob job = new DatabaseJob(command);
        this.librarian.addJobToQueue(job);

        try{
            return job.getResponse().get();
        }catch(InterruptedException | ExecutionException e){
            if(e instanceof InterruptedException){
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return "Error: Database communication failed";
        }
    }
}
