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

import networking.WebClient;
import classAux.Demo;
import classAux.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private WebClient webClient;
    private String auxiliar = "";
    private int control = 3;
    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<String[]> sendTasksToWorkers(List<String> workersAddresses, List<String> datos,boolean cargado) {
        CompletableFuture<String>[] futures = new CompletableFuture[datos.size()];
        for (int i = 0; i < workersAddresses.size(); i++) {
            System.out.println("Palabra enviada hacia: "+datos.get(i)+" Hacia el servidor: "+workersAddresses.ge);
            String workerAddress = workersAddresses.get(i);
            byte[] requestPayload = datos.get(i).getBytes();
            futures[i] = webClient.sendTask(workerAddress, requestPayload);
        }

        List<String[]> results = Stream.of(futures).map(
            future -> {
                String aux = "";
                while(!future.isDone()){}
                try {
                    aux = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (ExecutionException c){
                    c.printStackTrace();
                }
                if(control < datos.size()){
                    byte[] requestPayload = datos.get(control).getBytes();
                    futures[control] = webClient.sendTask(auxiliar = aux.split(" ")[0], requestPayload);
                }
                control++;
                return aux.split(" ");
            }
        ).collect(Collectors.toList());
        return results;
    }

}
