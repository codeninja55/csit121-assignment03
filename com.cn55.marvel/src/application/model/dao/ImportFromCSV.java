package application.model.dao;

import java.io.BufferedReader;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV extends CSV {
    void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws IOException;
}
