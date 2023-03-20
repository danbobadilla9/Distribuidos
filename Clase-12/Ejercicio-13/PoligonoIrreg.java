import java.util.*;
import java.lang.*;
import java.security.SecureRandom;
import java.io.*;
public class PoligonoIrreg{
    private static  List<Coordenada> vertices = new ArrayList<Coordenada>();
    private static int contador = 0;

    public PoligonoIrreg(){
        anadeVertice();
    }
    public static void anadeVertice() {
        SecureRandom aleatorio = new SecureRandom();
        for(int i = 0 ; i <10; i++){
            int aux = aleatorio.nextInt(2);

            vertices.add(new Coordenada((aux != 0) ? -1*aleatorio.nextInt(100)+aleatorio.nextDouble():aleatorio.nextInt(100)+aleatorio.nextDouble(),(aux != 0) ? -1*aleatorio.nextInt(100)+aleatorio.nextDouble():aleatorio.nextInt(100)+aleatorio.nextDouble()));
        }
    }
    
    public static void ordenaVertices(){
        Collections.sort(vertices, new SortbyMagnitud());
    }

    @Override
    public String toString( ) {
        String inf = "";
        for(Coordenada cor: vertices){
            inf+=cor;
        }
        return inf;
    }
}

class SortbyMagnitud implements Comparator<Coordenada>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Coordenada c1, Coordenada c2)
    {
        return Double.compare(c1.getMagnitud(),c2.getMagnitud() );
    }
}
 