package application.model.DataStoreConnectors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CategoriesReadImpl implements ReadCSV {
    private BufferedReader input;

    @Override
    public void read() {
        Path categoriesStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CategoriesStorage.csv");
        String line;

        openFile(categoriesStoragePath);

    }

    @Override
    public void openFile(Path path) {
        try {
            input = new BufferedReader(new FileReader(path.toString()));
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    @Override
    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
