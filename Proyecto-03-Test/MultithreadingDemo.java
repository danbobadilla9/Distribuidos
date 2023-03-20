// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import networking.WebClient;
class MultithreadingDemo extends Thread {
    String worker="";
    List<String> datos = new ArrayList<String>(); 
    int controlador = 0;
    WebClient webClient;
    public MultithreadingDemo(String worker,List<String> datos,int controlador,WebClient webClient){
        this.worker = worker;
        this.datos = datos;
        this.controlador = controlador;
        this.webClient = webClient;
    }
    public void run() {
        try {
                CompletableFuture<String>[] futures = new CompletableFuture[1];
                byte[] requestPayload = datos.get(controlador).getBytes();
                futures[0] = webClient.sendTask(worker, requestPayload);
                List<String> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());
                System.out.println(results );
        } catch (Exception e) {
        }
    }
}
