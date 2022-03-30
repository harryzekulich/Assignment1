iimport java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MyClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 50000);
      
            String[] biggestServer = {""};
            boolean biggestFound = false;
            String currentMsg = "";
            handshake(s);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            
            while (!currentMsg.contains("NONE")) {
              
                sendMsg(s, "REDY\n");
                currentMsg = readMsg(s);
                
                
                if (currentMsg.contains("JOBN")) {
                    String[] JOBNSplit = currentMsg.split(" ");
                  
                    sendMsg(s, "GETS Avail " + JOBNSplit[4] + " " + JOBNSplit[5] + " " + JOBNSplit[6] + "\n");
 
                    currentMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                   
                    currentMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                    if(biggestFound == false){
                        biggestServer = findBiggestServer(currentMsg);
                        biggestFound = true;
                    }

                    currentMsg = readMsg(s);

                    sendMsg(s, "SCHD " + JOBNSplit[2] + " " + biggestServer[0] + " " + biggestServer[1] + "\n");

                    currentMsg = readMsg(s);
                    System.out.println("SCHD: " + currentMsg);
                }
                else if (currentMsg.contains("DATA")) {
                    sendMsg(s, "OK\n");
                }
            }
            sendMsg(s, "QUIT\n");
            s.close();
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
     public static synchronized String readMsg(Socket s) {
        String currentMsg = "FAIL";
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            byte[] byteArray = new byte[dis.available()];
      
            byteArray = new byte[0];
            while (byteArray.length == 0) {
             
                byteArray = new byte[dis.available()];
                dis.read(byteArray);
               
                currentMsg = new String(byteArray, StandardCharsets.UTF_8);
                
                ublic static synchronized void sendMsg(Socket s, String currentMsg) {
        try {

            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            byte[] byteArray = currentMsg.getBytes();
            dout.write(byteArray);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
                      public static void handshake(Socket s) {
        String currentMsg = "";

     sendMsg(s, "HELO\n");

        currentMsg = readMsg(s);
        System.out.println("RCVD: " + currentMsg);
        sendMsg(s, "AUTH " + System.getProperty("user.name") + "\n");
        currentMsg = readMsg(s);
        System.out.println("RCVD: " + currentMsg);
    }
                    
                    public static String[] findBiggestServer(String currentMsg) {
       
        String[] serversAndInfo = currentMsg.split("\n");
        int mostCores = 0;
        String[] currentServer = {""};
        for (int i = 0; i < serversAndInfo.length; i++) {
            currentServer = serversAndInfo[i].split(" ");
            int currentCores = Integer.valueOf(currentServer[4]);
            if (currentCores > mostCores) {
                mostCores = currentCores;
            }
        }
