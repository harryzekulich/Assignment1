public class Server {
    String sType, sID, state, cSTime, cCount, mem, disk;
    String wJobs, runJobs;

    // Spliiting sections up into assigned parts
    public Server(String serverDetail) {
        String[] splitString = serverDetail.split(" ");

        sType = splitString[0]; // type (size/name)
        sID = splitString[1]; // Server identifyer
        state = splitString[2]; // server current action
        cSTime = splitString[3]; // last item of currently running server
        cCount = splitString[4]; // number of cpu cores
        mem = splitString[5]; // amount of RAM
        disk = splitString[6]; // disk storage
        wJobs = splitString[7]; // number of qeued jobs
        runJobs = splitString[8]; // amount of running jobs
        
        
    }
     // waiting jobs as an integer for comparison
     public int getWaitingJobs() {
        return Integer.parseInt(wJobs);
    }

    // string for SCHD runnable
    public String getServerTypeID() {
        return sType + " " + sID;
    }

    // core count being returned as a number to be used
    public int getCoreCount() {
        return Integer.parseInt(cCount);
    }


}
