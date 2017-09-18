package application.model;

import application.model.CardModel.Card;
import application.model.CardModel.CardsDAO;
import application.model.CardModel.CardsReadImpl;
import application.model.CategoryModel.CategoriesReadImpl;
import application.model.CategoryModel.Category;
import application.model.CategoryModel.CategoryDAO;
import application.model.PurchaseModel.Purchase;
import application.model.PurchaseModel.PurchaseDAO;
import application.model.PurchaseModel.PurchasesReadImpl;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /*============================== DELETE ==============================*/
    public void deleteCard(String cardID) {
        cards.remove(cardID);
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
    private BufferedReader openReadFile(Path path) {
        try {
            return new BufferedReader(new FileReader(path.toString()));
        } catch (FileNotFoundException e) {
            System.err.println("IOException: " + e.getMessage());
            return null;
        }
    }

    public void readData() {
        Path categoriesStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CategoriesStorage.csv");
        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/CardsStorage.csv");
        Path purchaseStoragePath = Paths.get("com.cn55.marvel/src/PersistentData/PurchaseStorage.csv");

        // Do something
        /* Strategy Design Pattern - Implementation of writing and reading buried in concrete classes */
        ReadCSV readCategoriesCSV = new CategoriesReadImpl();
        ReadCSV readPurchaseCSV = new PurchasesReadImpl();
        ReadCSV readCardsCSV = new CardsReadImpl();

        readCategoriesCSV.read(openReadFile(categoriesStoragePath));
        readPurchaseCSV.read(openReadFile(purchaseStoragePath));
        readCardsCSV.read(openReadFile(cardsStoragePath));
    }

    private BufferedWriter openWriteFile(Path path) {
        // Do something
        return null;
    }

    public void writeData() {
        // Do something
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
    public TreeMap<String,Card> getCardsUpdate(DataObserver who) { return new TreeMap<>(cards); }
    public TreeMap<Integer,Purchase> getPurchaseUpdate(DataObserver who) {
        return new TreeMap<>(purchases);
    }
    public TreeMap<Integer, Category> getCategoriesUpdate(DataObserver who) {
        return new TreeMap<>(categories);
    }
}
