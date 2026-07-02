import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger implements Runnable {
    private final LinkedBlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final String fileName;
    public Logger(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        this.fileName = "log_" + LocalDate.now().format(formatter) + ".txt";
    }

    public void enqueueLog(String msg){
        logQueue.offer(msg);
    }

    @Override
    public void run() {
         while(true) {
            try{
                String logMessage = logQueue.take();
                writeToLogFile(logMessage);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
         }
    }

    private void writeToLogFile(String data){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("logs/" + fileName, true));
            String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            bw.write("[" + timeStamp + "] " + data + "\n");
            bw.close();
        }catch(IOException e){
            Server.logError("Wasn't able to write to log-file: " + e.getMessage());
        }
    }
}
