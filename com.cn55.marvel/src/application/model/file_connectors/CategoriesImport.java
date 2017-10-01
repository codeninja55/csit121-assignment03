package application.model.file_connectors;

import application.model.Generator;
import application.model.category.Category;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportEmptyException;

import java.io.BufferedReader;
import java.io.IOException;

public class CategoriesImport implements ImportFromCSV {
    public void executeImport(DataStoreDAO dataStore, BufferedReader reader) throws ImportEmptyException, IOException {
        if (reader.lines().count() == 0) {
            reader.close();
            throw new ImportEmptyException("CategoriesStorage.csv is empty");
        } else {
            reader.lines().forEach(line -> {
                String[] readLine = line.split(CSV.DEFAULT_SEPARATOR_STR);
                // readLine[0] = id | [1] = name | [2] = description | [3] = amount
                Category importedCategory = new Category(Integer.parseInt(readLine[1]), readLine[2],
                        readLine[3], Double.parseDouble(readLine[4]));

                Generator.updateCategoryIDCounter();
                dataStore.createCategory(importedCategory);
            });
            reader.close();
        }
    }
}
