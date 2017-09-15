package application.model;

import application.model.CardModel.Card;
import application.model.CardModel.CardsDAO;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import javax.swing.*;
import java.util.*;

/* Data Abstract Object (DAO) Implementation Layer */
@SuppressWarnings("ConstantConditions")
public class DataDAO implements DataObservable, CardsDAO {

    private final ArrayList<DataObserver> dataObservers;
    private final HashMap<String,Card> cardsMap;
    private final ArrayList<Purchase> purchases;
    private HashMap<Integer, Integer> purchaseMap;
    private final ArrayList<Category> defaultCategories;
    private HashMap<Integer, Integer> categoriesMap;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    DataDAO() {
        this.cardsMap = new HashMap<>();
        this.dataObservers = new ArrayList<>();
        this.purchases = new ArrayList<>();
        this.purchaseMap = new HashMap<>();
        this.defaultCategories = new ArrayList<>();
        this.categoriesMap = new HashMap<>();
    }

    /*============================== CREATE ==============================*/
    void mapPurchases() {
        HashMap<Integer, Integer> purchaseMap = new HashMap<>();
        purchases.forEach((purchase)->purchaseMap.put(purchase.getReceiptID(), purchases.indexOf(purchase)));
        this.purchaseMap = purchaseMap;
    }

    void mapCategories() {
        HashMap<Integer, Integer> categoriesMap = new HashMap<>();
        defaultCategories.forEach((category)->categoriesMap.put(category.getId(), defaultCategories.indexOf(category)));
        this.categoriesMap = categoriesMap;
    }

    public void addCategory(Category category) {
        defaultCategories.add(category);
        mapCategories();
        notifyObservers();
    }

    public void createCard(Card card) {
        cardsMap.put(card.getID(), card);
        notifyObservers();
    }

    public void addPurchase(Purchase purchase) {
        updateCategoryTotalAmount(purchase.getCategories());
        purchases.add(purchase);
        mapPurchases();
        notifyObservers();
    }

    /*============================== RETRIEVE ==============================*/
    public HashMap<String,Card> getAllCards() {
        return cardsMap;
    }

    public Card getCard(String cardID) { return cardsMap.get(cardID); }

    public DefaultComboBoxModel<String> getCardModel() {
        DefaultComboBoxModel<String> cardModel = new DefaultComboBoxModel<>();
        ArrayList<Card> cardsClone = new ArrayList<>();
        cardsClone.addAll(cardsMap.values());
        cardsClone.sort(Comparator.comparing(Card::getID));
        cardModel.addElement("Please Select");
        cardsClone.forEach((card)->cardModel.addElement(card.getID()));
        return cardModel;
    }

    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }

    public HashMap<Integer, Integer> getPurchaseMap() {
        return purchaseMap;
    }

    public ArrayList<Category> getDefaultCategories() { return defaultCategories; }

    public HashMap<Integer, Integer> getCategoriesMap() {
        return categoriesMap;
    }

    /*============================== UPDATE ==============================*/

    private void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap) {
        defaultCategories.forEach((c) -> c.updateTotalAmount(purchaseCategoriesMap.get(c.getId()).getAmount()));
    }

    /*============================== DELETE ==============================*/
    public void deleteCard(String cardID) {
        cardsMap.remove(cardID);
        mapPurchases();
        notifyObservers();
    }

    void removeCategory(int index) {
        defaultCategories.remove(index);
        mapCategories();
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

    public void notifyObservers() {
        dataObservers.forEach(DataObserver::update);
    }

    public TreeMap<String,Card> getCardsUpdate(DataObserver who) {
        // Returning a TreeMap so it is sorted by keys
        return new TreeMap<>(cardsMap);
    }

    public ArrayList<Purchase> getPurchaseUpdate(DataObserver who) {
        return purchases;
    }

    public ArrayList<Category> getCategoriesUpdate(DataObserver who) {
        return defaultCategories;
    }
}
