// Proyecto 04 Topologia
// Bobadilla Segundo Dan Israel 
// 4CM11
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import classAux.SerializationUtils;
public class WebServer {

    private static final String WORKER_ADDRESS_1 = "http://34.125.213.235/task";
    private static final String WORKER_ADDRESS_2 = "http://34.125.146.246/task";
    private static final String WORKER_ADDRESS_3 = "http://34.125.55.214/task";
    private static final String WORKER_ADDRESS_4 = "http://34.125.12.12/task";

    private static final String TASK_ENDPOINT = "/task";
    private static final String START_ENDPOINT = "/start";
    private final int port;
    private HttpServer server;

    public static void main(String[] args) {
        int serverPort = 8080;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }

        WebServer webServer = new WebServer(serverPort);
        webServer.startServer();

        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }

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
        HttpContext startContext = server.createContext(START_ENDPOINT);

        taskContext.setHandler(this::handleTaskRequest);
        startContext.setHandler(this::handleStartRequest);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
        Headers headers = exchange.getRequestHeaders();
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        byte[] responseBytes = sendServidorProcesamiento(requestBytes);
        sendResponse(responseBytes, exchange);
    }
//----------------------------Iniciando servidor
    private void handleStartRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }
        System.out.println("Iniciando EL procesamiento \n");
        String responseMessage = "El servidor está vivo\n";
        sendResponse(responseMessage.getBytes(), exchange);
        Headers headers = exchange.getRequestHeaders();
        sendServidorProcesamiento();
    }
//--------------------------------------------------
    private byte[] sendServidorProcesamiento(byte[] requestBytes) {
        int cantidades = 0;
        int total = 0;
        String workerAddress ="";
        int auxMv = 0;
        //Enviar solicitud
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalDateTime date = LocalDateTime.now();
        int seconds = date.toLocalTime().toSecondOfDay();
        String timeStamp = Integer.toString(seconds);
        //Deserializando para imprimir los datos recibidos
        System.out.println("Datos recibidos y añadiendo el tiempo del servidor: \n");
        ArrayList<Object> data = new ArrayList<Object>();
        data = (ArrayList<Object>)SerializationUtils.deserialize(requestBytes);
        String informacion = (String)data.get(0);
        int siguiente = Integer.parseInt(informacion.split("-")[1])+1;
        if(informacion.split("-")[2].equals("C")){
            siguiente = data.size();
        }
        for(int i = 0; i < siguiente;i++){
            String datos = (String)data.get(i);
            String[] info = datos.split("-");
            if(info[2].equals("C")){
                if(i == Integer.parseInt(info[1])){
                    data.remove(i);
                    data.add(i,info[0]+"-"+info[1]+"-"+info[2]+"-"+timeStamp);
                }
            }else if(i == (siguiente-1)){
                data.remove(i);
                data.add(i,info[0]+"-"+info[1]+"-"+info[2]+"-"+timeStamp);
                break;
            }
            System.out.println("Servidor -> "+info[0]+" Tiempo en segundos -> "+info[3]);
            total+=Integer.parseInt(info[3]);
            cantidades++;
        }
        System.out.println("Tiempo Promedio -> "+total/cantidades);
        //Enviamos los datos al siguiente servidor
        Application.play(data);
        //----------------------------------------------------------------------------------------
        return String.format("El resultado de la multiplicación es %s\n", 10).getBytes();
    }
    public static void sends(ArrayList<Object> data){
        Application.play(data);
    }
    //-------------Iniciando el servidor
    private void sendServidorProcesamiento() {
        //Enviar solicitud
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime date = LocalDateTime.now();
        int seconds = date.toLocalTime().toSecondOfDay();
        String timeStamp = Integer.toString(seconds);
        //---------------------------------------------------------------------------------------
        ArrayList<Object> data = new ArrayList<Object>();
        //Lectura Pagina web "-" Enviando T/ No ah enviando nada F "-" Alive A / Dead D "-" hora en segundos 
        data.add(WORKER_ADDRESS_1+"-0-N-"+timeStamp);
        data.add(WORKER_ADDRESS_2+"-0-N- ");
        data.add(WORKER_ADDRESS_3+"-0-N- ");
        data.add(WORKER_ADDRESS_4+"-0-N- ");
        //----------------------------------------------------------------------------------------
        //Enviamos los datos al siguiente servidor
        Application.play(data);
        //----------------------------------------------------------------------------------------
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exange) throws IOException {
        exange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exange.close();
    }
}