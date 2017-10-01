package application.model.file_connectors;

import application.model.dao.DataStoreDAO;

import java.io.BufferedReader;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV extends CSV {
    void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws IOException;
}
