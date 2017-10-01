package application.model.purchase;

import application.model.card.CardType;
import application.model.category.Category;
import application.model.dao.CSV;
import application.model.dao.DataStoreDAO;
import application.model.dao.ExportToCSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class PurchasesExport implements ExportToCSV {
    public void exportData(DataStoreDAO db, BufferedWriter writer) throws IOException {
        db.getOrigPurchasesMap().values().parallelStream().forEach(p -> {
            try {
                int lastLine = 1;
                writer.append(Instant.now().toString()).append(CSV.DEFAULT_SEPARATOR)
                        .append(p.getPurchaseTimeStr()).append(CSV.DEFAULT_SEPARATOR)
                        .append(Integer.toString(p.getReceiptID())).append(CSV.DEFAULT_SEPARATOR)
                        .append(p.getCardType()).append(CSV.DEFAULT_SEPARATOR);

                writer.append((p.getCardType().equals(CardType.Cash.name)) ? "" : p.getCardID());

                writer.append(ExportToCSV.DEFAULT_SEPARATOR).append("{");
                for (Category c : p.getCategories().values()) {
                    writer.append("[")
                            .append(Integer.toString(c.getId())).append(CSV.DEFAULT_SEPARATOR)
                            .append(c.getName()).append(CSV.DEFAULT_SEPARATOR)
                            .append(c.getDescription()).append(CSV.DEFAULT_SEPARATOR)
                            .append(Double.toString(c.getAmount())).append("]");

                    if (lastLine != p.getCategories().size()) {
                        writer.append(CSV.DEFAULT_SEPARATOR);
                        lastLine++;
                    }
                }

                writer.append("}");
                writer.newLine();
            } catch (IOException e) {
                System.err.println("IOException" + e.getMessage());
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
