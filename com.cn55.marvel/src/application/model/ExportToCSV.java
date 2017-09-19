package application.model;

import java.io.BufferedWriter;
import java.util.Collection;

/* USING A STRATEGY DESIGN PATTERN */
public interface ExportToCSV {
    void exportData(BufferedWriter writer);
    void closeFile();
}
