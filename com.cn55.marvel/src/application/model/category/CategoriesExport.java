package application.model.category;

import application.model.ExportToCSV;
import application.model.Shop;

import java.io.BufferedWriter;
import java.io.IOException;

public class CategoriesExport implements ExportToCSV {
    private static final char DEFAULT_SEPARATOR = ',';
    private BufferedWriter output;

    public void exportData(BufferedWriter writer) {
        this.output = writer;
//        ArrayList<Category> categoriesList = categories.stream().collect(Collectors.toList());
        try{
            for (Category c : Shop.getShopInstance().getDataStore().getAllCategories().values()) {
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
    public void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
