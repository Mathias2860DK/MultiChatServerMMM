package server;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcherr extends Thread {
    BlockingQueue<String> allMsg;
    ConcurrentMap allNameOutPutStream;

    public Dispatcherr(BlockingQueue<String> allMsg, ConcurrentMap<String, OutputStream> allNameOutputStream){
        this.allMsg =allMsg;
        this.allNameOutPutStream = allNameOutputStream;
    }

    public void addClient(ConcurrentMap<String,OutputStream> allNameOutPutStreamTemp){

    }


    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            String msgInQueue = allMsg.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
