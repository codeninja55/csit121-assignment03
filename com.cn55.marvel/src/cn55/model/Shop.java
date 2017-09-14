package cn55.model;

import cn55.controller.Validator.CardExistsRule;
import cn55.controller.Validator.ExistsRule;
import cn55.controller.Validator.FormValidData;
import cn55.model.CardModel.AnonCard;
import cn55.model.CardModel.BasicCard;
import cn55.model.CardModel.Card;
import cn55.model.CardModel.PremiumCard;

import java.util.*;

/* SINGLETON DESIGN PATTERN */

@SuppressWarnings("ConstantConditions")
public class Shop {

    private static Shop shop;
    private final DataStoreModel db;
    private static int cardIDCounter = 10000;
    private static int categoryIDCounter = 100;
    private static final Set<Integer> receiptSet = new HashSet<>();

    /*============================== CONSTRUCTORS  ==============================*/
    private Shop() {
        this.db = new DataStoreModel();
        generateDefaultCategories();
        DataStoreModel.mapCategoriesTotalMap(db.getCategories());
    }

    // Provide global point of access
    // Double check locking mechanism but only with the initial call
    public static synchronized Shop getShopInstance() {
        if (shop == null)
            shop = new Shop();

        return shop;
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

    private static void addReceiptID(int receiptID) { receiptSet.add(receiptID); }

    /*============================== MUTATORS  ==============================*/
    private void generateDefaultCategories() {
        ArrayList<Category> categories = db.getCategories();
        categories.add(new Category("Other", "#Description"));
        categories.add(new Category("Motors", "#Description"));
        categories.add(new Category("Electronics", "#Description"));
        categories.add(new Category("Fashion", "#Description"));
        categories.add(new Category("Toys", "#Description"));
        categories.add(new Category("Deals", "#Description"));

        db.mapCategories();
        db.notifyObservers();
    }

    public void makeCategory(Category category) {
        db.addCategory(category);
    }

    public void makePurchase(String cardID, int receiptID, HashMap<Integer, Category> categories) {

        ExistsRule cardExistsRule = new CardExistsRule();
        FormValidData validData = new FormValidData();
        validData.setCardID(cardID);

        if (cardID.equals(CardType.Cash.getName())) {
            db.addPurchase(new Purchase(categories, receiptID));
            DataStoreModel.updateCategoriesTotalMap(categories);
        } else {
            if (cardExistsRule.existsValidating(validData) >= 0) {
                Card card = db.getCards().get(db.getCardMap().get(cardID));
                String cardType = card.getCardType();
                Purchase newPurchase = new Purchase(cardID, cardType, categories, receiptID);
                card.calcPoints(newPurchase.getCategoriesTotal());

                if(!cardType.equals(CardType.AnonCard.getName()))
                    card.calcBalance(newPurchase.getCategoriesTotal());

                db.addPurchase(newPurchase);
                DataStoreModel.updateCategoriesTotalMap(categories);
            }
        }
    }

    public void makeCard(HashMap<String, String> newCard) {

        String name = newCard.get("name");
        String email = newCard.get("email");
        String cardType = newCard.get("cardType");

        if (!cardType.equals(CardType.AnonCard.getName())) {
            if (cardType.equals(CardType.BasicCard.getName()))
                db.addCards(new BasicCard(name, email));
            else
                db.addCards(new PremiumCard(name, email));
        } else {
            db.addCards(new AnonCard());
        }
    }

    public void deleteCard(int cardIndex) {
        db.mapCards();
        db.removeCard(cardIndex);
    }

    // Converts a purchase from a card purchase to a cash purchase when the card has been deleted
    public void convertPurchase(String cardID) {
        db.mapPurchases();
        db.getPurchases().forEach(p -> {
            if (p.getCardID() != null && p.getCardID().equals(cardID)) p.convertPurchase();
        });
    }

    public void deleteCategory(int categoryID) {
        db.mapCategories();
        // MOVE AMOUNT TO OTHERS FOR EACH PURCHASE
        db.getPurchases().forEach((Purchase p) -> {
            Category other = p.getCategories().get(100);
            other.setAmount(other.getAmount() + p.getCategories().get(categoryID).getAmount());
            p.getCategories().remove(categoryID);
        });

        Double newValue = DataStoreModel.getCategoriesTotalMap().get(100) + DataStoreModel.getCategoriesTotalMap().get(categoryID);
        DataStoreModel.getCategoriesTotalMap().replace(100, newValue);
        DataStoreModel.getCategoriesTotalMap().remove(categoryID);

        db.removeCategory(db.getCategoriesMap().get(categoryID));
    }

    /*============================== ACCESSORS ==============================*/
    public static String getNextCardID() {
        return "MC" + (cardIDCounter + 1);
    }

    public static int getNextCategoryID() {
        return categoryIDCounter;
    }

    public DataStoreModel getDataStore() {
        return db;
    }
}
