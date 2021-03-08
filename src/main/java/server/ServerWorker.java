package server;


import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;


public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null; //tænker vi skal bruge login og validerer brugeren. men behøves ikke instanitieres i konstruktøren
    private OutputStream outputStream;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        //TODO: you must be connected to acces commands.
        while ((line = reader.readLine()) != null) {
            String[] input = line.split("#");
            if ("CONNECT".equals(input[0])) {
                handleConnect(input[1]);
            } else if ("SEND".equals(input[0])) {
                handleSend(input[1], input[2]);
            } else if ("CLOSE".equalsIgnoreCase(input[0])) {
                closeConnection();
            }

        }

    }

    private void closeConnection() throws IOException {
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        server.removeWorker(this);

        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }

        clientSocket.close(); //should return CLOSE#0 --> TODO: Exeption
    }

    private void handleSend(String sendTo, String text) throws IOException {
        String error = "Brugeren findes ikke \n";
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        String [] input = sendTo.split(",");
        for (ServerWorker serverWorker : serverWorkerList) {
            if (sendTo.equals("*")){
                String sendMsg = "MESSAGE#" + login + "#" + text + "\n";
                serverWorker.sendToClients(sendMsg);
            }
            for (int i = 0; i < input.length; i++) {
                if (input[i].contains(serverWorker.getLogin())){
                    String sendMsg = "MESSAGE#" + login + "#" + text + "\n";
                    serverWorker.sendToClients(sendMsg);
                    error = "";
                }

            }
        }
        outputStream.write(error.getBytes());
    }




    private void sendToClients(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }

    private void handleConnect(String userName) throws IOException {
        this.login = userName;
    List<ServerWorker> serverWorkerList = server.getWorkerList();
    String msgAll ="";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null){
                msgAll += serverWorker.getLogin() +",";
            }
            serverWorker.sendWhosisOnline();
        }
    }

    private void sendWhosisOnline() throws IOException {
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        String msgAll = "ONLINE#";
        //Sends online command to all other online clients
        for (ServerWorker serverWorker: serverWorkerList) {
            if (serverWorker.getLogin() != null){
                msgAll += serverWorker.getLogin() +",";
            }
        }
        String finalMsg = msgAll + "\n";
        outputStream.write(finalMsg.getBytes());
        //very handsome matia
    }
}
