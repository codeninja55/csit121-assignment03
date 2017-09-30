package application.model.purchase;

import application.model.dao.DataDAO;
import application.model.dao.ExportToCSV;
import application.model.card.CardType;
import application.model.category.Category;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class PurchasesExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(DataDAO db, BufferedWriter writer) throws IOException {
        this.output = writer;

        for (Purchase p : db.getAllPurchases().values()) {
            int lastLine = 1;
            output.append(Instant.now().toString()).append(DEFAULT_SEPARATOR)
                    .append(p.getPurchaseTimeStr()).append(DEFAULT_SEPARATOR)
                    .append(Integer.toString(p.getReceiptID())).append(DEFAULT_SEPARATOR)
                    .append(p.getCardType()).append(DEFAULT_SEPARATOR);

            output.append((p.getCardType().equals(CardType.Cash.name)) ? "" : p.getCardID());

            output.append(DEFAULT_SEPARATOR).append("{");
            for (Category c : p.getCategories().values()) {
                output.append("[")
                        .append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR)
                        .append(c.getName()).append(DEFAULT_SEPARATOR)
                        .append(c.getDescription()).append(DEFAULT_SEPARATOR)
                        .append(Double.toString(c.getAmount())).append("]");

                if (lastLine != p.getCategories().size()) {
                    output.append(DEFAULT_SEPARATOR);
                    lastLine++;
                }
            }
            output.append("}");
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
