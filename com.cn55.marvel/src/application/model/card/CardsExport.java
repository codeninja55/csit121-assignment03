package application.model.card;

import application.model.dao.DataDAO;
import application.model.dao.ExportToCSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class CardsExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(DataDAO db, BufferedWriter writer) throws IOException {
        this.output = writer;

        for (Card card : db.getAllCards().values()) {
            output.append(Instant.now().toString()).append(DEFAULT_SEPARATOR)
                    .append(card.getID()).append(DEFAULT_SEPARATOR)
                    .append(card.getCardType()).append(DEFAULT_SEPARATOR)
                    .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "").append(DEFAULT_SEPARATOR)
                    .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "").append(DEFAULT_SEPARATOR)
                    .append(Double.toString(card.getPoints())).append(DEFAULT_SEPARATOR)
                    .append((card instanceof AdvancedCard) ? Double.toString(((AdvancedCard) card).getBalance()) : "");
            output.newLine();
        }

        closeFile();
    }

    private void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
