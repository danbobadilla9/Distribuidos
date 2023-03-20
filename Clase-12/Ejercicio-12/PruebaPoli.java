public class PruebaPoli{
    public static void main (String[] args) {
        //Enviamso el tamaño al constructor
        PoligonoIrreg poligonoIrreg = new PoligonoIrreg();
     
        //Creamos objetos de tipi Coordenada
        Coordenada coordenada1 = new Coordenada(2,3);
        Coordenada coordenada2 = new Coordenada(1,3);
        Coordenada coordenada3 = new Coordenada(3,3);
     
        //Utilizamos el metodo anade vertice
        poligonoIrreg.anadeVertice(coordenada1);
        poligonoIrreg.anadeVertice(coordenada2);
        poligonoIrreg.anadeVertice(coordenada3);
     
        //Imprimimos los valores
        System.out.println(poligonoIrreg);
    }
}