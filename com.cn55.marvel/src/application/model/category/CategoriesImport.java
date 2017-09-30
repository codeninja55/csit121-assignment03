package application.model.category;

import application.model.dao.DataDAO;
import application.model.Generator;
import application.model.dao.ImportFromCSV;

import java.io.BufferedReader;
import java.io.IOException;

public class CategoriesImport implements ImportFromCSV {
    private static final String DEFAULT_SEPARATOR = ",";
    private BufferedReader input;

    public void executeImport(DataDAO db, BufferedReader reader) throws IOException {
        this.input = reader;
        String line;

        while((line = input.readLine()) != null) {
            String[] readLine = line.split(DEFAULT_SEPARATOR);
            // readLine[0] = id | [1] = name | [2] = description | [3] = amount
            Category importedCategory = new Category(Integer.parseInt(readLine[1]), readLine[2],
                    readLine[3], Double.parseDouble(readLine[4]));

            Generator.updateCategoryIDCounter();
            db.createCategory(importedCategory);
        }
        closeFile();
    }

    private void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
