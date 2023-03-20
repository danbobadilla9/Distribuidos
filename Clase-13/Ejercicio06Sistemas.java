import java.security.SecureRandom;
public class Ejercicio06Sistemas{
    public static String palabra = "";
    public static int n=0;
    public  Ejercicio06Sistemas(String n,String palabra) {
        this.n = Integer.parseInt(n);
        this.palabra = palabra;
    }
        public static String generarCadenota() {
        SecureRandom aleatorio = new SecureRandom();
        StringBuilder cadenota = new StringBuilder();
        String aux = "";
        for (int i = 0; i < n * 3; i++) {
            aux += (char) (aleatorio.nextInt(26) + 65);
            if ((i + 1) % 3 == 0) {
                cadenota.append(aux+" ");
                aux ="";
            }
        }
        int ocurrencia = 0; int i = 0;
        i = cadenota.indexOf(palabra);
        while(i >= 0) {
            i = cadenota.indexOf(palabra, i+1);
            ocurrencia++;
        }    
           return  "Numero de ocurrencia: "+ocurrencia;
    }
}