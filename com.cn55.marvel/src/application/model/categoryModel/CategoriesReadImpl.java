package application.model.categoryModel;

import application.model.Generator;
import application.model.ReadCSV;
import application.model.Shop;

import java.io.BufferedReader;
import java.io.IOException;

public class CategoriesReadImpl implements ReadCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    public void read(BufferedReader reader) {
        this.input = reader;
        String line;

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

    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
