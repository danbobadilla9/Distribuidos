// Proyecto 04 Topologia
// Bobadilla Segundo Dan Israel 
// 4CM11
import classAux.Demo;
import classAux.SerializationUtils;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
public class Application {
    public static void play(ArrayList<Object> data) {
        Aggregator aggregator = new Aggregator();
        List<byte[]> results = aggregator.sendTasksToWorkers(data);
        for (byte[] bs : results) {
            String s = new String(bs, StandardCharsets.UTF_8);
            if(s.charAt(0)== 'D'){
                reSend(Integer.parseInt(s.charAt(1)+""),data,aggregator);
            }
        }
    }
    public static void reSend(int siguiente,ArrayList<Object> data,Aggregator aggregator){
        data.remove(siguiente);
        for(int i = 0; i< data.size();i++){
            String informacion = (String)data.get(i);
            String[] info = informacion.split("-");
            data.remove(i);
            data.add(i,info[0]+"-"+(siguiente-1)+"-"+info[2]+"-"+info[3]);
        }
        List<byte[]> results = aggregator.sendTasksToWorkers(data);
    }
}
