// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11
import networking.WebClient;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public void sendTasksToWorkers(List<String> workersAddresses, List<String> datos,boolean cargado) {
        CompletableFuture<String[]>[] futures = new CompletableFuture[workersAddresses.size()];

        List<URI> urls = new ArrayList<URI>();
        for (String url : workersAddresses) {
            urls.add(URI.create(url));
        }
        webClient.sendTask(urls, datos);
    }

}
