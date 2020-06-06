import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {

    // static variable single_instance of type Singleton
    private static WriteToFile single_instance = null;

    //writer to a file
    File file = new File("output.txt");
    private FileWriter myWriter = null;

    private WriteToFile() {
   

        if (!file.exists()) {

            try {
                myWriter = new FileWriter(file);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            try {
                myWriter = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writetoFile(String statement) throws IOException {

        myWriter.append(statement+"\n");
        System.out.println("Successfully wrote to the file.");
    }

        // static method to create instance of Singleton class
        public static WriteToFile getInstance() {
            if (single_instance == null)
                single_instance = new WriteToFile();
    
            return single_instance;
        }
}