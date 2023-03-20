import java.util.*;
public class PruebaPoli{
    public static void main (String[] args) {

        PoligonoIrreg poligonoIrreg = new PoligonoIrreg(3,2);
        poligonoIrreg.andeV(10, 10);
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(poligonoIrreg);
        
        byte[] transformado = SerializationUtils.serialize(data);
        //Imprimimos los valores
        System.out.println(poligonoIrreg);
        System.out.println("DESERIALIZANDO");
        data = (ArrayList<Object>)SerializationUtils.deserialize(transformado);
        poligonoIrreg = (PoligonoIrreg)data.get(0);
        poligonoIrreg.andeV(15, 15);
        System.out.println(poligonoIrreg);
    }
}