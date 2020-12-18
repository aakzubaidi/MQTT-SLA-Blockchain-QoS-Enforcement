import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Java program implementing Singleton class 
// with getInstance() method 
class Incidents {
    // static variable single_instance of type Singleton
    private static Incidents single_instance = null;

    // store for incidents (, value)
    public Map<String, List<String>> incidentsLocalStore = null;
    WriteToFile writeToFile = null;

    // private constructor restricted to this class itself
    private Incidents() {
        incidentsLocalStore = new ConcurrentHashMap<String, List<String>>();
        // writer to a file
        writeToFile = WriteToFile.getInstance();
    }

    // static method to create instance of Singleton class
    public static Incidents getInstance() {
        if (single_instance == null)
            single_instance = new Incidents();

        return single_instance;
    }

    public void printLocalIncidentStore() throws IOException {
        System.out.println("<<< The current local incidents store consists of: >>> " + incidentsLocalStore);
        writeToFile.writetoFile("The current local incidents store consists of: "+ incidentsLocalStore);
    }
}