package application.model.dao;

import application.model.*;
import application.model.card.Card;
import application.model.card.CardsExport;
import application.model.card.CardsImport;
import application.model.category.CategoriesExport;
import application.model.category.CategoriesImport;
import application.model.category.Category;
import application.model.purchase.Purchase;
import application.model.purchase.PurchasesExport;
import application.model.purchase.PurchasesImport;
import application.view.custom.components.ProgressDialog;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/* Data Access Object (DAO) Implementation Layer */
public class DataStoreDAO extends DAO implements DataObservable, CardsDAO, PurchaseDAO, CategoryDAO {
    private final ArrayList<DataObserver> dataObservers;
    private LocalDateTime firstPurchaseDate;
    private LocalDateTime lastPurchaseDate;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    public DataStoreDAO() {
        this.dataObservers = new ArrayList<>();
        super.cards = new HashMap<>();
        super.purchases = new HashMap<>();
        super.categories = new HashMap<>();
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

    /*============================== OBSERVER DESIGN PATTERN ==============================*/
    /* Implement DataObservable interface making this object instance a DataObservable */
    public void register(DataObserver obj) {
        dataObservers.add(obj);
    }
    public void unregister(DataObserver obj) {
        dataObservers.remove(obj);
    }
    public void notifyObservers() { dataObservers.forEach(DataObserver::update); }
    // NOTE: All these methods only return a deep copy of the original data as constructed by a stream
    public TreeMap<String, Card> getCardsUpdate(DataObserver who) {
        return cards.entrySet().parallelStream().map(c -> c.getValue().clone(c.getValue()))
                .collect(Collectors.toMap(Card::getID, c -> c, (k,v) -> k, TreeMap::new));
    }
    public TreeMap<Integer, Purchase> getPurchaseUpdate(DataObserver who) {
        return purchases.entrySet().parallelStream().map(p -> new Purchase(p.getValue()))
                .collect(Collectors.toMap(Purchase::getReceiptID, p -> p, (k,v) -> k, TreeMap::new));
    }
    public TreeMap<Integer, Category> getCategoriesUpdate(DataObserver who) {
        return categories.entrySet().parallelStream().map(c -> new Category(c.getValue(), c.getValue().getTotalAmount()))
                .collect(Collectors.toMap(Category::getId, c -> c, (k,v) -> k, TreeMap::new));
    }
    // NOTE: This returns a shallow copy only
    public LocalDateTime getFirstPurchaseDate(DataObserver who) { return firstPurchaseDate; }
    public LocalDateTime getLastPurchaseDate(DataObserver who) { return lastPurchaseDate; }
}
