import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(din));   // messages from server to be read    
        ArrayList<Server> serverList = new ArrayList<Server>(); //servers avaliable
        Job job = null; // ease of access to job
        Scheduler scheduler = new Scheduler(serverList); //calls scheduler class
  
        String inString = "", outString = "", state = "Initial"; //send, recieve and determine what is needed to be returned
        int sCount = 0; //holds serverNum
        String algorithm = "ATL"; // all to largest algorithm
        boolean listUpdated = false; // sees if arrayList was updated

        for (int i = 0; i < args.length - 1; i++) { // sees what algo to use
            if (args[i].equals("-a")) { // "-a" was used it means there was an specific algorithm selected
                algorithm = args[i + 1]; // if command line had "-a", take argument straight after and save it
                break;
            }
        }

        while (!state.equals("QUIT")) { // keeps communication with the server
            if (!state.equals("Initial") && !(outString.equals("REDY") && state.equals("JobScheduling"))) {
                inString = socketIn.readLine(); // reads message from server & saves it
            }

            
            switch (state) {  // determins what gets sent ot the server
                case "Initial":
                    outString = "HELO";
                    state = "Authorisation";
                    break;
                case "Authorisation": // Sends AUTH message with username
                    outString = "AUTH " + System.getProperty("user.name");
                    state = "Ready";
                    break;

                case "Ready": //Sends redy to server
                    if (inString.equals("NONE")) { //quit if no message
                        outString = "QUIT";
                        state = "Quitting";
                    } else { 
                        outString = "REDY";
                        state = "Decision";
                    }
                    break;

                case "Decision": // DEcides what to do based on what has been sent
              
                    if ((sCount == 0 || listUpdated == false) && inString.contains("JOBN")) {
                        job = new Job(inString);
                        outString = "GETS Capable " + job.getJobNeeds();
                        state = "serverListPrep";
                    }

                    // If list is updated, schedule job
                    if (!serverList.isEmpty() && listUpdated == true) {
                        state = "JobScheduling";
                    }

                    // no more jobs, quit
                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                    }
                    break;

                case "serverListPrep": //Reads DATA message
                    sCount = getDataCount(inString); //data message gets read and saved
                    outString = "OK";
                    state = "serverListReading";
                    break;
                case "serverListReading": // serveLIst gets ready from Gets
                    readServerList(inString, serverList, socketIn, sCount); //
                    listUpdated = true; //changed to true after updating
                    scheduler = new Scheduler(serverList); //update server
                    outString = "OK";
                    state = "Ready";
                    break;

                case "JobScheduling":
                    // when a job gets sent, save the job
                    if (inString.contains("JOBN")) {
                        job = new Job(inString);
                    }

                    //no jobs, quit
                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                        break;
                    } else if (inString.contains("JCPL")) { 
                        if (outString.equals("REDY")) { // see if REDY was done before
                            break; 
                        }
                    } else {
                        outString = "SCHD " + job.jobID + " " + scheduler.schedule(algorithm); // schedule job
                        state = "Ready"; // in the ready state after scheduled
                    }
                    listUpdated = false; //after scheduled, make false to update
                    break;

                case "Quitting": //quit
                    outString = "QUIT";
                    state = "QUIT";
                    break;

                default:
                    System.out.println("Error has occurred"); 
                    quitCommunication(din, dout, s); // quite communication
            }

            // send server a message, unless REDY
            if (!(outString.equals("REDY") && state.equals("JobScheduling"))) {
                dout.write((outString + "\n").getBytes()); //send message, make sure server can communicate
            }
        } // End of loop

        if (state.equals("QUIT")) {
            quitCommunication(din, dout, s); //quit 
        }
    }
    
    // reads DATA message to see how many lines are needed
    private static int getDataCount(String inString) {
        String[] splitString = inString.split(" "); // split data message based on white spaces
        int count = Integer.parseInt(splitString[1]); // saves second value and changes it to int

        return count;
    }

    //Read message from server, from message
    private static void readServerList(String inString, ArrayList<Server> serverList, BufferedReader socketIn,
            int sCount) throws IOException {

        if (!serverList.isEmpty()) { //if list not empty, remove all
            serverList.removeAll(serverList);
        }
        Server temp; //used for server object
        for (int i = 0; i < sCount; i++) { //loop through servers
            if (i != 0) { //start at first
                inString = socketIn.readLine();
            }
            temp = new Server(inString); // add server message as an entry
            serverList.add(temp); // add server to arraylist
        }
    }

    

    // quite/remove communication
    private static void quitCommunication(DataInputStream din, DataOutputStream dout, Socket s) throws IOException {
        din.close(); 
        dout.close(); 
        s.close(); //
    }
}
