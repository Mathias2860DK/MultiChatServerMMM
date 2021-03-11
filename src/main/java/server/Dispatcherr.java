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
                handleMsg(msgInQueue);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void send(String msgInQueue) throws IOException {
        String[]inputArray = msgInQueue.split("#");
        String[]modtager = inputArray[1].split(",");
        String outputMsg = "MESSAGE#" + modtager[0] + "#" + inputArray[2] + "\n";

        if (inputArray[1].contains("*")){
            for (String key : allNameOutputStream.keySet()) {
                allNameOutputStream.get(key).write(outputMsg.getBytes());
            }
        } else {
            for (int i = 1; i < modtager.length; i++) {
                findClientWriterByName(modtager[i]).write(outputMsg.getBytes());
            }

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
        } else if(msgInQueue.contains("CLOSE")){
            closeConnection(msgInQueue);
        }
        else {

        }
    }

    private void closeConnection(String msgInQueue) throws IOException {
        System.out.println("kommer vi ind i close con");
        String[] splitArray = msgInQueue.split("#");
        String userWhoWantsToCloseConnection = splitArray[1];
        allNameOutputStream.remove(userWhoWantsToCloseConnection);
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
