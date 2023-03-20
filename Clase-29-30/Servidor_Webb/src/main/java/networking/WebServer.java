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

package networking;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import model.frontend.FrontendSearchRequest;
import model.frontend.FrontendSearchResponse;
import networking.WebClient;
import networking.Aggregator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.StringTokenizer;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.Headers;



import java.math.BigInteger;


import java.util.Random;
import java.io.ByteArrayOutputStream;
public class WebServer {

    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/searchipn";
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/searchipn";
    private static final String WORKER_ADDRESS_3 = "http://localhost:8083/searchipn";

    private static final String STATUS_ENDPOINT = "/status";
    private static final String HOME_PAGE_ENDPOINT = "/";
    private static final String HOME_PAGE_UI_ASSETS_BASE_DIR = "/ui_assets/";
    private static final String ENDPOINT_PROCESS = "/procesar_datos";
    private static final String SEARCHIPN_ENDPOINT = "/searchipn";
    private long endTime = 0;
    private long startTime = 0;
    private boolean bandera = true;
    private final int port;
    private HttpServer server;
    private final ObjectMapper objectMapper;

    public WebServer(int port) {
        this.port = port;
        //ObjectMapper para el uso de JSON con POJO's
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public void startServer() {//Cremos endpoints y sus manejadores
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //Cremos los puntos 
        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext taskContext = server.createContext(ENDPOINT_PROCESS);
        HttpContext searchContext = server.createContext(SEARCHIPN_ENDPOINT);

        //Los manejadores
        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);
        searchContext.setHandler(this::handleSearchRequest);
        //Pagina de inicio
        // handle requests for resources
        HttpContext homePageContext = server.createContext(HOME_PAGE_ENDPOINT);
        homePageContext.setHandler(this::handleRequestForAsset);

        server.setExecutor(Executors.newFixedThreadPool(8)); // HIlos del servidor 
        server.start();
    }

    private void handleRequestForAsset(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) { //Verificamos si es get o post 
            exchange.close();
            return;
        }

        byte[] response;

        String asset = exchange.getRequestURI().getPath();
        //Obtendremos el URL que sera modificado
        if (asset.equals(HOME_PAGE_ENDPOINT)) {//SI el url es igual al de el index
            response = readUiAsset(HOME_PAGE_UI_ASSETS_BASE_DIR + "index.html");//Cargamos los archivos del frontend de index
        } else {
            response = readUiAsset(asset);
        }
        addContentType(asset, exchange);//Agregamos los tipos de archivos al header

        sendResponse(response, exchange); // Cargamos los archivos a la vista 
    }

    private byte[] readUiAsset(String asset) throws IOException {
        InputStream assetStream = getClass().getResourceAsStream(asset);

        if (assetStream == null) {
            return new byte[]{};
        }

        return assetStream.readAllBytes();
    }

    private static void addContentType(String asset, HttpExchange exchange) {
        String contentType = "text/html";
        if (asset.endsWith("js")) {
            contentType = "text/javascript";
        } else if (asset.endsWith("css")) {
            contentType = "text/css";
        }
        exchange.getResponseHeaders().add("Content-Type", contentType);
    }
    //Manejador de la respuesta del AJAX
    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
        if(!bandera){
            return;
        }
        bandera = false;
        try {            
            //readValue Lee el JSON que se envio y lo convierte al POJO de FrontendSearchRequest
            FrontendSearchRequest frontendSearchRequest = objectMapper.readValue(exchange.getRequestBody().readAllBytes(), FrontendSearchRequest.class); 
            System.out.println("Los datos recibidos en el servidor web son:" + frontendSearchRequest.getSearchQuery());
            String frase = frontendSearchRequest.getSearchQuery();// Obtenemos la info del POJO
            //--------------------
            List<String> results = procesando(frase.split("-"));
            
            //--------------------
            StringTokenizer st = new StringTokenizer(frase);
            //Dividimos el string y enviamos a un nuevo POJO la frase y el conteo
            //Creamos denuevo el JSON
            ArrayList<FrontendSearchResponse> lista = new ArrayList<FrontendSearchResponse>();
            for (String result : results) {
            
            FrontendSearchResponse frontendSearchResponse = new FrontendSearchResponse(result, 1);
            lista.add(frontendSearchResponse);
            }
             byte[] responseBytes = objectMapper.writeValueAsBytes(lista);
             String s = new String(responseBytes, StandardCharsets.UTF_8);
      System.out.println("Output : " + s);
            sendResponse(responseBytes, exchange);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private List<String> procesando(String[] palabras){
        Aggregator aggregator = new Aggregator();
        String task1 = "1757600,"+palabras[0];
        String task2 = "1757600,"+palabras[1];
        String task3 = "1757600,"+palabras[2];
        
        List<String> results = aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2,WORKER_ADDRESS_3),Arrays.asList(task1, task2,task3));

        float prom = 0;
        int indice = 0,fi = 0;
        for (String result : results) {
            System.out.println(result);
        }
        return results;
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException { //Servidor vivo!
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        String responseMessage = "El servidor está vivo\n";
        sendResponse(responseMessage.getBytes(), exchange);
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
    }
        private void handleSearchRequest(HttpExchange exchange) throws IOException {
            System.out.println("LLEGO AL SERVIDOR");
    //Se verifica si se solicitó un método POST
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
    //Se busca clave X-Debug para verificar estado del servidor
        Headers headers = exchange.getRequestHeaders();
    boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }
    //Calculando tiempo que tardó el proceso completo
        startTime = System.nanoTime();
    //Se recupera el cuerpo del mensaje de la transacción HTTP
    //Este va a contener la cantidad de tokens aleatorios
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
    //Se envían los números recibidos para calcular una operación con calculateResponse
        byte[] responseBytes = calculateSearchResponse(requestBytes);
    //Se termina el cálculo y se toma el tiempo final
    //Si se activó el modo Debug se imprime el tiempo que tardó
    //Se envía respuesta al cliente
        sendResponse(responseBytes, exchange);
    }

    private byte[] calculateSearchResponse(byte[] requestBytes) {
        //Separando parámetros recibidos
    String bodyString = new String(requestBytes);
        String[] stringNumbers = bodyString.split(",");
    //Convirtiendo parámetros al formato deseado
    int n = Integer.parseInt(stringNumbers[0]);
    String cadena = stringNumbers[1];
    //Declaración de cadenota
    StringBuilder cadenota = new StringBuilder();
    Random rand = new Random();
    //Inicializando cadenota con "n" tokens de letras aleatorias
    int contador = 0;
    for(int i = 0; i < n*4; i++)
        if(contador != 3){
            cadenota.append((char)(rand.nextInt(26) + 65));
            contador++;
        } else if(contador == 3 && i != (n*4-1)) {
            cadenota.append((char)32);
            contador = 0;
        }
    //Buscando la cadena deseada y contando cantidad de apariciones
    int apariciones = 0, buscar = 0;
    while(buscar != -1){
        buscar = cadenota.indexOf(cadena, buscar);
        if(buscar != -1){
            apariciones++;
            buscar += 4;
        }
    }
    long finishTime = System.nanoTime();
        return String.format("\nLa cantidad de veces que se encontró la Palabra '" + cadena + "' fue: %d Tiempo: "+(startTime-finishTime), apariciones).getBytes();
    }
}
