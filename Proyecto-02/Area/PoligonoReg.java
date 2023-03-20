// Proyecto 02 Area de un poligono de radio 1
// Bobadilla Segundo Dan Israel 
// 4CM11
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.*;

public class PoligonoReg{
    private static  List<Coordenada> vertices = new ArrayList<Coordenada>();
    private static int nVertices = 0;
    private static int angulo = 0;//Angulo interno
    public PoligonoReg(int cantidadV){
        nVertices = cantidadV;
        angulo = 360/cantidadV;
    }
    
    public String obtieneArea(){
        double ap = Math.abs(Math.cos(Math.toRadians(angulo/2)));
        double l = Math.abs(Math.sin(Math.toRadians(angulo/2)))*2;
        return "El area es: "+(nVertices*ap*l)/2;
    }
}