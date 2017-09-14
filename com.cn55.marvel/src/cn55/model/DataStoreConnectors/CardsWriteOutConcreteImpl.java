package cn55.model.DataStoreConnectors;

import cn55.model.CardModel.AdvancedCard;
import cn55.model.CardModel.Card;
import cn55.model.DataStoreModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardsWriteOutConcreteImpl implements WriteCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    @Override
    public void writeOut() {
        String cardsHeader = "id,cardType,name,email,balance,points";
        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/cardsStorage.csv");
        openFile(cardsStoragePath);

        try{
            output.append(cardsHeader);
            output.newLine();

            for (Card card : DataStoreModel.getDataStoreInstance().getCards()) {
                output.append(card.getID()).append(DEFAULT_SEPARATOR);
                output.append(card.getCardType()).append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "").append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "").append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? Double.toString(((AdvancedCard) card).getBalance()) : "").append(DEFAULT_SEPARATOR);
                output.append(Double.toString(card.getPoints()));
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
