package application.model.card;

import application.model.dao.DataStoreDAO;
import application.model.dao.ExportToCSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class CardsExport implements ExportToCSV {
    public void exportData(DataStoreDAO dataStore, BufferedWriter writer) throws IOException {
        dataStore.getOrigCardsMap().values().parallelStream().forEach(card -> {
            try {
                writer.append(Instant.now().toString()).append(DEFAULT_SEPARATOR)
                        .append(card.getID()).append(DEFAULT_SEPARATOR)
                        .append(card.getCardType()).append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "").append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "").append(DEFAULT_SEPARATOR)
                        .append(Double.toString(card.getPoints())).append(DEFAULT_SEPARATOR)
                        .append((card instanceof AdvancedCard) ? Double.toString(((AdvancedCard) card).getBalance()) : "");
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
