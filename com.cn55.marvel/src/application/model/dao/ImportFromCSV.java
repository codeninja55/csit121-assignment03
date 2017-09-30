package application.model.dao;

import java.io.BufferedReader;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV {
    void executeImport(DataStoreDAO db, BufferedReader reader) throws IOException;
}
