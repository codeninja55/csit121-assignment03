package cn55.model;

/* THIS CLASS IS A TEMP DATABASE OBJECT */

import cn55.model.CardModel.AdvancedCard;
import cn55.model.CardModel.Card;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/* SINGLETON DESIGN PATTERN */

@SuppressWarnings("ConstantConditions")
public class Database implements Subject {

    private static Database db;
    private static Formatter output;

    private final ArrayList<Observer> observers;
    private final ArrayList<Card> cards;
    private HashMap<String, Integer> cardMap;
    private final ArrayList<Purchase> purchases;
    private HashMap<Integer, Integer> purchaseMap;
    private final ArrayList<Category> categories;
    private HashMap<Integer, Integer> categoriesMap;

    private static int cardIDCounter = 10000;
    private static int categoryIDCounter = 100;

    private static final HashMap<Integer, Double> categoriesTotalMap = new HashMap<>();
    private static final Set<Integer> receiptSet = new HashSet<>();

    /*============================== CONSTRUCTORS  ==============================*/
    // Private modifier prevents any other class from instantiating
    private Database() {
        this.observers = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.cardMap = new HashMap<>();
        this.purchases = new ArrayList<>();
        this.purchaseMap = new HashMap<>();
        this.categories = new ArrayList<>();
        this.categoriesMap = new HashMap<>();
    }

    // Provide global point of access
    // Double check locking mechanism but only with the initial call
    public static synchronized Database getDBInstance() {
        if (db == null) db = new Database();
        return db;
    }

    /*============================== STATIC METHODS ==============================*/
    public static int generateReceiptID() {
        Random randomObj = new Random();

        int receiptID;
        receiptID = randomObj.ints(10000000,99999999).findFirst().getAsInt();

        if (receiptSet.contains(receiptID)) {
            return generateReceiptID();
        } else {
            addReceiptID(receiptID);
            return receiptID;
        }
    }

    public static String generateCardID() {
        return "MC" + (++cardIDCounter);
    }

    static int generateCategoryID() {
        return categoryIDCounter++;
    }

    private static void addReceiptID(int receiptID) { Database.receiptSet.add(receiptID); }

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
        categories.forEach((category)->categoriesMap.put(category.getId(), categories.indexOf(category)));
        this.categoriesMap = categoriesMap;
    }

    // Map the Categories to a HashMap to store total amounts for each category across the whole program
    static void mapCategoriesTotalMap(ArrayList<Category> categories) {
        if (categoriesTotalMap.size() == 0)
            categories.forEach((item) -> categoriesTotalMap.put(item.getId(), 0D));
    }

    // Accepts the same categories HashMap stored in a Purchase object and updates the categoriesTotalMap
    // to reflect either the new Category added or the updated total amount for an existing category.
    static void updateCategoriesTotalMap(HashMap<Integer, Category> categories) {
        for (HashMap.Entry<Integer, Category> item : categories.entrySet()) {
            if (!categoriesTotalMap.containsKey(item.getKey())) {
                Database.categoriesTotalMap.put(item.getKey(), item.getValue().getAmount());
            } else {
                Double newTotal = Database.categoriesTotalMap.get(item.getKey()) + item.getValue().getAmount();
                Database.categoriesTotalMap.put(item.getKey(), newTotal);
            }
        }
    }

    public void addCategory(Category category) {
        categories.add(category);
        mapCategories();
        notifyObservers();
    }

    public void addCards(Card card) {
        this.cards.add(card);
        mapCards();
        notifyObservers();
    }

    void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
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
        categories.remove(index);
        mapCategories();
        notifyObservers();
    }

    /*============================== ACCESSORS  ==============================*/
    public static String getNextCardID() {
        return "MC" + (cardIDCounter + 1);
    }

    public static int getNextCategoryID() {
        return categoryIDCounter;
    }

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

    public ArrayList<Category> getCategories() { return categories; }

    public HashMap<Integer, Integer> getCategoriesMap() {
        return categoriesMap;
    }

    public static HashMap<Integer, Double> getCategoriesTotalMap() {
        return categoriesTotalMap;
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
    public void register(Observer obj) {
        observers.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public ArrayList<Card> getCardsUpdate(Observer who) {
        return cards;
    }

    @Override
    public ArrayList<Purchase> getPurchaseUpdate(Observer who) {
        return purchases;
    }

    @Override
    public ArrayList<Category> getCategoriesUpdate(Observer who) {
        return categories;
    }

    /*============================== SEQUENTIAL FILE STREAM ==============================*/
    public void writeCards() {
        openFile();

        cards.forEach(card -> {
            try {
                String name = (card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "";
                String email = (card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "";
                double balance = (card instanceof AdvancedCard) ? ((AdvancedCard) card).getBalance() : 0;

                output.format("%s %s %s %s %.2f %.2f%n", card.getID(), card.getCardType(),
                        name, email, balance, card.getPoints());
            } catch (FormatterClosedException e) {
                /* TODO - TESTING */
                System.err.println("FormattedClosedException");
                e.printStackTrace();
            }
        });

        closeFile();
    }

    public void openFile() {
        try {
            Path path = Paths.get("database.txt");
            File file = new File("database.txt");
            output = new Formatter("database.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        output.close();
    }
}
