
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class MyClient {

    public static void main(String[] args) {




        
        try {
            Socket s = new Socket("localhost", 50000);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            // Set up variables to be used
            String[] biggestServer = { "" };
            boolean biggestFound = false;
            String currentMsg = "";
            handshake(s);

            // While there are more jobs to be done
            while (!currentMsg.contains("NONE")) {
                // Tells the server that the client is ready for a command and reads it
                sendMsg(s, "REDY\n");
                currentMsg = dis.readLine();

                // Checks to see if the received command is a new job
                if (currentMsg.contains("JOBN")) {
                    String[] JOBNSplit = currentMsg.split(" ");

                    dout.write(("GETS Capable " + JOBNSplit[4] + " " + JOBNSplit[5] + " " + JOBNSplit[6] + "\n").getBytes());
                    
                    currentMsg = dis.readLine();
                    int serverNum = Integer.parseInt(currentMsg.split(" ")[1]);
                    dout.write(("OK\n").getBytes());


                    ArrayList<String> nameIt = new ArrayList<String>();

                    for(int i=0; i<serverNum; i++){
                        nameIt.add(dis.readLine());
                        
                    }

                    dout.write(("OK\n").getBytes());
                    dout.write(("SCHD " + JOBNSplit[2] + " " + nameIt.get(0).split(" ")[0] + " " + nameIt.get(0).split(" ")[1] + "\n").getBytes());
                    
                   


            //         // Ask what servers are available to run a job with the given data
            //         sendMsg(s, "GETS Avail " + JOBNSplit[4] + " " + JOBNSplit[5] + " " + JOBNSplit[6] + "\n");
            //         // Reads the msg saying what data is about to be sent and responds with "OK"
            //         currentMsg = readMsg(s);
            //         sendMsg(s, "OK\n");

            //         // Reads the available servers data and responds with "OK"
            //         currentMsg = readMsg(s);
            //         sendMsg(s, "OK\n");

            //         // Checks to see if the biggest Server has been found
            //         // Used as a flag to see that it was found on the first round
            //         if (biggestFound == false) {
            //             biggestServer = findBiggestServer(currentMsg);
            //             biggestFound = true;
            //         }

            //         // Reads "." from the server
            //         currentMsg = readMsg(s);

            //         // Schedule the current job to the biggest server (SCHD JobNumber ServerName
            //         // ServerNumber)
            //         sendMsg(s, "SCHD " + JOBNSplit[2] + " " + biggestServer[0] + " " + biggestServer[1] + "\n");

            //         // Read the next JOB
            //         currentMsg = readMsg(s);
            //         // System.out.println("SCHD: " + currentMsg);
            //     } else if (currentMsg.contains("DATA")) {
            //         sendMsg(s, "OK\n");
                }
            }
            // Sends "Quit" to the server to end the session and then closes the socket
        
    }
    
     public void write(String text) {
        try {
            out.write((text + "\n").getBytes());
            // System.out.print("SENT: " + text);
            out.flush();
        } catch (IOException i) {
            System.out.println("ERR: " + i);
        }
    }
        
         public String Read() {
        String text = "";
        try {
            text = in.readLine();
            // System.out.print("RCVD: " + text);
            inputString = text;

        } catch (IOException i) {
            System.out.println("ERR: " + i);
        }
        return text;
    }


    // Function used to read a msg from the server
    public static synchronized String readMsg(Socket s) {
        String currentMsg = "FAIL";
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            byte[] byteArray = new byte[dis.available()];
            // Reset byteArray to have 0 elements so it is ready to recieve
            // a msg and wait until a msg is recieved
            byteArray = new byte[0];
            while (byteArray.length == 0) {
                // Read the bytestream from the server
                byteArray = new byte[dis.available()];
                dis.read(byteArray);
                // Make a string using the recieved bytes and print it
                currentMsg = new String(byteArray, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the msg just recieved from the server
        return currentMsg;
    }

    // Function used to send a msg to the server
    public static synchronized void sendMsg(Socket s, String currentMsg) {
        try {
            // Converts the String msg to an array of bytes and sends them to the server
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            byte[] byteArray = currentMsg.getBytes();
            dout.write(byteArray);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handshake(Socket s) {
        String currentMsg = "";

        // Initiate handshake with server
        //sendMsg(s, "HELO\n");
        dout.write((s, "HELO\n").getBytes());

        // Check for response from sever for "HELO"
        currentMsg = readMsg(s);
        // System.out.println("RCVD: " + currentMsg);

        // Authenticate with a username (ubuntu)
        sendMsg(s, "AUTH " + System.getProperty("user.name") + "\n");

        // Check to see if sever has ok'd the client's AUTH
        currentMsg = readMsg(s);
        // System.out.println("RCVD: " + currentMsg);
    }

    // Used to find the biggest server available to run the current job
    public static String[] findBiggestServer(String currentMsg) {
        // All the servers in the currentMsg being split into an array
        String[] serversAndInfo = currentMsg.split("\n");
        // Sets up variables
        int mostCores = 0;
        String[] currentServer = { "" };
        // Searches for the most cores a server holds in the given available servers
        for (int i = 0; i < serversAndInfo.length; i++) {
            currentServer = serversAndInfo[i].split(" ");
            int currentCores = Integer.valueOf(currentServer[4]);
            if (currentCores > mostCores) {
                mostCores = currentCores;
            }
        }
        // Finds and returns the biggest server (The one with the most cores)
        for (int i = 0; i < serversAndInfo.length; i++) {
            currentServer = serversAndInfo[i].split(" ");
            int currentCores = Integer.valueOf(currentServer[4]);
            if (currentCores == mostCores) {
                return currentServer;
            }
        }
        return currentServer;

    }

    // public Boolean getsCapable(String string) {

    // String[] server = string.split(" ");

    // if (server[2].equals("active") == false && server[2].equals("booting") ==
    // false
    // && server[2].equals("unavailable") == false) {
    // return true;
    // }

    // else {
    // return false;
    // }

    // }

    public String getsCapable2(ArrayList<String> array, ArrayList<Integer> numJobs) {
        ArrayList<String> temp = new ArrayList<String>();
        // ArrayList<Integer> temp1 = new ArrayList<Integer>();

        // int min = Collections.min(numJobs);

        for (int i = 0; i < array.size(); i++) {
            String[] split = array.get(i).split(" ");

            // temp1.add(Integer.parseInt(split[4]));
            System.out.println("working");
            temp.add(array.get(i));
        }

        // int max = Collections.max(temp1);

        // int pos = temp1.lastIndexOf(max);

        return temp.get(1);
    }

}
