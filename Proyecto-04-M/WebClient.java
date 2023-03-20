// Proyecto 04 Topologia
// Bobadilla Segundo Dan Israel 
// 4CM11
package networking;
import classAux.Demo;
import classAux.SerializationUtils;
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
    public ArrayList<Object> data = new ArrayList<Object>();
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
        System.out.println("\nDatos que seran enviados\n");
        data = (ArrayList<Object>)SerializationUtils.deserialize(requestPayload);
        String datos = (String)data.get(0);
        final int  siguiente = Integer.parseInt(datos.split("-")[1]);
        String bandera = datos.split("-")[2];
        if(bandera.equals("C")){
            for(int i = 0; i < data.size();i++){
                String informacion = (String)data.get(i);
                String[] info = informacion.split("-");
                System.out.println("Servidor -> "+info[0]+" Tiempo en segundos -> "+info[3]);
                total+=Integer.parseInt(info[3]);
                cantidades++;
            }
        }else{
            for(int i = 0; i < siguiente;i++){
                String informacion = (String)data.get(i);
                String[] info = informacion.split("-");
                System.out.println("Servidor -> "+info[0]+" Tiempo en segundos -> "+info[3]);
                total+=Integer.parseInt(info[3]);
                cantidades++;
            }
        }

        System.out.println("Tiempo Promedio -> "+total/cantidades);
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
        .thenApply(resposen -> {
            return resposen.body();
        })
        .exceptionally(e -> {
            if(e.getCause() instanceof ConnectException){
                System.out.println("\nERROR Servidor Desconectado: "+url+" "+e.getMessage());
            }
            return ("D"+siguiente).getBytes();
        });




    }

}