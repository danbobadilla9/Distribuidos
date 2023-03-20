// Proyecto 02 GUIO de los poligonos
// Bobadilla Segundo Dan Israel 
// 4CM11
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.security.SecureRandom;

public class Poligonos {
    public int aux = 0,aux2= 0;
    public SecureRandom aleatorio = new SecureRandom();
    public int numeroVertices = 0, angulo = 0, radio = 0;
    public double[] verticesX, verticesY;
    public Polygon poligono;
    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public Poligonos() {
        numeroVertices = aleatorio.nextInt(13) + 2;
        if (numeroVertices == 2) {
            numeroVertices++;
        }
        radio = aleatorio.nextInt(200) +20;
        angulo = 360 / numeroVertices;
        verticesX = new double[numeroVertices];
        verticesY = new double[numeroVertices];
        generarCoordenadas();
    }

    public void generarCoordenadas() {
        poligono = new Polygon();
        
        aux = aleatorio.nextInt(screenSize.width - radio) ;
        aux2 = aleatorio.nextInt(screenSize.height - radio) ;
//        System.out.println("largo: "+aux+" alto: "+aux2+" Largo Pantalla: "+screenSize.width+" Alto Pantalla: "+screenSize.height);
        if(aux2 < screenSize.height/2){
            aux2+=radio;
        }else{
            aux2-=radio;
        }
        if(aux < screenSize.width/2){
            aux+=radio;
        }else{
            aux-=radio;
        }
        for (int i = 0; i < numeroVertices; i++) {
            verticesX[i] = Math.cos(Math.toRadians(angulo * (i + 1))) * radio + aux;
            verticesY[i] = Math.sin(Math.toRadians(angulo * (i + 1))) * radio + aux2;
        }
        for (int i = 0; i < numeroVertices; i++) {
            poligono.addPoint((int) verticesX[i], (int) verticesY[i]);
        }
    }

    public void draw(Graphics g) {
        g.drawPolygon(poligono);
    }
    public void ordenar(int desplazamiento){//Centra los demas poligonos 
        aux = 200*desplazamiento;
        System.out.println("aux: "+aux+" des: "+desplazamiento);
//        if(aux > 300){
//            aux = aux -200;
//        }
        aux2 =100 +radio;
        poligono.reset();
        for (int i = 0; i < numeroVertices; i++) {
            verticesX[i] = Math.cos(Math.toRadians(angulo * (i + 1))) * radio + aux;
            verticesY[i] = Math.sin(Math.toRadians(angulo * (i + 1))) * radio + aux2;
            
            poligono.addPoint((int) verticesX[i], (int) verticesY[i]);
        }
    }
    
    public void centrar(){//Centra el mas pequeño al centro 
        aux = screenSize.width/2 ;
        aux2 = screenSize.height/2 ;
        poligono.reset();
        for (int i = 0; i < numeroVertices; i++) {
            verticesX[i] = Math.cos(Math.toRadians(angulo * (i + 1))) * radio + aux;
            verticesY[i] = Math.sin(Math.toRadians(angulo * (i + 1))) * radio + aux2;
            
            poligono.addPoint((int) verticesX[i], (int) verticesY[i]);
        }
    }
    
    public int obtieneArea() {
        double ap = Math.abs(Math.cos(Math.toRadians(angulo / 2)))*radio;
        double l = Math.abs(Math.sin(Math.toRadians(angulo / 2)))*radio * 2;
       return (int)(numeroVertices * ap * l) / 2;
    }
}
