package application.model.DataStoreConnectors;

import application.model.CardModel.AdvancedCard;
import application.model.CardModel.Card;
import application.model.Shop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardsWriteOut implements WriteCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    @Override
    public void writeOut() {
        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CardsStorage.csv");
        openFile(cardsStoragePath);

        try{
            for (Card card : Shop.getShopInstance().getDataStore().getCards()) {
                output.append(card.getID())
                        .append(DEFAULT_SEPARATOR)
                        .append(card.getCardType())
                        .append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "")
                        .append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "")
                        .append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? Double.toString(((AdvancedCard) card).getBalance()) : "")
                        .append(DEFAULT_SEPARATOR)
                        .append(Double.toString(card.getPoints()));
                output.newLine();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
        closeFile();
    }

    @Override
    public void openFile(Path path) {
        try {
            output = new BufferedWriter(new FileWriter(path.toString()));
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    @Override
    public void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
