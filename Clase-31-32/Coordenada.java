public class Coordenada {

    private double x, y;

    public Coordenada(double x, double y) {

        this.x = x;

        this.y = y;

    }

    //Metodo getter de x

    public double abcisa( ) { return x; }

 

    //Metodo getter de y

    public double ordenada( ) { return y; }

    public void region(){
        if(x < 0 && y <0){
            System.out.println("Cuadrante 3");
        }else if(x > 0 && y > 0){
            System.out.println("Cuadrante 1");
        }else if(x > 0 && y < 0){
            System.out.println("Cuadrante 4");
        }
    }

    public void radio(){
        System.out.println("El cuadrado es -> "+(x*x)+(y*y));
    }

    //Sobreescritura del método de la superclase objeto para imprimir con System.out.println( )

    @Override

    public String toString( ) {

        return "[" + x + "," + y + "]";

    }

}