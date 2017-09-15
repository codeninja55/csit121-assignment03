package application.model.DataStoreConnectors;

import application.model.CardModel.AnonCard;
import application.model.CardModel.BasicCard;
import application.model.CardModel.Card;
import application.model.CardModel.PremiumCard;
import application.model.CardModel.CardType;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardsReadImpl implements ReadCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    @Override
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
                    importCard = new AnonCard(readLine[0], readLine[1]);
                else if (readLine[1].equals(CardType.BasicCard.getName()))
                    importCard = new BasicCard(readLine[2], readLine[3], Double.parseDouble(readLine[4]));
                else if (readLine[1].equals(CardType.PremiumCard.getName()))
                    importCard = new PremiumCard(readLine[2], readLine[3], Double.parseDouble(readLine[4]));

                assert importCard != null;
                importCard.setPoints(Double.parseDouble(readLine[5]));

                Shop.getShopInstance().getDataStore().addCards(importCard);
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
