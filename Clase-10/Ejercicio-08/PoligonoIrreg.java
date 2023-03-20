public class PoligonoIrreg{
    private static Coordenada[] vertices;
    private static int contador = 0;

    public PoligonoIrreg(int tam){
        vertices = new Coordenada[tam];
    }

    public static void anadeVertice(Coordenada coordenada) {
        if(contador != vertices.length){
            vertices[contador] = coordenada;
        }else{
            System.out.println("El limite establecido a sido sobrepasado");
        }
        contador++;
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