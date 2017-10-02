package application.model.file_connectors;

import application.model.dao.DataStoreDAO;

import java.io.BufferedWriter;
import java.io.IOException;

public class CardsExport implements ExportToCSV {
    public void exportData(DataStoreDAO dataStore, BufferedWriter writer) throws IOException {
        dataStore.getOrigCardsMap().values().forEach(card -> {
            try {
                writer.append(card.toStringDelim());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
