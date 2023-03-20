package networking;
import classAux.Demo;
import classAux.SerializationUtils;
import operaciones.Coordenada;
import operaciones.PoligonoIrreg;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.net.http.HttpTimeoutException;
public class WebClient {
    private HttpClient client;

    public WebClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public CompletableFuture<byte[]> sendTask(String url, byte[] requestPayload) {
        int cantidades = 0;
        int total = 0;
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
                .uri(URI.create(url))
                .header("X-debug","true")
                .build(); 
        System.out.println("Datos que seran enviados\n");
        ArrayList<Object> data = new ArrayList<Object>();
        data = (ArrayList<Object>)SerializationUtils.deserialize(requestPayload);
        for(int i = 0; i < data.size();i++){
            String datos = (String)data.get(i);
            String[] info = datos.split("-");
            if(info[1].equals("T")&&info[2].equals("A")&&!info[3].equals(" ")){
                total+= Integer.parseInt(info[3]);
                cantidades++;
                System.out.println("Servidor -> "+info[0]+" Tiempo en segundos -> "+info[3]);
            }
        }
        System.out.println("Tiempo Promedio -> "+total/cantidades);
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
        .thenApply(resposen -> {
            return resposen.body();
        })
        .exceptionally(e -> {
            if(e.getCause() instanceof ConnectException){
                System.out.println("ERROR Servidor Desconectado: "+url+" "+e.getMessage()); 
            }
            return ("ERROR Servidor Desconectado: ").getBytes();
        });




    }
}