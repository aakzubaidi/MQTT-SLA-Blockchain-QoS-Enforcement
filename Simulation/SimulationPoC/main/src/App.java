import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class App {

    // settings for monitoring agent
    static int intialDelay = 5;
    static int period = 5;
    // collected data from monitoring agent. After each incident report, these
    // countr are set to zero again
    static int validCount = 0;
    static int failureCount = 0;
    // set parameters of the simulation
    // range of which the count of failures can be.
    static int minRandom = 0;
    static int maxRandom = 10;
    // factor of increasing the valid requests.
    static int factorofValidRequests = 1000;

    // counters for checking the occurance of incidents and normal operation
    static int incidentCount = 0;
    static int nortmalCount = 0;
    static int numberofIncidents = 10;

    public static void main(final String[] args) throws Exception {

        // instantiating Incidents class. We use conuccurent hashmap to acquire
        // read/write lock
        Incidents incidents = Incidents.getInstance();

        // schedule monitoring agent to check for incidents
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(new runner(), intialDelay, period, TimeUnit.SECONDS);
        // writer to a file>
        WriteToFile writeToFile = WriteToFile.getInstance();

        
        for (int i = 1; i <= numberofIncidents; i++) {

            writeToFile.writetoFile("#############################\n"+ "Iteration "+ i+ ": try to write to a file from main" );
            failureCount = ThreadLocalRandom.current().nextInt(minRandom, maxRandom);
            validCount += i * factorofValidRequests;


            // check if there is incidents to report
            if (failureCount > 0) {

                //compose an incident
                List<String> incidentValues = new ArrayList<String>();
                // random Failures
                incidentValues.add(Integer.toString(failureCount));
                // random Failures
                incidentValues.add(Integer.toString(validCount));

                // we add this (record of an incident) into a concurrent hashmap
                // to acquire read/write lock
                // the key is distinguished by nanoseconds to prevent updating the same key
                incidents.incidentsLocalStore.put("#" + System.nanoTime(), incidentValues);
                incidents.printLocalIncidentStore();

                // after reporting the failures and valid requests, the counts are reset to
                // zeros
                failureCount = 0;
                //validCount = 0;
                // increase number of repoerted incidents
                incidentCount++;
                System.out.println("The incident has been reported\n");
                writeToFile.writetoFile("The incident has been reported" );
                System.out.println(
                        ": Incident count is: " + incidentCount + ", while the normal count is: " + nortmalCount);
                writeToFile.writetoFile("Incident count is: " + incidentCount + ", while the normal operations count is: " + nortmalCount);
            } else {
                // increase number of normal operations (no incidents to report)
                nortmalCount++;
                System.out.println("there is no incident to report\n");
                writeToFile.writetoFile("there is no incident to report" );

                System.out.println("Incident count is: " + incidentCount + ", while the normal operations count is: "
                        + nortmalCount);
                writeToFile.writetoFile("Incident count is: " + incidentCount + ", while the normal operations count is: "
                + nortmalCount );
            }

        }
        System.out.println("count of submitted incidents: " + incidentCount);
        incidents.printLocalIncidentStore();
        System.out.println("<<<<<<<<< End of monitoring task >>>>>>>>>");

    }

}
