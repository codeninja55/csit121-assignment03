package application.model.CardModel;

import application.model.DataStoreConnectors.ReadCSV;
import application.model.Generator;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardsReadImpl implements ReadCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    public void read() {
        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CardsStorage.csv");
        String line;
        openFile(cardsStoragePath);

        try {
            while((line = input.readLine()) != null) {
                String[] readLine = line.split(DEFAULT_SEPARATOR);

                // readLine[0] = ID | [1] = cardType | [2] = name | [3] = email | readLine[4] = balance | [5] = points

                Card importCard = null;

                if (readLine[1].equals(CardType.AnonCard.getName()))
                    importCard = new AnonCard(readLine[0], Double.parseDouble(readLine[5]));
                else if (readLine[1].equals(CardType.BasicCard.getName()))
                    importCard = new BasicCard(readLine[0], readLine[2], readLine[3],
                            Double.parseDouble(readLine[4]), Double.parseDouble(readLine[5]));
                else if (readLine[1].equals(CardType.PremiumCard.getName()))
                    importCard = new PremiumCard(readLine[0], readLine[2], readLine[3],
                            Double.parseDouble(readLine[4]), Double.parseDouble(readLine[5]));

                assert importCard != null;
                importCard.setPoints(Double.parseDouble(readLine[5]));

                Generator.updateCardIDCounter();
                Shop.getShopInstance().getDataStore().addCards(importCard);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    public void openFile(Path path) {
        try {
            input = new BufferedReader(new FileReader(path.toString()));
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
