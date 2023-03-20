// Proyecto 02 Area de un poligono de radio 1
// Bobadilla Segundo Dan Israel 
// 4CM11
public class PruebaPoli{
    public static void main (String[] args) {
        int cantidadV = Integer.parseInt(args[0]);
        PoligonoReg poligonoReg = new PoligonoReg(cantidadV);
        System.out.println(poligonoReg.obtieneArea());
    }
}