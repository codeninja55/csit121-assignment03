package application.model.dao;

import application.model.card.CardsExport;
import application.model.card.CardsImport;
import application.model.category.CategoriesExport;
import application.model.category.CategoriesImport;
import application.model.purchase.PurchasesExport;
import application.model.purchase.PurchasesImport;
import application.view.custom.components.ProgressDialog;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileDAO extends DataDAO {
    private final Path CATEGORIES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/CategoriesStorage.csv");
    private final Path CARDS_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/CardsStorage.csv");
    private final Path PURCHASES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/PurchaseStorage.csv");

    private BufferedReader openReadFile(Path path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path.toString()));
    }

    public void importData() {
        /* Strategy Design Pattern - Implementation of writing and reading buried in concrete classes */
        ImportFromCSV categoriesImporter = new CategoriesImport();
        ImportFromCSV cardsImporter = new CardsImport();
        ImportFromCSV purchasesImporter = new PurchasesImport();

        SwingWorker<Void, Integer> importWorker = new SwingWorker<Void, Integer>() {
            protected Void doInBackground() throws Exception {
                try {
                    categoriesImporter.executeImport(FileDAO.this, openReadFile(CATEGORIES_LOG_PATH));
                    cardsImporter.executeImport(FileDAO.this, openReadFile(CARDS_LOG_PATH));
                    purchasesImporter.executeImport(FileDAO.this, openReadFile(PURCHASES_LOG_PATH));
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
                return null;
            }

            protected void process(List<Integer> counts) {
                // do something
            }

            protected void done() {
                notifyObservers();
            }
        };

        importWorker.execute();
    }
    private BufferedWriter openWriteFile(Path path) throws IOException {
        return new BufferedWriter(new FileWriter(path.toString()));
    }

    public void exportData(final ProgressDialog progressBar) {
        ExportToCSV categoriesExport = new CategoriesExport();
        ExportToCSV cardsExport = new CardsExport();
        ExportToCSV purchasesExport = new PurchasesExport();

        progressBar.setMaximum(100);
        progressBar.setVisible(true);

        SwingWorker<Void, Integer> exportWorker = new SwingWorker<Void, Integer>() {
            protected Void doInBackground() throws Exception {
                int progress = 0;
                // Calls the this.cancel() method which throws an InterruptedException event
                progressBar.getCancelBtn().addActionListener(e -> this.cancel(true));

                while(progress < 100) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // When the user presses cancel,
                        // the exception will break the loop and progress never reaches 100
                        break;
                    }
                    progressBar.setValue(progress += 10);
                }

                // This task is done so fast that u don't really notice.
                // While its working in the background, the GUI is not locked up
                if (progress == 100) {
                    try {
                        categoriesExport.exportData(FileDAO.this, openWriteFile(CATEGORIES_LOG_PATH));
                        cardsExport.exportData(FileDAO.this, openWriteFile(CARDS_LOG_PATH));
                        purchasesExport.exportData(FileDAO.this, openWriteFile(PURCHASES_LOG_PATH));
                    } catch (IOException e) {
                        System.err.println("IOException: " + e.getMessage());
                    }
                } else {
                    progressBar.setString("Cancelled. Data Not Saved.");
                }
                return null;
            }

            protected void done() { progressBar.setVisible(false); }
        };
        exportWorker.execute();
    }
}
