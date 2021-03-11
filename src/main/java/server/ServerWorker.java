package server;


import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;


public class ServerWorker extends Thread {

    private OutputStream outputStream;
    BlockingQueue<String> allMsg;
    BufferedReader reader;
    String[] input;
    String name;

    public ServerWorker(String name, BlockingQueue<String> allMsg, OutputStream outputStream, BufferedReader reader) {
        this.allMsg = allMsg;
        this.name = name;
        this.outputStream = outputStream;
        this.reader = reader;
    }


    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void handleClientSocket() throws IOException, InterruptedException {
        String line;
        boolean go = true;
        //TODO: you must be connected to acces commands.

        while (go) {
            line = reader.readLine();
            System.out.println("handleClientSocket while loop/ kommer vi ind her");
            input = line.split("#");
            if ("CONNECT".equals(input[0])) {
                //handleConnect();
            } else if ("SEND".equals(input[0])) {
                handleSend();
            } else if ("CLOSE".equalsIgnoreCase(input[0])) {
                closeConnection();
            }else{
                closeConnection();
            }
        }
    }

    private void closeConnection() {
        String outputString = input[0] + "#" + name;
        allMsg.add(outputString);
    }

    private void handleSend() throws IOException {
       String outputString = input[0] + "#" + name + "," + input[1] + "#" + input[2];
       //SEND#mat2,mat#hej mat
       allMsg.add(outputString);
        }
}
