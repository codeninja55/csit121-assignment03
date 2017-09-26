package application.model.purchase;

import application.model.Shop;
import application.model.ExportToCSV;
import application.model.card.CardType;
import application.model.category.Category;

import java.io.BufferedWriter;
import java.io.IOException;

public class PurchasesExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(BufferedWriter writer) {
        this.output = writer;
        try{
            for (Purchase p : Shop.getShopInstance().getDataStore().getAllPurchases().values()) {
                int lastLine = 1;
                output.append(p.getPurchaseTimeStr()).append(DEFAULT_SEPARATOR)
                        .append(Integer.toString(p.getReceiptID())).append(DEFAULT_SEPARATOR)
                        .append(p.getCardType()).append(DEFAULT_SEPARATOR);

                if (p.getCardType().equals(CardType.Cash.getName())) output.append("");
                else output.append(p.getCardID());

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
