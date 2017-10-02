package application.model.file_connectors;

import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportException;

import java.io.IOException;
import java.nio.file.Path;

/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV extends CSV {
    void executeImport(DataStoreDAO dataStore, Path file) throws ImportException, IOException;
}
