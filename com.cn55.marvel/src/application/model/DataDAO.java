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
    private final HashMap<String,Card> cards;
    private final HashMap<Integer,Purchase> purchases;
    private final ArrayList<Category> defaultCategories;
    private HashMap<Integer, Integer> categoriesMap;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    DataDAO() {
        this.cards = new HashMap<>();
        this.purchases = new HashMap<>();
        this.dataObservers = new ArrayList<>();
        this.defaultCategories = new ArrayList<>();
        this.categoriesMap = new HashMap<>();
    }

    /*============================== CREATE ==============================*/
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
        cards.put(card.getID(), card);
        notifyObservers();
    }

    public void createPurhcase(Purchase purchase) {
        updateCategoryTotalAmount(purchase.getCategories());
        purchases.put(purchase.getReceiptID(), purchase);
        notifyObservers();
    }

    /*============================== RETRIEVE ==============================*/
    public HashMap<String,Card> getAllCards() {
        return cards;
    }

    public Card getCard(String cardID) { return cards.get(cardID); }

    public DefaultComboBoxModel<String> getCardModel() {
        DefaultComboBoxModel<String> cardModel = new DefaultComboBoxModel<>();
        ArrayList<Card> cardsClone = new ArrayList<>();
        cardsClone.addAll(cards.values());
        cardsClone.sort(Comparator.comparing(Card::getID));
        cardModel.addElement("Please Select");
        cardsClone.forEach((card)->cardModel.addElement(card.getID()));
        return cardModel;
    }

    public HashMap<Integer,Purchase> getAllPurchases() {
        return purchases;
    }

    public Purchase getPurchase(int receiptID) {
        return purchases.get(receiptID);
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
        cards.remove(cardID);
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
        return new TreeMap<>(cards);
    }

    public TreeMap<Integer,Purchase> getPurchaseUpdate(DataObserver who) {
        return new TreeMap<>(purchases);
    }

    public ArrayList<Category> getCategoriesUpdate(DataObserver who) {
        return defaultCategories;
    }
}
