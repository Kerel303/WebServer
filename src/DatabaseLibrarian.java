import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseLibrarian implements Runnable {
    private volatile boolean isOn = false;
    private LinkedBlockingQueue<DatabaseJob> queue;

    public DatabaseLibrarian(){
        queue = new LinkedBlockingQueue<>();
    }


    @Override
    public void run() {
        if(isOn){
            return;
        }
        isOn = true;
        executeCommand("SELECT");
        try{
            while(isOn){
                DatabaseJob job = queue.take();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        if(!isOn){
            return;
        }
        isOn = false;
    }

    public void addJobToQueue(DatabaseJob job){
        this.queue.offer(job);
    }

    private String executeCommand(String command){
        switch(command){
            case "SELECT":
                System.out.println("Select");
                break;
            case "CREATE":
                System.out.println("Create");
                break;
            case "INSERT":
                System.out.println("Insert");
                break;
            default:
                System.out.println("Other");
        }

        return "For now";
    }
}
