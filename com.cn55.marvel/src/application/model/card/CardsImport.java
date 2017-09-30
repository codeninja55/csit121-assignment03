package application.model.card;

import application.model.dao.DataDAO;
import application.model.Generator;
import application.model.dao.ImportFromCSV;

import java.io.BufferedReader;
import java.io.IOException;

public class CardsImport implements ImportFromCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    public void executeImport(DataDAO db, BufferedReader reader) throws IOException {
        this.input = reader;
        String line;


        while((line = input.readLine()) != null) {
            String[] readLine = line.split(DEFAULT_SEPARATOR);
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
            db.createCard(importCard);
        }
        closeFile();
    }

    private void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
