package cn55.model.DataStoreConnectors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardsReadConcreteImpl implements ReadCSV {

    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    @Override
    public void read() {

        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/cardsStorage.csv");
        openFile(cardsStoragePath);

        try {

            while(("" = input.readLine()) != null) {
                String[] line = "".split(DEFAULT_SEPARATOR);


            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

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
