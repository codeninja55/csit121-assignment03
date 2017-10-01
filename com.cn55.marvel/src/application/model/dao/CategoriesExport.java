package application.model.dao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class CategoriesExport implements ExportToCSV {
    public void exportData(DataStoreDAO dataStore, BufferedWriter writer) throws IOException {
        dataStore.getOrigCategoriesMap().values().forEach(c -> {
            try {
                writer.append(Instant.now().toString()).append(DEFAULT_SEPARATOR)
                        .append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR)
                        .append(c.getName()).append(DEFAULT_SEPARATOR)
                        .append(c.getDescription()).append(DEFAULT_SEPARATOR)
                        .append(Double.toString(c.getAmount()));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ExportToCSV.closeWriter(writer);
    }
}
