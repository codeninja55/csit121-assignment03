package application.model;
import java.io.BufferedWriter;
/* USING A STRATEGY DESIGN PATTERN */
public interface ExportToCSV {
    void exportData(BufferedWriter writer);
    @SuppressWarnings("unused")
    void closeFile();
}
