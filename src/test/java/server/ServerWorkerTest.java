package server;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class ServerWorkerTest {
ServerWorker serverWorker;
BlockingQueue<String> allMsg;
Thread testThread;
Socket socket;
ServerSocket serverSocket;
InputStream inputStream;
String userInput;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        String name = "mat";
        allMsg = new ArrayBlockingQueue<>(250);
        userInput = "SEND#bent#hey fra mat";
        //socket = serverSocket.accept();
       // OutputStream outputStream  = socket.getOutputStream();
       //inputStream = socket.getInputStream();
       // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        serverWorker = new ServerWorker(name,allMsg,null,null);
        testThread = new Thread(serverWorker);
    }

    @Test
    void handleSend(){
        allMsg.add(userInput);
        int expected = 1;
        int acutal = allMsg.size();

        assertEquals(expected,acutal);
    }

    @Test
    void textServerWorkerSendMsg() throws InterruptedException {
        testThread.start();
        allMsg.add(userInput);
        testThread.join();
        int expected = 1;
        int acutal = allMsg.size();

        assertEquals(expected,acutal);
    }
}