/*
 *  MIT License
 *
 *  Copyright (c) 2019 Michael Pogrebinsky - Distributed Systems & Cloud Computing with Java
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
import classAux.Demo;
import classAux.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {
    private static  String WORKER_ADDRESS_1 = "http://";
    private static  String WORKER_ADDRESS_2 = "http://";
    private static  String WORKER_ADDRESS_3 = "http://";

    public static void main(String[] args) {
        //Recibiendo las variables

        Aggregator aggregator = new Aggregator();
        WORKER_ADDRESS_1 += args[0]+"/task";
        WORKER_ADDRESS_2 += args[1]+"/task";
        WORKER_ADDRESS_3 += args[2]+"/task";
        List<String> datos = new ArrayList<String>();
        for(int i = 3; i<args.length; i++){
            datos.add(args[i]);
        }
        List<String> results = aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1,WORKER_ADDRESS_2,WORKER_ADDRESS_3),datos,true);
        for (String[] result : results) {
            System.out.println("Servidor -> "+result[0]+" Palabra -> "+result[1]+" Veces encontrada -> "+result[2]);
        }
    }
}
