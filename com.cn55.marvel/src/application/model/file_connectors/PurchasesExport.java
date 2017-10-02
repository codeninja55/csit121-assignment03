package application.model.file_connectors;

import application.model.dao.DataStoreDAO;

import java.io.BufferedWriter;
import java.io.IOException;

public class PurchasesExport implements ExportToCSV {
    public void exportData(DataStoreDAO db, BufferedWriter writer) throws IOException {
        db.getOrigPurchasesMap().values().forEach(purchase -> {
            try {
                writer.append(purchase.toStringDelim());
                writer.newLine();
            } catch (IOException e) {
                System.err.println("IOException" + e.getMessage());
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
