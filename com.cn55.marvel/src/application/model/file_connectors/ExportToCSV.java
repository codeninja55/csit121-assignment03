package application.model.file_connectors;

import application.model.dao.DataStoreDAO;

import java.io.BufferedWriter;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ExportToCSV extends CSV {
    void exportData(DataStoreDAO db, BufferedWriter writer) throws IOException;
    static void closeWriter(BufferedWriter writer) throws IOException {
        writer.flush();
        writer.close();
    }
}
