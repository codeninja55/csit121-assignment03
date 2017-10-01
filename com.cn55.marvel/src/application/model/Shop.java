package application.model;

import application.model.card.*;
import application.model.category.Category;
import application.model.dao.AuthenticatorDAO;
import application.model.dao.DataStoreDAO;
import application.model.purchase.Purchase;

import java.util.HashMap;

/* SINGLETON DESIGN PATTERN */

@SuppressWarnings("ConstantConditions")
public class Shop {
    private static Shop shop;
    private final DataStoreDAO dataStore;
    private final AuthenticatorDAO authenticator;

    /*============================== CONSTRUCTORS  ==============================*/
    private Shop() {
        this.dataStore = DataStoreDAO.getDataStoreInstance();
        this.authenticator = new AuthenticatorDAO();
    }

    // Provide global point of access
    // Double check locking mechanism but only with the initial call
    public static synchronized Shop getShopInstance() {
        if (shop == null)
            shop = new Shop();

        return shop;
    }

    /*============================== MUTATORS  ==============================*/
    public void makeCategory(Category category) {
        dataStore.createCategory(category);
        updatePurchaseAddCategory(category);
    }

    public void makePurchase(String cardID, int receiptID, HashMap<Integer, Category> categories) {
        if (cardID.equals(CardType.Cash.name)) {
            dataStore.createPurchase(new Purchase(categories, receiptID));
        } else {
            if (dataStore.getOrigCardsMap().containsKey(cardID)) {
                Card card = dataStore.getCard(cardID);
                String cardType = card.getCardType();
                Purchase newPurchase = new Purchase(cardID, cardType, categories, receiptID);
                card.calcPoints(newPurchase.getCategoriesTotal());

                if (card instanceof AdvancedCard)
                    ((AdvancedCard)card).calcBalance(newPurchase.getCategoriesTotal());

                dataStore.createPurchase(newPurchase);
            }
        }
    }

    public void makeCard(HashMap<String, String> newCard) {
        String name = newCard.get("name");
        String email = newCard.get("email");
        String cardType = newCard.get("cardType");

        if (!cardType.equals(CardType.AnonCard.name)) {
            if (cardType.equals(CardType.BasicCard.name))
                dataStore.createCard(new BasicCard(name, email));
            else
                dataStore.createCard(new PremiumCard(name, email));
        } else {
            dataStore.createCard(new AnonCard());
        }
    }

    public void deleteCard(String cardID) {
        dataStore.deleteCard(cardID);
        convertPurchase(cardID);
    }

    public void deleteCategory(int categoryID) {
        // Update the amount for each purchase
        dataStore.getOrigPurchasesMap().values().forEach((Purchase p) -> {
            Category other = p.getCategories().get(100);
            other.setAmount(other.getAmount() + p.getCategories().get(categoryID).getAmount());
            p.getCategories().remove(categoryID);
        });

        // Update the total amounts in the defaultCategories List
        double deletedCategoryValue = dataStore.getOrigCategoriesMap().get(categoryID).getTotalAmount();
        dataStore.getOrigCategoriesMap().get(100).updateTotalAmount(deletedCategoryValue);

        dataStore.deleteCategory(categoryID);
    }

    private void convertPurchase(String cardID) {
        // Converts a purchase from a card purchase to a cash purchase when the card has been deleted
        dataStore.getOrigPurchasesMap().values().parallelStream().filter(p -> p.getCardID() != null)
                .filter(p -> p.getCardID().equals(cardID)).forEach(p -> {
                    p.setCardID(null);
                    p.setCardType(CardType.Cash.name);
        });
    }

    private void updatePurchaseAddCategory(Category category) {
        // Adds the categories to each purchase when purchase created
        dataStore.getOrigPurchasesMap().values().parallelStream()
                .forEach(p -> p.getCategories().put(category.getId(), category));
    }

    /*============================== ACCESSORS ==============================*/
    public DataStoreDAO getDataStore() { return dataStore; }
    public AuthenticatorDAO getAuthenticator() { return authenticator; }
}
