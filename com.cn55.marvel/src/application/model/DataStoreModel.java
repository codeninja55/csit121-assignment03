package application.model;

/* THIS CLASS IS A LOCAL STORE DATA OBJECT */

import application.model.CardModel.Card;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import javax.swing.*;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class DataStoreModel implements Subject {

    private final ArrayList<application.model.Observer> observers;
    private final ArrayList<Card> cards;
    private HashMap<String, Integer> cardMap;
    private final ArrayList<Purchase> purchases;
    private HashMap<Integer, Integer> purchaseMap;
    private final ArrayList<Category> defaultCategories;
    private HashMap<Integer, Integer> categoriesMap;

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    DataStoreModel() {
        this.observers = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.cardMap = new HashMap<>();
        this.purchases = new ArrayList<>();
        this.purchaseMap = new HashMap<>();
        this.defaultCategories = new ArrayList<>();
        this.categoriesMap = new HashMap<>();
    }

    /*============================== MUTATORS  ==============================*/
    void mapCards() {
        HashMap<String, Integer> cardMap = new HashMap<>();
        cards.forEach((card)->cardMap.put(card.getID(), cards.indexOf(card)));
        this.cardMap = cardMap;
    }

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

    private void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap) {
        defaultCategories.forEach((c) -> c.updateTotalAmount(purchaseCategoriesMap.get(c.getId()).getAmount()));
    }

    public void addCategory(Category category) {
        defaultCategories.add(category);
        mapCategories();
        notifyObservers();
    }

    public void addCards(Card card) {
        cards.add(card);
        mapCards();
        notifyObservers();
    }

    public void addPurchase(Purchase purchase) {
        updateCategoryTotalAmount(purchase.getCategories());
        purchases.add(purchase);
        mapPurchases();
        notifyObservers();
    }

    void removeCard(int index) {
        cards.remove(index);
        mapCards();
        mapPurchases();
        notifyObservers();
    }

    void removeCategory(int index) {
        defaultCategories.remove(index);
        mapCategories();
        notifyObservers();
    }

    /*============================== ACCESSORS  ==============================*/
    public ArrayList<Card> getCards() {
        return cards;
    }

    public HashMap<String, Integer> getCardMap() { return cardMap; }

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

    public DefaultComboBoxModel<String> getCardModel() {
        DefaultComboBoxModel<String> cardModel = new DefaultComboBoxModel<>();
        ArrayList<Card> cardsClone = new ArrayList<>();
        cardsClone.addAll(cards);
        cardsClone.sort(Comparator.comparing(Card::getID));
        cardModel.addElement("Please Select");
        cardsClone.forEach((card)->cardModel.addElement(card.getID()));
        return cardModel;
    }

    /*============================== OBSERVER DESIGN PATTERN ==============================*/
    /* Implement Subject interface making this object instance a Subject */
    @Override
    public void register(application.model.Observer obj) {
        observers.add(obj);
    }

    @Override
    public void unregister(application.model.Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(application.model.Observer::update);
    }

    @Override
    public ArrayList<Card> getCardsUpdate(application.model.Observer who) {
        return cards;
    }

    @Override
    public ArrayList<Purchase> getPurchaseUpdate(application.model.Observer who) {
        return purchases;
    }

    @Override
    public ArrayList<Category> getCategoriesUpdate(Observer who) {
        return defaultCategories;
    }

}
