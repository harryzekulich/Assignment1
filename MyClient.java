import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MyClient {
    
    
    
    
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private DataInputStream din = null;
    // public Server[] servers;
    private int largestServerIndex = 0;
    private String inputString;
    private Boolean completed = false;
    
    
    
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 50000); //setting up variables
            String[] largestServ = {""};
            boolean largestGot = false;
            String tempMsg = "";
            handshake(s);

            //if there are still jobs to be delt with
            while (!tempMsg.contains("NONE")) {
                //this message shows that client is ready for servers command, and reads
                sendMsg(s, "REDY\n");
                tempMsg = readMsg(s);
                
            
                //new job check
                if (tempMsg.contains("JOBN")) {
                    String[] JOBNSplit = tempMsg.split(" ");
                    //grabs the servers avliable
                    sendMsg(s, "GETS Avail " + JOBNSplit[4] + " " + JOBNSplit[5] + " " + JOBNSplit[6] + "\n");
                    //reads message and responds "ok" twice as server needs the response
                    tempMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                    tempMsg = readMsg(s);
                    sendMsg(s, "OK\n");

                    //checks largest server
                    if(largestGot == false){
                        largestServ = findLargestServ(tempMsg);
                        largestGot = true;
                    }
                    //reads "."
                    tempMsg = readMsg(s);

                    //schedule job with the largest serv, jobNuM, ServName, ServNum
                    sendMsg(s, "SCHD " + JOBNSplit[2] + " " + largestServ[0] + " " + largestServ[1] + "\n");

                    //Reads the next JOB
                    tempMsg = readMsg(s);
                    //System.out.println("SCHD: " + tempMsg);
                }
                else if (tempMsg.contains("DATA")) {
                    sendMsg(s, "OK\n");
                }
            }
            //once schedulaing has complete, safe quit of socket
            sendMsg(s, "QUIT\n");
            s.close();
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handshake(Socket s) {
        String tempMsg = "";

        //starts handshake
        sendMsg(s, "HELO\n");

        //checks response
        tempMsg = readMsg(s);
        //System.out.println("RCVD: " + tempMsg);

        // responds with AUTH, and ubuntu username e.g harry zekulich
        sendMsg(s, "AUTH " + System.getProperty("user.name") + "\n");

        //check for OK from server
        tempMsg = readMsg(s);
       // System.out.println("RCVD: " + tempMsg);
    }

     // find largest function
     public static String[] findLargestServ(String tempMsg) {
        // servers split into information array
        String[] information = tempMsg.split("\n");
        //new variables
        int mostCores = 0;
        String[] tempServ = {""};
        //searches for server with most cores
        for (int i = 0; i < information.length; i++) {
            tempServ = information[i].split(" ");
            int tempCore = Integer.valueOf(tempServ[4]);
            if (tempCore > mostCores) {
                mostCores = tempCore;
            }
        }
        //finds that server in array and returns it
        for (int i = 0; i < information.length; i++) {
            tempServ = information[i].split(" ");
            int tempCore = Integer.valueOf(tempServ[4]);
            if (tempCore == mostCores) {
                return tempServ;
            }
        }
        return tempServ;

    }

    // Function used to read a msg from the server
    public static synchronized String readMsg(Socket s) {
        String tempMsg = "FAIL";
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            byte[] byteArr = new byte[dis.available()];
            //byte array resets, ready for new mmsg
            byteArr = new byte[0];
            while (byteArr.length == 0) {
                //reads stream from server
                byteArr = new byte[dis.available()];
                dis.read(byteArr);
                //create new string with read bytes
                tempMsg = new String(byteArr, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Return just recieved msg
        return tempMsg;
    }

       

    // sends message to server function
    public static synchronized void sendMsg(Socket s, String tempMsg) {
        try {
            //converts string msg to array which can be sent to server
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            byte[] byteArr = tempMsg.getBytes();
            dout.write(byteArr);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public Boolean getsCapable(String string) {           

        String[] server = string.split(" ");
        
        if (server[2].equals("active") == false && server[2].equals("booting") == false
                && server[2].equals("unavailable") == false) {
            return true;
        }

        else {
            return false;
        }

    }
    
    
    public String getsCapable2(ArrayList<String> array, ArrayList<Integer> numJobs) {
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<Integer> temp1 = new ArrayList<Integer>();

       
       int min = Collections.min(numJobs);
       

         for(int i=0;i<array.size();i++){ 
            String[] split = array.get(i).split(" ");
            if(Integer.parseInt(split[7]) == min){ 
                temp1.add(Integer.parseInt(split[4]));
                temp.add(array.get(i));
            }
        }
        int max = Collections.max(temp1);
       

          int pos = temp1.lastIndexOf(max);

      
        

       return temp.get(pos);
    }
    
    
}
