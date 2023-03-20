// Proyecto 04 Topologia
// Bobadilla Segundo Dan Israel 
// 4CM11
import networking.WebClient;
import classAux.Demo;
import classAux.SerializationUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
public class Aggregator {
    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<byte[]> sendTasksToWorkers(ArrayList<Object> data) {
        CompletableFuture<byte[]>[] futures = new CompletableFuture[1];
        int auxMv = 0;
        int aux = 0;
        String datos = (String)data.get(0);
        int siguiente = Integer.parseInt(datos.split("-")[1])+1;
        if(siguiente >= data.size()){
            datos = (String)data.get(0);
        }else{
            datos = (String)data.get(siguiente);
        }
        String workerAddress = datos.split("-")[0];
        System.out.println("\n Dirección Enviar  -> "+workerAddress);
        
            for(int i = 0; i< siguiente;i++){
                String separador = (String)data.get(i);
                String[] info = separador.split("-");
                if(siguiente == data.size()){
                    data.remove(i);
                    data.add(i,info[0]+"-"+(0)+"-C-"+info[3]);
                }else if(info[2].equals("C")){
                    data.remove(i);
                    data.add(i,info[0]+"-"+siguiente+"-C-"+info[3]);
                }else{
                    data.remove(i);
                    data.add(i,info[0]+"-"+siguiente+"-N-"+info[3]);
                }
            }

        // Serializamos la data
        byte[] transformado = SerializationUtils.serialize(data);
        futures[0] = webClient.sendTask(workerAddress, transformado);
        List<byte[]> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());

        return results;
    }
}
