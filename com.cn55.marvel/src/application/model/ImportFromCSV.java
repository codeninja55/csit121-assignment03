package application.model;
import java.io.BufferedReader;
/* USING A STRATEGY DESIGN PATTERN */
@SuppressWarnings("unused")
public interface ImportFromCSV {
    void importData(BufferedReader reader);
    void closeFile();
}
