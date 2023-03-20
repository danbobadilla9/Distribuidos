// Proyecto 02 GUIO de los poligonos
// Bobadilla Segundo Dan Israel 
// 4CM11
import java.lang.reflect.InvocationTargetException;


public class Probador {
     public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        int cantidad = Integer.parseInt(args[0]);
        System.out.println("Si el programa no compila es porque necesita una version anterior al jdk");
        System.out.println("Intente con: sudo apt install openjdk-11-jdk");
        SimpleGui2 gui = new SimpleGui2(cantidad);
        gui.setVisible(true);
    }
}
