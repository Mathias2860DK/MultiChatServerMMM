package server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcherr extends Thread {
    BlockingQueue<String> allMsg;
    ConcurrentMap <String, OutputStream> allNameOutputStream;

    public Dispatcherr(BlockingQueue<String> allMsg, ConcurrentMap<String, OutputStream> allNameOutputStream){
        this.allMsg =allMsg;
        this.allNameOutputStream = allNameOutputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msgInQueue = allMsg.take();
                //handleDispatcher(msgInQueue);
                handleMsg(msgInQueue);

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDispatcher(String msgInQueue) throws IOException {
        //CONNECT#kurt
        String[] splitString = msgInQueue.split("#");
            if (splitString[0].contains("SEND")) {
                handleMsg(msgInQueue);
            } else if (splitString[0].contains("dis")) {
                handleConnect(splitString[1]);
            }
    }

    private void send(String msgInQueue) throws IOException {
        String[]inputArray = msgInQueue.split("#");
        String[]modtager = inputArray[1].split(",");
        String outputMsg = "MESSAGE#" + modtager[0] + "#" + inputArray[2];

        for (int i = 1; i < modtager.length; i++) {
            findClientWriterByName(modtager[i]).write(outputMsg.getBytes());
        }

    }

    private void handleMsg(String msgInQueue) throws IOException {
        //SEND#mat#hej mat
        System.out.println("handlemsg");
        String[]inputArray = msgInQueue.split("#");
        //String[]modtager = inputArray[1].split(",");
        if (msgInQueue.contains("ONLINE")){
            whoIsOnline();

        }else if (msgInQueue.contains("SEND")){
            send(msgInQueue);
        }
        else {

        }
    }

    private void handleConnect(String msgInQueue) throws IOException {
        System.out.println("test");
    whoIsOnline();
    }

    private void whoIsOnline() throws IOException {
        String outputMsg = "";
        for (String key : allNameOutputStream.keySet()) {
            System.out.println(key + " : " + allNameOutputStream.get(key));
            //ONLINE#musti,mark
            outputMsg += key + ",";

        }
        String finalOutputMsg = "ONLINE#" + outputMsg + "\n";
        for (String key : allNameOutputStream.keySet()) {
            allNameOutputStream.get(key).write(finalOutputMsg.getBytes());
        }
    }

    private OutputStream findClientWriterByName(String name){
        OutputStream outputStream = null;
        outputStream = allNameOutputStream.get(name);
        return outputStream;
    }
}
