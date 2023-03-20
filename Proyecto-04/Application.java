import classAux.Demo;
import classAux.SerializationUtils;
import operaciones.Coordenada;
import operaciones.PoligonoIrreg;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
public class Application {
    public static void play(ArrayList<Object> data) {
        Aggregator aggregator = new Aggregator();
        List<byte[]> results = aggregator.sendTasksToWorkers(data);

    }
}
