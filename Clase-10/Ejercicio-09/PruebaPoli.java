public class PruebaPoli{
    public static void main (String[] args) {
        long inicio = System.nanoTime();
        PoligonoIrreg poligonoIrreg = new PoligonoIrreg(10000000);
        poligonoIrreg.anadeVertice();
        long fin = System.nanoTime();
        System.out.println("Nanosegundos: "+(fin-inicio));
    }
}