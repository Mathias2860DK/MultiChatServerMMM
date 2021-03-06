package server;


import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class ServerWorker extends Thread {
    //nisse
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
                closeConnection(0);
            } else {
                closeConnection(1);
            }

        }

    }

    private void closeConnection(int errorNumber) throws IOException {
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        server.removeWorker(this);

        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("src/main/java/server/MyLogFile.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("");

            if (errorNumber == 0) {
                outputStream.write("Error 0) Normal close, ".getBytes());
                logger.info("Error 0) Normal close, ");
            }
            if (errorNumber == 1) {
                outputStream.write("Error 1) Illegal input was received, ".getBytes());
                logger.info("Error 1) Illegal input was received, ");
            }
            if (errorNumber == 2) {
                outputStream.write("Error 2) User not found, ".getBytes());
                logger.info("Error 2) User not found, ");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientSocket.close(); //should return CLOSE#0 --> TODO: Exeption
    }


    private void handleSend(String sendTo, String text) throws IOException {
        String error = "Brugeren findes ikke";
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        String[] input = sendTo.split(",");
        for (ServerWorker serverWorker : serverWorkerList) {
            if (sendTo.equals("*")) {
                String sendMsg = "MESSAGE#" + login + "#" + text + "\n";
                serverWorker.sendToClients(sendMsg);
                error = "";
            }
            for (int i = 0; i < input.length; i++) {
                if (input[i].contains(serverWorker.getLogin())) {
                    String sendMsg = "MESSAGE#" + login + "#" + text + "\n";
                    serverWorker.sendToClients(sendMsg);
                    error = "";
                }

            }
        }
       // outputStream.write(error.getBytes());
        if(error.equals("Brugeren findes ikke")){
            closeConnection(2);
        }
    }



    private void sendToClients(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }

    private void handleConnect(String userName) throws IOException {
        this.login = userName;
        List<ServerWorker> serverWorkerList = server.getWorkerList();
        String msgAll = "";
        for (ServerWorker serverWorker : serverWorkerList) {
            if (serverWorker.getLogin() != null) {
                msgAll += serverWorker.getLogin() + ",";
            }
            serverWorker.sendWhosisOnline();
        }
    }

    private void sendWhosisOnline() throws IOException {
        List<ServerWorker> serverWorkerList = server.getWorkerList();
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
    }
}
