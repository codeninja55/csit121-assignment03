package cn55.model.DataStoreConnectors;

import cn55.model.CardModel.AnonCard;
import cn55.model.CardModel.BasicCard;
import cn55.model.CardModel.Card;
import cn55.model.CardModel.PremiumCard;
import cn55.model.CardType;
import cn55.model.Shop;

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
        String line;
        openFile(cardsStoragePath);

        try {
            while((line = input.readLine()) != null) {
                String[] readLine = line.split(DEFAULT_SEPARATOR);

                System.out.println(readLine[0]); // ID
                System.out.println(readLine[1]); // cardType
                System.out.println(readLine[2]); // name
                System.out.println(readLine[3]); // email
                System.out.println(readLine[4]); // balance
                System.out.println(readLine[5]); // points

                Card importCard = null;

                if (readLine[1].equals(CardType.AnonCard.getName())) {
                    importCard = new AnonCard(readLine[0], readLine[1], Double.parseDouble(readLine[5]));
                } else if (readLine[1].equals(CardType.BasicCard.getName())) {
                    importCard = new BasicCard(readLine[2], readLine[3], Double.parseDouble(readLine[4]));
                } else if (readLine[1].equals(CardType.PremiumCard.getName())) {
                    importCard = new PremiumCard(readLine[2], readLine[3], Double.parseDouble(readLine[4]));
                }

                Shop.getShopInstance().getDataStore().addCards(importCard);

                System.out.println(importCard);
                System.out.println(Shop.getShopInstance().getDataStore().getCards());
                /*for (String s : readLine) {
                    System.out.println(s);
                }*/
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
