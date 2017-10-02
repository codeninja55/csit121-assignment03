package application.model.dao;

import application.model.Generator;
import application.model.card.Card;
import application.model.category.Category;
import application.model.exceptions.ImportException;
import application.model.file_connectors.*;
import application.model.purchase.Purchase;
import application.view.custom.components.ProgressDialog;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/* Data Access Object (DAO) Implementation Layer */
public class DataStoreDAO implements DataObservable, CardsDAO, PurchaseDAO, CategoryDAO {
    private final Path CATEGORIES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/CategoriesStorage.csv");
    private final Path CARDS_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/CardsStorage.csv");
    private final Path PURCHASES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistent_data/PurchaseStorage.csv");
    private final ArrayList<DataObserver> dataObservers;
    private final HashMap<String,Card> cardsMap;
    private final HashMap<Integer,Purchase> purchasesMap;
    private final HashMap<Integer,Category> categoriesMap;
    private static DataStoreDAO ds;
    private LocalDateTime firstPurchaseDate;
    private LocalDateTime lastPurchaseDate;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    private DataStoreDAO() {
        this.cardsMap = new HashMap<>();
        this.purchasesMap = new HashMap<>();
        this.categoriesMap = new HashMap<>();
        this.dataObservers = new ArrayList<>();
    }

    public static synchronized DataStoreDAO getDataStoreInstance() {
        if (ds == null) ds = new DataStoreDAO();
        return ds;
    }

    /*============================== FILE CONNECTORS ==============================*/
    public void importData() {
        /* Strategy Design Pattern - Implementation of writing and reading buried in concrete classes */
        ImportFromCSV categoriesImporter = new CategoriesImport();
        ImportFromCSV cardsImporter = new CardsImport();
        ImportFromCSV purchasesImporter = new PurchasesImport();

        // Instantiate a new SwingWorker thread and use an anonymous class to implement its behaviour on a separate thread
        // from the GUI Event Dispatch Thread. This stops freezing up of the GUI when exporting or exporting.
        SwingWorker<Void, Integer> importWorker = new SwingWorker<Void, Integer>() {
            protected Void doInBackground() throws Exception {
                try {
                    categoriesImporter.executeImport(DataStoreDAO.this, CATEGORIES_LOG_PATH);
                } catch (ImportException e) {
                    System.err.println("Import Error: " + e.getMessage());
                    System.err.println("Caused by: " + e.getCause());
                }

                try {
                    cardsImporter.executeImport(DataStoreDAO.this, CARDS_LOG_PATH);
                } catch (ImportException e) {
                    System.err.println("Import Error: " + e.getMessage());
                    System.err.println("Caused by: " + e.getCause());
                }

                try {
                    purchasesImporter.executeImport(DataStoreDAO.this, PURCHASES_LOG_PATH);
                } catch (ImportException e) {
                    System.err.println("Import Error: " + e.getMessage());
                    System.err.println("Caused by: " + e.getCause());
                }

                return null;
            }

            protected void done() {
                notifyObservers();
            }
        };

        importWorker.execute();
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
                        // When the user presses cancel, the exception will break the loop and progress never reaches 100
                        break;
                    }
                    progressBar.setValue(progress += 10);
                }

                // This task is done so fast that u don't really notice.
                // While its working in the background, the GUI is not locked up
                if (progress == 100) {
                    try {
                        // Explicit call but implicitly, Files.newBufferedWriter uses CREATE | TRUNCATE_EXISTING | WRITE
                        categoriesExport.exportData(DataStoreDAO.this, Files.newBufferedWriter(CATEGORIES_LOG_PATH, StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
                        cardsExport.exportData(DataStoreDAO.this, Files.newBufferedWriter(CARDS_LOG_PATH));
                        purchasesExport.exportData(DataStoreDAO.this, Files.newBufferedWriter(PURCHASES_LOG_PATH));
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

    /*============================== CREATE ==============================*/
    public void createCategory(Category category) {
        this.categoriesMap.put(category.getId(), category);
        notifyObservers();
    }
    public void createCard(Card card) {
        this.cardsMap.put(card.getID(), card);
        notifyObservers();
    }
    public void createPurchase(Purchase purchase) {
        Generator.addReceiptID(purchase.getReceiptID());
        this.purchasesMap.put(purchase.getReceiptID(), purchase);
        updateCategoryTotalAmount(purchase.getCategories());
        updatePurchaseDateBounds();
        notifyObservers();
    }

    /*============================== RETRIEVE ==============================*/
    public HashMap<String,Card> getOrigCardsMap() {
        return cardsMap;
    }
    public Card getCard(String cardID) { return cardsMap.getOrDefault(cardID, null); }
    public HashMap<Integer,Purchase> getOrigPurchasesMap() {
        return purchasesMap;
    }
    public Purchase getPurchase(int receiptID) {
        return purchasesMap.getOrDefault(receiptID, null);
    }
    public HashMap<Integer, Category> getOrigCategoriesMap() {
        return categoriesMap;
    }
    public Category getCategory(int categoryID) {
        return categoriesMap.getOrDefault(categoryID, null);
    }

    /*============================== UPDATE ==============================*/
    public void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap) {
        categoriesMap.values().forEach((c) -> c.updateTotalAmount(purchaseCategoriesMap.get(c.getId()).getAmount()));
    }
    private void updatePurchaseDateBounds() {
        firstPurchaseDate = purchasesMap.values().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseTime))
                .map(Purchase::getPurchaseTime).findFirst().orElse(null);
        lastPurchaseDate = purchasesMap.values().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseTime).reversed())
                .map(Purchase::getPurchaseTime).findFirst().orElse(null);
    }

    /*============================== DELETE ==============================*/
    public void deleteCard(String cardID) {
        cardsMap.remove(cardID);
        updatePurchaseDateBounds();
        notifyObservers();
    }
    public void deletePurchase() {
        System.out.println("NOT YET IMPLEMENTED");
    }
    public void deleteCategory(int id) {
        categoriesMap.remove(id);
        notifyObservers();
    }

    /*============================== OBSERVER DESIGN PATTERN ==============================*/
    /* Implement DataObservable interface making this object instance a DataObservable */
    public void register(DataObserver observer) {
        dataObservers.add(observer);
    }
    public void unregister(DataObserver observer) {
        dataObservers.remove(observer);
    }
    public void notifyObservers() { dataObservers.forEach(DataObserver::update); }
    // NOTE: All these methods only return a deep copy of the original data as constructed by a stream
    public TreeMap<String, Card> getCardsUpdate(DataObserver who) {
        return cardsMap.entrySet().parallelStream().map(c -> c.getValue().clone(c.getValue()))
                .collect(Collectors.toMap(Card::getID, c -> c, (k,v) -> k, TreeMap::new));
    }
    public TreeMap<Integer, Purchase> getPurchaseUpdate(DataObserver who) {
        return purchasesMap.entrySet().parallelStream().map(p -> new Purchase(p.getValue()))
                .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k,v) -> k, TreeMap::new));
    }
    public TreeMap<Integer, Category> getCategoriesUpdate(DataObserver who) {
        return categoriesMap.entrySet().parallelStream().map(c -> new Category(c.getValue(), c.getValue().getTotalAmount()))
                .collect(Collectors.toMap(Category::getId, c -> c, (k,v) -> k, TreeMap::new));
    }
    // NOTE: This returns a shallow copy only
    public LocalDateTime getFirstPurchaseDate(DataObserver who) {
        return (firstPurchaseDate == null) ? LocalDateTime.now() : firstPurchaseDate;
    }
    public LocalDateTime getLastPurchaseDate(DataObserver who) {
        return  (lastPurchaseDate == null) ? LocalDateTime.now() : lastPurchaseDate;
    }
}
