
import com.sun.net.httpserver.*;
import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;

public class WebServer {
    private static final String TASK_ENDPOINT = "/task"; // Routers
    private final int port; // Puerto Cliente
    private HttpServer server;// Implementando un servidor con la clase HttpServer

    public static void main(String[] args) {
        int serverPort = 8080;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]); // Lee el puerto o utiliza el default: 8080
        }
        WebServer webServer = new WebServer(serverPort); // Instancia el servidor
        webServer.startServer(); // Inicia el servidor
        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }

    // Constructor
    public WebServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        HttpContext taskContext = server.createContext(TASK_ENDPOINT);
        taskContext.setHandler(this::handleTaskRequest);// Asigna un método manejador a los endpoints
        server.setExecutor(Executors.newFixedThreadPool(8));// Se proveen 8 hilos para que el servidor trabaje
        server.start();// Se inicia el servidor en segundo plano (Hilo)
    }

    // Manejador Del Router Task
    private void handleTaskRequest(HttpExchange exchange) throws IOException { // HttpExchange contiene todo lo
                                                                               // relacionado con la transacción HTTP
                                                                               // actual
        boolean isDebugMode = false;
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) { // Si no es peticion por POST se cierra el servidor
            exchange.close();
            return;
        }
        Headers headers = exchange.getRequestHeaders(); // Obtiene los Headers de la petición
        if (headers.containsKey("X-Test") && headers.get("X-Test").get(0).equalsIgnoreCase("true")) { // Reenvia datos
                                                                                                      // dummy si es una
                                                                                                      // prueba
            String dummyResponse = "123\n";
            sendResponse(dummyResponse.getBytes(), exchange, true);
            return;
        }

        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {// Se busca clave
                                                                                                       // X-Debug para
                                                                                                       // verificar
                                                                                                       // estado del
                                                                                                       // servidor
            isDebugMode = true;
        }

        long startTime = System.nanoTime();// Calculando tiempo que tardó el proceso completo

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();// Se recupera el cuerpo del mensaje de la
                                                                       // peticion HTTP
        byte[] responseBytes = searchWord(requestBytes); // Se envian al metodo para realizar el calculo
        long finishTime = System.nanoTime();// Se termina el cálculo y se toma el tiempo final
        // Si se activó el modo Debug se imprime el tiempo que tardó
        if (isDebugMode) {
            String debugMessage = String.format("La operacion tomo %d nanosegundos", finishTime - startTime);
            // Se coloca el tiempo en el header X-Debug-Info
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
        }
        // Se envía respuesta al cliente
        sendResponse(responseBytes, exchange, true);
        // try {
        // Thread.sleep(10000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // System.out.println("TERMINADO");
        // sendResponse(responseBytes, exchange,true);

    }

    private byte[] searchWord(byte[] requestBytes) throws IOException{
        String content = new String(Files.readAllBytes(Paths.get("biblia.txt")),StandardCharsets.ISO_8859_1);
        String cadena = new String(requestBytes, StandardCharsets.UTF_8);
        int apariciones = 0, buscar = 0;
        while(buscar != -1){
            buscar = content.indexOf(cadena, buscar);
            if(buscar != -1){
                apariciones++;
                buscar += cadena.length();
            }
        }
        System.out.println("Palabra a buscar: "+cadena);
        System.out.println("Veces repetida en la biblia: "+apariciones);
        return (cadena+" "+apariciones).getBytes();
    }

    // --------------------------------------------END MULTI
    private void sendResponse(byte[] responseBytes, HttpExchange exchange, boolean bandera) throws IOException {
        // Agrega estatus code 200 de éxito y longitud de la respuesta
        exchange.sendResponseHeaders(200, responseBytes.length);
        // Se escribe en el cuerpo del mensaje
        OutputStream outputStream = exchange.getResponseBody();
        // Se envía al cliente por medio del Stream
        outputStream.write(responseBytes);
        outputStream.flush();
        if (bandera) {
            outputStream.close();
            exchange.close();
        }

    }
}