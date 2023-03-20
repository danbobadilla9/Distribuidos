// Proyecto 02 GUIO de los poligonos
// Bobadilla Segundo Dan Israel 
// 4CM11
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimpleGui2 extends JFrame {

    public static List<Poligonos> poligonos = new ArrayList<Poligonos>();
    public JFrame topFrame;
    public Panel p;
    public int bandera = 0;
    public Timer timer;

    public SimpleGui2(int cantidad) throws InterruptedException, InvocationTargetException {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                p = new Panel(cantidad);
                add(p);
            }
        });
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(5000);
                remove(p);
                revalidate();
                repaint();
                Thread.sleep(5000);
                p.bandera = 1;
                p.ordenarPoligonos();
                System.out.println("Ordenados");
                add(p);
//                revalidate();
                int delay = 1000; //milliseconds
                ActionListener taskPerformer = new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        repaint();
                        
                    }
                };
                timer = new Timer(delay, taskPerformer);
                timer.setRepeats(true);
                timer.start();
//                repaint();
//                revalidate();
                return null;
            }
        }.execute();

    }

    private class Panel extends JPanel {

        public Graphics g;
        public int bandera = 0;
        public int contador = 0;
        public int cantidad = 0;
        public Panel (int cantidad ){
            this.cantidad = cantidad;
        }
        @Override
        public void paintComponent(Graphics g) {
            this.g = g;
            g.setColor(Color.blue);
            
            if (bandera == 0) {
                for (int i = 0; i < cantidad; i++) {
                    poligonos.add(new Poligonos());
                    poligonos.get(i).draw(g);
                    System.out.println("area: " + poligonos.get(i).obtieneArea());
                }

            } else {
                poligonos.get(0).centrar();
               
                
                for(int i = 0 ; i <= contador ; i++){
                    if(contador > cantidad-1){
                        contador = cantidad -2;
                        timer.stop();
                        repaint();
                        break;
                    }
                    if(i == 0){
                        poligonos.get(i).centrar();
                    }else{
                        poligonos.get(i).ordenar(i);
                    }
                    poligonos.get(i).draw(g);
                }
                contador++;
            }

        }

        public void ordenarPoligonos() {
            Collections.sort(poligonos, new SortbyMagnitud());
        }

    }
}

class SortbyMagnitud implements Comparator<Poligonos> {

    public int compare(Poligonos c1, Poligonos c2) {
        return Integer.compare(c1.obtieneArea(), c2.obtieneArea());
    }
}
