package application.model.DataStoreWriters;

import application.model.CardModel.CardType;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;
import application.model.Shop;
import application.model.WriteCSV;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PurchasesWriteOut implements WriteCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    @Override
    public void writeOut() {
        Path purchaseStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/PurchaseStorage.csv");
        openFile(purchaseStoragePath);

        try{
            for (Purchase p : Shop.getShopInstance().getDataStore().getAllPurchases().values()) {
                int lastLine = 1;
                output.append(p.getPurchaseTime())
                        .append(DEFAULT_SEPARATOR)
                        .append(Integer.toString(p.getReceiptID()))
                        .append(DEFAULT_SEPARATOR)
                        .append(p.getCardType())
                        .append(DEFAULT_SEPARATOR);

                if (p.getCardType().equals(CardType.Cash.getName())) output.append("");
                else output.append(p.getCardID());

                output.append(DEFAULT_SEPARATOR).append("{");
                for (Category c : p.getCategories().values()) {
                    output.append("[")
                            .append(Integer.toString(c.getId()))
                            .append(DEFAULT_SEPARATOR)
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
