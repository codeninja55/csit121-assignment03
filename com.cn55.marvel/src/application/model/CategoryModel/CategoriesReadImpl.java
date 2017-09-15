package application.model.CategoryModel;

import application.model.ReadCSV;
import application.model.Generator;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CategoriesReadImpl implements ReadCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    @Override
    public void read() {
        Path categoriesStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CategoriesStorage.csv");
        String line;

        openFile(categoriesStoragePath);

        try {
            while((line = input.readLine()) != null) {
                String[] readLine = line.split(DEFAULT_SEPARATOR);

                // readLine[0] = id | [1] = name | [2] = description | [3] = amount

                Category importedCategory = new Category(Integer.parseInt(readLine[0]), readLine[1],
                                                        readLine[2], Double.parseDouble(readLine[3]));

                Generator.updateCategoryIDCounter();
                Shop.getShopInstance().getDataStore().createCategory(importedCategory);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        closeFile();
    }

    @Override
    public void openFile(Path path) {
        try {
            input = new BufferedReader(new FileReader(path.toString()));
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    @Override
    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
