package application.model.category;

import application.model.Generator;
import application.model.dao.DataStoreDAO;
import application.model.dao.ImportFromCSV;

import java.io.BufferedReader;
import java.io.IOException;

public class CategoriesImport implements ImportFromCSV {
    private static final String DEFAULT_SEPARATOR = ",";

    public void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws IOException {
        String line;

        while((line = reader.readLine()) != null) {
            String[] readLine = line.split(DEFAULT_SEPARATOR);
            // readLine[0] = id | [1] = name | [2] = description | [3] = amount
            Category importedCategory = new Category(Integer.parseInt(readLine[1]), readLine[2],
                    readLine[3], Double.parseDouble(readLine[4]));

            Generator.updateCategoryIDCounter();
            dataStore.createCategory(importedCategory);
        }
        reader.close();
    }
}
