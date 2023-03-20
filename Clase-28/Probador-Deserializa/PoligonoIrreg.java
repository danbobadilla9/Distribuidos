import java.util.*;
import java.io.Serializable;
public class PoligonoIrreg implements Serializable{
    public ArrayList<Coordenada> vertices = new ArrayList<Coordenada>();
    public int contador = 0;
    public PoligonoIrreg(int x,int y){
        vertices.add(new Coordenada(x, y));
    }
   public void andeV(int x, int y){
       vertices.add(new Coordenada(x, y));
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