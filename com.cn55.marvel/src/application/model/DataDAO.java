package application.model;

import application.model.card.Card;
import application.model.card.CardsDAO;
import application.model.card.CardsExport;
import application.model.card.CardsImport;
import application.model.category.CategoriesExport;
import application.model.category.CategoriesImport;
import application.model.category.Category;
import application.model.category.CategoryDAO;
import application.model.purchase.Purchase;
import application.model.purchase.PurchaseDAO;
import application.model.purchase.PurchasesExport;
import application.model.purchase.PurchasesImport;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

/* Data Access Object (DAO) Implementation Layer */
@SuppressWarnings("ConstantConditions")
public class DataDAO implements DataObservable, CardsDAO, PurchaseDAO, CategoryDAO {
    private final ArrayList<DataObserver> dataObservers;
    private final HashMap<String,Card> cards;
    private final HashMap<Integer,Purchase> purchases;
    private final HashMap<Integer,Category> categories;

    private final Path CATEGORIES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistentData/CategoriesStorage.csv");
    private final Path CARDS_LOG_PATH = Paths.get("com.cn55.marvel/src/persistentData/CardsStorage.csv");
    private final Path PURCHASES_LOG_PATH = Paths.get("com.cn55.marvel/src/persistentData/PurchaseStorage.csv");

    private LocalDateTime firstPurchaseDate;
    private LocalDateTime lastPurchaseDate;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    DataDAO() {
        this.cards = new HashMap<>();
        this.purchases = new HashMap<>();
        this.dataObservers = new ArrayList<>();
        this.categories = new HashMap<>();
    }

    /*============================== CREATE ==============================*/
    public void createCategory(Category category) {
        categories.put(category.getId(), category);
        notifyObservers();
    }
    public void createCard(Card card) {
        cards.put(card.getID(), card);
        notifyObservers();
    }
    public void createPurchase(Purchase purchase) {
        updateCategoryTotalAmount(purchase.getCategories());
        Generator.addReceiptID(purchase.getReceiptID());
        purchases.put(purchase.getReceiptID(), purchase);
        updatePurchaseDateBounds();
        notifyObservers();
    }

    /*============================== RETRIEVE ==============================*/
    public HashMap<String,Card> getAllCards() {
        return cards;
    }
    public Card getCard(String cardID) { return cards.getOrDefault(cardID, null); }
    public HashMap<Integer,Purchase> getAllPurchases() {
        return purchases;
    }
    public Purchase getPurchase(int receiptID) {
        return purchases.getOrDefault(receiptID, null);
    }
    public HashMap<Integer, Category> getAllCategories() {
        return categories;
    }
    public Category getCategory(int categoryID) {
        return categories.getOrDefault(categoryID, null);
    }

    /*============================== UPDATE ==============================*/
    public void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap) {
        categories.values().forEach((c) -> c.updateTotalAmount(purchaseCategoriesMap.get(c.getId()).getAmount()));
    }
    private void updatePurchaseDateBounds() {
        firstPurchaseDate = purchases.values().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseTime))
                .map(Purchase::getPurchaseTime).findFirst().orElse(LocalDateTime.of(1970,1,1,0,0));
        lastPurchaseDate = purchases.values().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseTime).reversed())
                .map(Purchase::getPurchaseTime).findFirst().orElse(LocalDateTime.now());
    }

    /*============================== DELETE ==============================*/
    public void deleteCard(String cardID) {
        cards.remove(cardID);
        updatePurchaseDateBounds();
        notifyObservers();
    }
    public void deletePurchase() {
        System.out.println("NOT YET IMPLEMENTED");
    }
    public void deleteCategory(int id) {
        categories.remove(id);
        notifyObservers();
    }

    /*============================== FILE CONNECTOR ==============================*/
    private BufferedReader openReadFile(Path path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path.toString()));
    }
    public void readData() {
        /* Strategy Design Pattern - Implementation of writing and reading buried in concrete classes */
        ImportFromCSV categoriesImporter = new CategoriesImport();
        ImportFromCSV cardsImporter = new CardsImport();
        ImportFromCSV purchasesImporter = new PurchasesImport();

        try {
            categoriesImporter.importData(openReadFile(CATEGORIES_LOG_PATH));
            cardsImporter.importData(openReadFile(CARDS_LOG_PATH));
            purchasesImporter.importData(openReadFile(PURCHASES_LOG_PATH));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        }
    }
    private BufferedWriter openWriteFile(Path path) throws IOException {
        return new BufferedWriter(new FileWriter(path.toString()));
    }
    public void writeData() {
        ExportToCSV categoriesExport = new CategoriesExport();
        ExportToCSV cardsExport = new CardsExport();
        ExportToCSV purchasesExport = new PurchasesExport();

        try {
            categoriesExport.exportData(openWriteFile(CATEGORIES_LOG_PATH));
            cardsExport.exportData(openWriteFile(CARDS_LOG_PATH));
            purchasesExport.exportData(openWriteFile(PURCHASES_LOG_PATH));
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    /*============================== OBSERVER DESIGN PATTERN ==============================*/
    /* Implement DataObservable interface making this object instance a DataObservable */
    public void register(DataObserver obj) {
        dataObservers.add(obj);
    }
    public void unregister(DataObserver obj) {
        dataObservers.remove(obj);
    }
    public void notifyObservers() {
        dataObservers.forEach(DataObserver::update);
    }
    public TreeMap<String, Card> getCardsUpdate(DataObserver who) { return new TreeMap<>(cards); }
    public TreeMap<Integer, Purchase> getPurchaseUpdate(DataObserver who) {
        return new TreeMap<>(purchases);
    }
    public TreeMap<Integer, Category> getCategoriesUpdate(DataObserver who) {
        return new TreeMap<>(categories);
    }
    public LocalDateTime getFirstPurchaseDate(DataObserver who) { return firstPurchaseDate; }
    public LocalDateTime getLastPurchaseDate(DataObserver who) { return lastPurchaseDate; }
}
