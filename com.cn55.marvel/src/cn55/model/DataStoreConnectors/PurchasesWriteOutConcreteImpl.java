package cn55.model.DataStoreConnectors;

import cn55.model.Category;
import cn55.model.DataStoreModel;
import cn55.model.Purchase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PurchasesWriteOutConcreteImpl implements WriteCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    @Override
    public void writeOut() {
        String purchaseHeaders = "purchaseTime,receiptID,cardType,cardID,categories[]";
        Path purchaseStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/purchaseStorage.csv");
        openFile(purchaseStoragePath);

        try{
            output.append(purchaseHeaders);
            output.newLine();

            for (Purchase p : DataStoreModel.getDataStoreInstance().getPurchases()) {
                int lastLine = 1;
                output.append(p.getPurchaseTime().toString()).append(DEFAULT_SEPARATOR);
                output.append(Integer.toString(p.getReceiptID())).append(DEFAULT_SEPARATOR);
                output.append(p.getCardType()).append(DEFAULT_SEPARATOR);
                output.append(p.getCardID()).append(DEFAULT_SEPARATOR);
                output.append("{");
                for (Category c : p.getCategories().values()) {
                    output.append("[").append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR);
                    output.append(c.getName()).append(DEFAULT_SEPARATOR);
                    output.append(c.getDescription()).append(DEFAULT_SEPARATOR);
                    output.append(Double.toString(c.getAmount())).append("]");

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
