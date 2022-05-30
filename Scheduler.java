import java.util.ArrayList;

public class Scheduler {
    ArrayList<Server> serverList;

    public Scheduler(ArrayList<Server> serverList) {
        this.serverList = serverList;
    }

  //choose a scheduler algorithm stage 2 = NEW
    
    public String schedule(String algorithm) {
        switch (algorithm) {
            case "ATL":
                return allToLargest();
            case "NEW":
                return newAlgorithm();
            default: 
                System.out.println("Please input a valid algorithm");
                System.exit(1); // Exit program
                return null;
        }
    }
    
     // finds servers with least amount of cpus
    private Server getSmallestServer(ArrayList<Server> serverList) {
        Server smallest = serverList.get(0); //assigned as first in list
        Server test;

        // again checks for compariosn
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); //test becomes 1 infront
            if (smallest.getCoreCount() > test.getCoreCount()) { //sees if server has less cpus than previous
                smallest = test; //if true, that becomes smallest
            }
        }
        return smallest; //returns smallest server
    }
    
    // finds servers with most amount of cores
    private Server getLargestServer(ArrayList<Server> serverList) {
        Server largest = serverList.get(0); //assigns largest server as first in list
        Server test;

        //check items for comparison
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); //test is 1 infront of loop for use 
            if (largest.getCoreCount() < test.getCoreCount()) { // checks to see if server has more cpus
                largest = test; //changes to largest if true
            }
        }
        return largest; //largest server gets returned
    }

    // scheduler finds largest servers for jobs
    public String allToLargest() {
        Server largest = getLargestServer(serverList); //saves largest server from server list

        return largest.getServerTypeID(); 
    }

   //the new algo which selects servers with least amount of jobs
    public String newAlgorithm() {
        Server best = shortestQueue(serverList); // Finds the server with the least amount of jobs

        return best.getServerTypeID();
    }

   


    // looks for server with smallest number of jobs
    private Server shortestQueue(ArrayList<Server> serverList) {
        Server best = getSmallestServer(serverList); // the best server is assigned as 1 in list
        Server test;

        //checks for comparison
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); //set server to check 1 infront
            if (best.getWaitingJobs() > test.getWaitingJobs()) { // sees if new server has less jobs
                best = test; //if true change it
            }
        }
        return best; // Returns best server
    }

}
