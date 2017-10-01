package application.model.file_connectors;

import application.model.Generator;
import application.model.card.*;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportEmptyException;

import java.io.BufferedReader;
import java.io.IOException;

public class CardsImport implements ImportFromCSV {
    public void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws IOException, ImportEmptyException {
        if (reader.lines().count() == 0) {
            reader.close();
            throw new ImportEmptyException("CardsStorage.csv is empty");
        } else {
            reader.lines().forEach(line -> {
                String[] readLine = line.split(CSV.DEFAULT_SEPARATOR_STR);
                // [1] = ID | [2] = cardType | [3] = name | [4] = email | [5] = balance | [6] = points
                Card importCard = null;
                String cardID = readLine[1];

                if (CardType.AnonCard.equalsName(readLine[2]))
                    importCard = new AnonCard(cardID, Double.parseDouble(readLine[5]));
                else if (CardType.BasicCard.equalsName(readLine[2]))
                    importCard = new BasicCard(cardID, readLine[3], readLine[4], Double.parseDouble(readLine[5]),
                            Double.parseDouble(readLine[6]));
                else if (CardType.PremiumCard.equalsName(readLine[2]))
                    importCard = new PremiumCard(cardID, readLine[3], readLine[4], Double.parseDouble(readLine[5]),
                            Double.parseDouble(readLine[6]));

                Generator.updateCardIDCounter();
                assert importCard != null;
                dataStore.createCard(importCard);
            });
            reader.close();
        }

    }
}