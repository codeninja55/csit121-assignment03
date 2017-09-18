package application.model;
import java.io.BufferedReader;
public interface ReadCSV {
    void read(BufferedReader reader);
    void closeFile();
}
