package application.model.DataStoreConnectors;

import java.nio.file.Path;

/* USING A STRATEGY DESIGN PATTERN */
public interface WriteCSV {
    void writeOut();
    void openFile(Path path);
    void closeFile();
}
