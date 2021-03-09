package server;

import java.util.concurrent.BlockingQueue;

public class Dispatcherr extends Thread {
    BlockingQueue<String> allMsg;

public void add(String msg){
    allMsg.add(msg);
    System.out.println("TEST:" + allMsg);
}

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            String msgInQueue = allMsg.take();
            System.out.println(msgInQueue);
            System.out.println(allMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
