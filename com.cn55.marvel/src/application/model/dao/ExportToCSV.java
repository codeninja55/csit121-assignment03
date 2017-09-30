package application.model.dao;

import java.io.BufferedWriter;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ExportToCSV {
    void exportData(DataDAO db, BufferedWriter writer) throws IOException;
}
