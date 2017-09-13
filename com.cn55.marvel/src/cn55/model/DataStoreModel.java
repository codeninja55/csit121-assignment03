package cn55.model;

/* THIS CLASS IS A TEMP DATABASE OBJECT */

import cn55.model.CardModel.AdvancedCard;
import cn55.model.CardModel.Card;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/* SINGLETON DESIGN PATTERN */

@SuppressWarnings("ConstantConditions")
public class DataStoreModel implements Subject {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char NEW_LINE_SEPARATOR = '\n';

    private static DataStoreModel db;
    private static BufferedWriter output;

    private final ArrayList<cn55.model.Observer> observers;
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
    private DataStoreModel() {
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
    public static synchronized DataStoreModel getDataStoreInstance() {
        if (db == null) db = new DataStoreModel();
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

    private static void addReceiptID(int receiptID) { DataStoreModel.receiptSet.add(receiptID); }

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
                DataStoreModel.categoriesTotalMap.put(item.getKey(), item.getValue().getAmount());
            } else {
                Double newTotal = DataStoreModel.categoriesTotalMap.get(item.getKey()) + item.getValue().getAmount();
                DataStoreModel.categoriesTotalMap.put(item.getKey(), newTotal);
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
    public void register(cn55.model.Observer obj) {
        observers.add(obj);
    }

    @Override
    public void unregister(cn55.model.Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(cn55.model.Observer::update);
    }

    @Override
    public ArrayList<Card> getCardsUpdate(cn55.model.Observer who) {
        return cards;
    }

    @Override
    public ArrayList<Purchase> getPurchaseUpdate(cn55.model.Observer who) {
        return purchases;
    }

    @Override
    public ArrayList<Category> getCategoriesUpdate(Observer who) {
        return categories;
    }

    /*============================== SEQUENTIAL CSV FILE WRITER ==============================*/
    public void writeCards() {
        String cardsHeader = "id,cardType,name,email,balance,points";
        Path cardsStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/cardsStorage.csv");
        openFile(cardsStoragePath);

        try{
            output.append(cardsHeader);
            output.append(NEW_LINE_SEPARATOR);

            for (Card card : cards) {
                output.append(card.getID()).append(DEFAULT_SEPARATOR);
                output.append(card.getCardType()).append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getName() : "").append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? ((AdvancedCard) card).getEmail() : "").append(DEFAULT_SEPARATOR);
                output.append((card instanceof AdvancedCard) ? Double.toString(((AdvancedCard) card).getBalance()) : "").append(DEFAULT_SEPARATOR);
                output.append(Double.toString(card.getPoints()));
                output.newLine();
            }
        } catch (IOException e) {
            // TODO - TESTING
            System.err.println("IOException: " + e.getMessage());
        }
        closeFile();
    }

    public void writePurchases() {
        String purchaseHeaders = "purchaseTime,receiptID,cardType,cardID,categories[]";
        Path purchaseStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/purchaseStorage.csv");
        openFile(purchaseStoragePath);

        try{
            output.append(purchaseHeaders);
            output.append(NEW_LINE_SEPARATOR);

            for (Purchase p : purchases) {
                int lastLine = 1;
                output.append(p.getPurchaseTime().toString()).append(DEFAULT_SEPARATOR);
                output.append(Integer.toString(p.getReceiptID())).append(DEFAULT_SEPARATOR);
                output.append(p.getCardType()).append(DEFAULT_SEPARATOR);
                output.append(p.getCardID()).append(DEFAULT_SEPARATOR);
                output.append("{");
                for (Category c : p.getCategories().values()) {
                    output.append("[").append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR);
                    output.append(c.getName()).append(DEFAULT_SEPARATOR);
                    output.append(c.getDescription()).append(DEFAULT_SEPARATOR);
                    output.append(Double.toString(c.getAmount())).append("]");

                    if (lastLine != p.getCategories().size()) {
                        output.append(DEFAULT_SEPARATOR);
                        lastLine++;
                    }
                }
                output.append("}");
                output.newLine();
            }
        } catch (IOException e) {
            // TODO - TESTING
            System.err.println("IOException: " + e.getMessage());
        }
        closeFile();
    }

    public void writeCategories() {
        String categoriesHeader = "id,name,description,amount";
        Path categoriesStoragePath = Paths.get("com.cn55.marvel/src/cn55/model/PersistentData/categoriesStorage.csv");
        openFile(categoriesStoragePath);

        try{
            output.append(categoriesHeader);
            output.append(NEW_LINE_SEPARATOR);
            for (Category c : categories) {
                output.append(Integer.toString(c.getId())).append(DEFAULT_SEPARATOR);
                output.append(c.getName()).append(DEFAULT_SEPARATOR);
                output.append(c.getDescription()).append(DEFAULT_SEPARATOR);
                output.append(Double.toString(c.getAmount()));
                output.newLine();
            }
        } catch (IOException e) {
            // TODO - TESTING
            System.err.println("IOException: " + e.getMessage());
        }
        closeFile();
    }

    private void openFile(Path path) {
        try {
            output = new BufferedWriter(new FileWriter(path.toString()));
        } catch (IOException e) {
            // TODO - TESTING
            System.err.println("IOException: " + e.getMessage());
        }
    }

    private void closeFile() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
