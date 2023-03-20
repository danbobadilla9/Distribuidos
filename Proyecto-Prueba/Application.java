// Proyecto 03 Burcador de palabras
// Bobadilla Segundo Dan Israel 
// 4CM11
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {
    private static  String WORKER_ADDRESS_1 = "http://";
    private static  String WORKER_ADDRESS_2 = "http://";
    private static  String WORKER_ADDRESS_3 = "http://";

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();
        WORKER_ADDRESS_1 += args[0]+"/task";
        WORKER_ADDRESS_2 += args[1]+"/task";
        WORKER_ADDRESS_3 += args[2]+"/task";
        List<String> datos = new ArrayList<String>();
        for(int i = 3; i<args.length; i++){
            datos.add(args[i]);
        }
        aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1,WORKER_ADDRESS_2,WORKER_ADDRESS_3),datos,true);

    }
}
