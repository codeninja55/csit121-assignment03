package cn55.model.DataStoreConnectors;

import cn55.model.CategoryModel.Category;
import cn55.model.Shop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CategoriesWriteOut implements WriteCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    @Override
    public void writeOut() {
        Path categoriesStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/categoriesStorage.csv");
        openFile(categoriesStoragePath);

        try{
            for (Category c : Shop.getShopInstance().getDataStore().getCategories()) {
                output.append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR);
                output.append(c.getName()).append(DEFAULT_SEPARATOR);
                output.append(c.getDescription()).append(DEFAULT_SEPARATOR);
                output.append(Double.toString(c.getAmount()));
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
