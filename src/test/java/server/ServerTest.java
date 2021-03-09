package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

class ServerTest {
    //ArrayList<ServerWorker> serverWorkers;
    Server server;
    Thread t1;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
server = new Server(8818);
t1 = new Thread(server);

    }




    @org.junit.jupiter.api.Test
    void run() throws InterruptedException {
        t1.start();
        assertNotNull(server.getWorkerList().get(0));
        t1.join();

    }


    @org.junit.jupiter.api.Test
    void removeWorker() {
    }
}