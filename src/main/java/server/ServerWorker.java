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

    @Override
    public void run() {
        handleClientSocket();
    }

    private void handleClientSocket() {
    }
}
