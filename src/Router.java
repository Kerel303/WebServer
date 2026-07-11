import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Router {
    private final Map<String, String> routes = new HashMap<>();

    public Router() throws IOException{
        try(Stream<Path> paths = Files.list(Path.of("http"))){
            paths.filter(Files::isRegularFile).forEach(path -> {
                try{
                    addRoute(path.toString());
                }catch(IOException e) {
                    Server.logError("Failed to add a route from html folder: " + e.getMessage());
                }
            });
        }catch(IOException e){
            Server.logError("Failed to add routes from html folder: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addRoute(String path) throws FileNotFoundException, IOException{
        Path file = Path.of(path);
        String fileName = file.getFileName().toString();

        if(!fileName.endsWith(".html")){
            return;
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));

        String line;
        while((line = reader.readLine()) != null){
            sb.append(line).append("\n");
        }
        reader.close();

        String route;

        if(fileName.equals("index.html")){
            route = "/";
        }else{
            route = "/" + fileName.replace(".html", "");
        }

        routes.put(route, sb.toString());

    }

    public String resolve(String path){
        return routes.get(path);
    }
}
