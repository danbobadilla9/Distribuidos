// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11
package networking;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class WebClient {
    private HttpClient client;

    public WebClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public CompletableFuture<String> sendTask(String url, byte[] requestPayload) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
                .uri(URI.create(url))
                .header("X-debug","true")
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {  
            String aux ="Servidor -> "+response.uri() + " Palabra y veces encontrada -> "+response.body().toString();
            return aux;
        });

    }
}