// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11
package networking;

import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class WebClient {
    private HttpClient client;

    public WebClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public void sendTask(List<URI> urls, final List<String> datos) {
        // HttpRequest request = HttpRequest.newBuilder()
        // .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
        // .uri(URI.create(url))
        // .header("X-debug","true")
        // .build();
        // return client.sendAsync(request,HttpResponse.BodyHandlers.ofString()).thenApply(response -> {String aux =response.uri() + " "+response.body().toString();return aux.split(" ");});
        List<HttpRequest> requests = new ArrayList<HttpRequest>();
        int control = 0;
        for (int i = 0; i < datos.size(); i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofByteArray(datos.get(i).getBytes()))
                    .uri(urls.get(control))
                    .header("X-debug", "true")
                    .build();
            requests.add(request);
            System.out.println("Servidor -> "+urls.get(control)+" Palbra -> "+datos.get(i));
            if ((control+1) % 3 == 0){
                    control = 0;
                    continue;
            }
                control++;

        }



        
        List<CompletableFuture<String>> data = requests.stream()
            .map(request -> client.sendAsync(request,HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                System.out.println("Palbra recibida de: "+response.uri()+" "+response.body().toString()+" Hilo: "+Thread.currentThread().getName());
                return " ";
                
            }))
            .collect(Collectors.toList()); 

        try {
            for (CompletableFuture<String> getData : data) {
                System.out.print(getData.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException c){
            c.printStackTrace();
        }
    }

   
}
