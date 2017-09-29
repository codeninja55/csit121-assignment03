package application.model;

import java.io.BufferedReader;
import java.io.IOException;

/* USING A STRATEGY DESIGN PATTERN */
public interface ImportFromCSV {
    void executeImport(DataDAO db, BufferedReader reader) throws IOException;
}
