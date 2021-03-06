package server;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class ChatServer {

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        String logFile = "log.txt";  //Do we need this
        int port = 8818;
        Server server = new Server(port);
        server.start();
    }
}
