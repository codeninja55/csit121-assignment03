package application.model.category;

import application.model.DataDAO;
import application.model.ExportToCSV;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Instant;

public class CategoriesExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(DataDAO db, BufferedWriter writer) throws IOException {
        this.output = writer;

        for (Category c : db.getAllCategories().values()) {
            output.append(Instant.now().toString()).append(DEFAULT_SEPARATOR)
                    .append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR)
                    .append(c.getName()).append(DEFAULT_SEPARATOR)
                    .append(c.getDescription()).append(DEFAULT_SEPARATOR)
                    .append(Double.toString(c.getAmount()));
            output.newLine();
        }

        closeFile();
    }

    private void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
