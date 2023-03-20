import networking.WebClient;
import classAux.Demo;
import classAux.SerializationUtils;
import operaciones.Coordenada;
import operaciones.PoligonoIrreg;
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
        String workerAddress ="";
        int auxMv = 0;
        int aux = 0;
        for(int i = 0; i< data.size();i++){
            String datos = (String)data.get(i);
            String[] info = datos.split("-");
            if(info[1].equals("F")&&info[2].equals("A")){
                workerAddress = info[0];
                data.remove(i);
                auxMv = i;
                data.add(auxMv, workerAddress+"-T-A- ");
                break;
            }
            if(info[1].equals("T")&&info[2].equals("A")){
                aux++;
            }
        }
        if(aux == data.size()){
            for(int i = 0; i < data.size();i++){
                String datos = (String)data.get(i);
                String[] info = datos.split("-");
                if(i == (data.size()-1)){
                    data.remove(i);
                    data.add(i, info[0]+"-T-A-"+info[3]);
                }else{
                    data.remove(i);
                    data.add(i, info[0]+"-T-A-"+info[3]);
                }
            }
            String datos = (String)data.get(0);
            String[] info = datos.split("-");
            workerAddress = info[0];
        }
        // Serializamos la data
        byte[] transformado = SerializationUtils.serialize(data);
        futures[0] = webClient.sendTask(workerAddress, transformado);
        System.out.println("ACA 2 YA LLLEGO");
        List<byte[]> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());

        return results;
    }
}
