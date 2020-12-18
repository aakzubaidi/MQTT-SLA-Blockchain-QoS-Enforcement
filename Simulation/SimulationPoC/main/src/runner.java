import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

public class runner extends TimerTask {

    int countOfRemovedIncidents = 0;

    @Override
    public void run() {

        System.out.println("Task Timer on Fixed Rate");
        System.out.println("I am thread whose name is: "+ Thread.currentThread().getName() + " with ID: "+ Thread.currentThread().getId());
        WriteToFile writeToFile = WriteToFile.getInstance();

        // instantiating Incidents class. We use conuccurent hashmap to acquire
        // read/write lock
        Incidents incidents = Incidents.getInstance();
        if (!incidents.incidentsLocalStore.isEmpty()) {
            Iterator it = incidents.incidentsLocalStore.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println("Removing " + pair.getKey() + " = " + pair.getValue());
                try {
                    writeToFile.writetoFile("Removing " + pair.getKey() + " = " + pair.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                it.remove(); // avoids a ConcurrentModificationException
                countOfRemovedIncidents++;
            }
            System.out.println("<<<<<<<<< End of fabric sdk task >>>>>>>>>");
            System.out.println("Count of removed incidents by this thread: " + countOfRemovedIncidents);

            try {
                incidents.printLocalIncidentStore();
                writeToFile.writetoFile("<<<<<<<<< End of fabric sdk task >>>>>>>>>");
                writeToFile.writetoFile("From runner: Count of removed incidents by this thread: " + countOfRemovedIncidents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("No incidents to be processed");
        }

        countOfRemovedIncidents = 0;
    }

}