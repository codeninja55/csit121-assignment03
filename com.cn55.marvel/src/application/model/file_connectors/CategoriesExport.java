package application.model.file_connectors;

import application.model.dao.DataStoreDAO;

import java.io.BufferedWriter;
import java.io.IOException;

public class CategoriesExport implements ExportToCSV {
    public void exportData(DataStoreDAO dataStore, BufferedWriter writer) throws IOException {
        dataStore.getOrigCategoriesMap().values().forEach(category -> {
            try {
                writer.append(category.toStringDelim());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
