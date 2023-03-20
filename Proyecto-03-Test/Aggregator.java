// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11

import networking.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private WebClient webClient;
    private String auxiliar = "";
    private int control = 3;
    public Aggregator() {
        this.webClient = new WebClient();
    }

    public void sendTasksToWorkers(List<String> workersAddresses, List<String> datos,boolean cargado) {

        int n = 3;
        List<MultithreadingDemo> object = new ArrayList<MultithreadingDemo>(); 
        int controlador = 0;
        for (int i = 0; i < n; i++) {
            System.out.println("Palabra -> "+datos.get(i)+" Servidor -> "+workersAddresses.get(i));
            object.add(new MultithreadingDemo(workersAddresses.get(i),datos,controlador,webClient));
            controlador++;
            object.get(i).start();
        }
        while(true){
            for(int i= 0; i< 3;i++){
                if(!object.get(i).isAlive()){
                    controlador++;
                    object.remove(i);
                    if(controlador < datos.size())
                        System.out.println("Palabra -> "+datos.get(controlador)+" Servidor -> "+workersAddresses.get(i));
                    object.add(i, new MultithreadingDemo(workersAddresses.get(i),datos,controlador,webClient));
                    object.get(i).start();
                }
            }
            if(controlador > datos.size()){
                break;
            }
        }

    }

}

