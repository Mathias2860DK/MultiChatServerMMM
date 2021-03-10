package server;


import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;


public class ServerWorker extends Thread {

    //private final Socket clientSocket;
    //private final Server server;
    private String login = null; //tænker vi skal bruge login og validerer brugeren. men behøves ikke instanitieres i konstruktøren
    private OutputStream outputStream;
    BlockingQueue<String> allMsg;
    BufferedReader reader;
    String[] input;

    Dispatcherr dispatcherr;
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
            input = line.split("#");
            System.out.println("test");
            if ("CONNECT".equals(input[0])) {
                handleConnect(input[1]);
            } else if ("SEND".equals(input[0])) {
                handleSend(input[1], input[2]);
            } else if ("CLOSE".equalsIgnoreCase(input[0])) {
                closeConnection(0);
            }else{
                closeConnection(1);
            }

        }

    }

    private void closeConnection(int errorNumber) throws IOException {
  /*      List<ServerWorker> serverWorkerList = server.getWorkerList();
        server.removeWorker(this);

        if(errorNumber == 0){
            outputStream.write("Error 0) Normal close, ".getBytes());
        }
        if(errorNumber == 1){
            outputStream.write("Error 1) Illegal input was received, ".getBytes());
        }
        if(errorNumber == 2){
            outputStream.write("Error 2) User not found, ".getBytes());
        }
        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }

        clientSocket.close(); //should return CLOSE#0 --> TODO: Exeption*/
    }

    private void handleSend(String sendTo, String text) throws IOException {
       String outputString = input[0] + "#" + name + "," + input[1] + "#" + input[2];
       allMsg.add(outputString);
        }
       // outputStream.write(error.getBytes());





    private void sendToClients(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }

    private void handleConnect(String userName) throws IOException, InterruptedException {
    /*    this.login = userName;
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }
        //dispatcherr.add(userName); //bare for at tjekke om det virker.*/
    }

    private void sendWhosisOnline() throws IOException {
  /*      List<ServerWorker> serverWorkerList = server.getWorkerList();
        String msgAll = "ONLINE#";
        //Sends online command to all other online clients
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
        }
        String finalMsg = msgAll + "\n";
        outputStream.write(finalMsg.getBytes());
        //very handsome matia

   */ }
}
