package application.model.cardModel;

import application.model.Shop;
import application.model.ExportToCSV;

import java.io.BufferedWriter;
import java.io.IOException;

public class CardsExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(BufferedWriter writer) {
        this.output = writer;
        try{
            for (Card card : Shop.getShopInstance().getDataStore().getAllCards().values()) {
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
    public void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
