import java.util.*;
public class PoligonoIrreg{
    private static  List<Coordenada> vertices = new ArrayList<Coordenada>();
    private static int contador = 0;
    public PoligonoIrreg(){
    }
    public static void anadeVertice(Coordenada coordenada) {
        vertices.add(coordenada);
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