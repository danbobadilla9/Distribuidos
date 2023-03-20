import java.security.SecureRandom;
public class PoligonoIrreg{
    private  Coordenada[] vertices;
    
    public PoligonoIrreg(int tam){
        vertices = new Coordenada[tam];
    }
    

    public  void anadeVertice() {
        SecureRandom aleatorio = new SecureRandom();

        // CASO 01) Uso de New
        for(int i=0;i<vertices.length;i++){
            vertices[i] = new Coordenada(aleatorio.nextInt(10),aleatorio.nextInt(10));
        }

        // CASO 02) Uso de Setters
        // for(int i=0;i<vertices.length;i++){
        //     vertices[i] = new Coordenada();
        //     vertices[i].setAbcisa(aleatorio.nextInt(10));
        //     vertices[i].setOrdenada(aleatorio.nextInt(10));
        // }

    }

    @Override
    public String toString( ) {
        String inf = "";
        for(int i = 0; i<vertices.length; i++){
            inf+=vertices[i];
        }
        return inf;
    }
}