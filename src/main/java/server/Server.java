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
    BlockingQueue<String> allMsg = new ArrayBlockingQueue<>(250);
    OutputStream outputStream;
    ConcurrentHashMap<String, OutputStream> allNameOutputStream = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, OutputStream> getAllNameOutputStream() {
        return allNameOutputStream;
    }

    public Server(int serverPort) {
        this.serverPort = serverPort;
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
                allNameOutputStream.put(inputArray[1], outputStream);//inputArray[1] = name
                //whoIsOnline();
                ServerWorker worker = new ServerWorker(inputArray[1] ,allMsg, outputStream,reader);
                allMsg.add("ONLINE#" + inputArray[1]);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void whoIsOnline() throws IOException {
        String outputMsg= "";
        String finalOutputMsg ="";
        ConcurrentHashMap<String, OutputStream> allNameOutputStream = getAllNameOutputStream();
        for (String key : allNameOutputStream.keySet()) {
            System.out.println(key + " : " + allNameOutputStream.get(key));
            //ONLINE#musti,mark
            outputMsg += key + ",";
            finalOutputMsg = "ONLINE#" + outputMsg + "\n";
            allNameOutputStream.get(key).write(finalOutputMsg.getBytes());
        }
       // allMsg.add(finalOutputMsg);
    }
}

