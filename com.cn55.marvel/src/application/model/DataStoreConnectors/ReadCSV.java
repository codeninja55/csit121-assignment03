package application.model.DataStoreConnectors;

import java.nio.file.Path;

public interface ReadCSV {
    void read();
    void openFile(Path path);
    void closeFile();
}
