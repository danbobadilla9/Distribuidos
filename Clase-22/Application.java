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

import java.util.Arrays;
import java.util.List;

public class Application {
    private static final String WORKER_ADDRESS_1 = "http://34.125.108.157/searchipn";
    private static final String WORKER_ADDRESS_2 = "http://34.125.155.102/searchipn";
    private static final String WORKER_ADDRESS_3 = "http://34.125.117.104/searchipn";
    private static final String WORKER_ADDRESS_4 = "http://34.125.57.217/searchipn";

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();
        String task1 = "1757600,IPN";
        String task2 = "1757600,IPN";
        String task3 = "1757600,IPN";
        String task4 = "1757600,IPN";
        
        List<String> results = aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2,WORKER_ADDRESS_3, WORKER_ADDRESS_4),
                Arrays.asList(task1, task2,task3, task4));
        float prom = 0;
        int indice = 0,fi = 0;
        for (String result : results) {
            System.out.println(result);
            indice = result.indexOf("tomo")+5;
            fi= result.indexOf("nanosegundos")-1;
            prom += Float.parseFloat(result.substring(indice, fi));
        }
        System.out.println("Tiempo Promedio: "+prom/4+" Nanosegundos");
    }
}
