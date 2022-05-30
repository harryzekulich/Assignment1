public class Job {
    String submitTime, jobID, estRun, cCount, mem, store;

    //splits string into assigned sections
    public Job(String jobString) {
        String[] splitString = jobString.split(" ");

        submitTime = splitString[1]; //timing of job submission 
        jobID = splitString[2]; // job id
        estRun = splitString[3]; // predicted runtime
        cCount = splitString[4]; //#of cpus
        mem = splitString[5]; // RAM space
        store = splitString[6]; // Disk Storage
    }

    //returns string for what the job needs
    public String getJobNeeds() {
        return cCount + " " + mem + " " + store;
    }
