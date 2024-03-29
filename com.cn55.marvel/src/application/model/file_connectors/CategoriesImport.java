package application.model.file_connectors;

import application.model.Generator;
import application.model.category.Category;
import application.model.dao.DataStoreDAO;
import application.model.exceptions.ImportException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CategoriesImport extends Throwable implements ImportFromCSV {
    public void executeImport(DataStoreDAO dataStore, Path file) throws ImportException, IOException {
        if (Files.lines(Paths.get("com.cn55.marvel/src/persistent_data/CategoriesStorage.csv")).count() == 0) {
            dataStore.createCategory(new Category("Other", "Default category"));
            throw new ImportException("CategoriesStorage.csv is empty", this);
        } else {
            Files.newBufferedReader(file).lines().forEach(line -> {
                String[] readLine = line.split(CSV.DEFAULT_SEPARATOR_STR);
                // readLine[0] = id | [1] = name | [2] = description | [3] = amount
                Category importedCategory = new Category(Integer.parseInt(readLine[1]), readLine[2],
                        readLine[3], Double.parseDouble(readLine[4]));

                Generator.updateCategoryIDCounter();
                dataStore.createCategory(importedCategory);
            });
        }
    }
}
