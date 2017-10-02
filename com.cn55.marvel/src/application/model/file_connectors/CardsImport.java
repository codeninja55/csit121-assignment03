package application.model.file_connectors;

import application.model.Generator;
import application.model.card.*;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CardsImport extends Throwable implements ImportFromCSV {
    public void executeImport(DataStoreDAO dataStore, Path file) throws IOException, ImportException {
        if (Files.lines(file).count() == 0) {
            throw new ImportException("CardsStorage.csv is empty", CardsImport.this);
        } else {
            Files.newBufferedReader(file).lines().forEach(line -> {
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
        }

    }
}
