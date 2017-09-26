package application.model.card;

import application.model.Generator;
import application.model.ImportFromCSV;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.IOException;

public class CardsImport implements ImportFromCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    public void importData(BufferedReader reader) {
        this.input = reader;
        String line;

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
                Shop.getShopInstance().getDataStore().createCard(importCard);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        closeFile();
    }

    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
