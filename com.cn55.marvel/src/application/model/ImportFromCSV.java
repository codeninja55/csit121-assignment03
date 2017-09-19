package application.model;
import java.io.BufferedReader;
/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV {
    void importData(BufferedReader reader);
    void closeFile();
}
