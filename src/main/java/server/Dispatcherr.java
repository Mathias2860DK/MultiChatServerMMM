package server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcherr extends Thread {
    BlockingQueue<String> allMsg;
    ConcurrentMap <String, OutputStream> allNameOutPutStream;

    public Dispatcherr(BlockingQueue<String> allMsg, ConcurrentMap<String, OutputStream> allNameOutputStream){
        this.allMsg =allMsg;
        this.allNameOutPutStream = allNameOutputStream;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            String msgInQueue = allMsg.take();
            handleMsg(msgInQueue);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMsg(String msgInQueue) throws IOException {
        if (msgInQueue.contains("SEND")){
            String[]inputArray = msgInQueue.split("#");
            String[]modtager = inputArray[1].split(",");
            String outputMsg = "MESSAGE#" + modtager[0] + "#" + inputArray[2];
            findClientWriterByName(modtager[1]).write(outputMsg.getBytes());
        }
    }

    private void handleConnect(){

    }

    private void whoIsOnline(){

    }

    private OutputStream findClientWriterByName(String name){
        OutputStream outputStream = null;
        outputStream = allNameOutPutStream.get(name);
        return outputStream;
    }
}
