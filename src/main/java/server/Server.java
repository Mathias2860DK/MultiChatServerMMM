package server;

import java.io.*;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server {
    private final int serverPort;
    Dispatcherr dispatcherr;
    BlockingQueue<String> allMsg = new ArrayBlockingQueue<>(250);
    OutputStream outputStream;
    ConcurrentHashMap<String, OutputStream> allNameOutputStream = new ConcurrentHashMap<>();

    private ArrayList<ServerWorker> workerList = new ArrayList<>(); //TODO: MABYE A HASHMAP!?

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public Server(int serverPort, Dispatcherr dispatcherr) {
        this.serverPort = serverPort;
        this.dispatcherr = dispatcherr;
    }

    public List<ServerWorker> getWorkerList() {
        return workerList;
    }


    public void runProgram() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            Dispatcherr dispatcherr = new Dispatcherr(allMsg, allNameOutputStream);
            dispatcherr.start();
            while (true) {
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                this.outputStream = clientSocket.getOutputStream();
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String input = reader.readLine(); //CONNECT#kurt
                String[] inputArray = input.split("#");
                allNameOutputStream.put(inputArray[1], outputStream);
                handleConnect(allNameOutputStream);
                //allNameOutputStream.put("kurt",outputStream);
                //ServerWorker worker = new ServerWorker(this, clientSocket, allMsg);
                ServerWorker worker1 = new ServerWorker(inputArray[1] ,allMsg, outputStream,reader);
                //workerList.add(worker);
                worker1.start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }

    public void handleConnect(ConcurrentHashMap allNameOutputStream) throws IOException, InterruptedException {
       // allNameOutputStream.

    /*    List<ServerWorker> serverWorkerList = server.getWorkerList();
        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }
        dispatcherr.add(userName); //bare for at tjekke om det virker.
    }*/
    }
}
