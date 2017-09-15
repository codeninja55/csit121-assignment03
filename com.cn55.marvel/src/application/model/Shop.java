package application.model;

import application.model.CardModel.*;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import java.util.*;

/* SINGLETON DESIGN PATTERN */

@SuppressWarnings("ConstantConditions")
public class Shop {
    private static Shop shop;
    private final DataDAO db;

    /*============================== CONSTRUCTORS  ==============================*/
    private Shop() {
        this.db = new DataDAO();
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
        db.createCategory(category);
    }

    public void makePurchase(String cardID, int receiptID, HashMap<Integer, Category> categories) {
        if (cardID.equals(CardType.Cash.getName())) {
            db.createPurchase(new Purchase(categories, receiptID));
        } else {
            if (db.getAllCards().containsKey(cardID)) {
                Card card = db.getCard(cardID);
                String cardType = card.getCardType();
                Purchase newPurchase = new Purchase(cardID, cardType, categories, receiptID);
                card.calcPoints(newPurchase.getCategoriesTotal());

                if(!cardType.equals(CardType.AnonCard.getName()))
                    card.calcBalance(newPurchase.getCategoriesTotal());

                db.createPurchase(newPurchase);
            }
        }
    }

    public void makeCard(HashMap<String, String> newCard) {

        String name = newCard.get("name");
        String email = newCard.get("email");
        String cardType = newCard.get("cardType");

        if (!cardType.equals(CardType.AnonCard.getName())) {
            if (cardType.equals(CardType.BasicCard.getName()))
                db.createCard(new BasicCard(name, email));
            else
                db.createCard(new PremiumCard(name, email));
        } else {
            db.createCard(new AnonCard());
        }
    }

    public void deleteCard(String cardID) {
        db.deleteCard(cardID);
    }

    // Converts a purchase from a card purchase to a cash purchase when the card has been deleted
    public void convertPurchase(String cardID) {
        db.getAllPurchases().forEach((k,v) -> {
            if (v.getCardID() != null && v.getCardID().equals(cardID)) v.convertPurchase();
        });
    }

    public void deleteCategory(int categoryID) {
        // Update the amount for each purchase
        db.getAllPurchases().values().forEach((Purchase p) -> {
            Category other = p.getCategories().get(100);
            other.setAmount(other.getAmount() + p.getCategories().get(categoryID).getAmount());
            p.getCategories().remove(categoryID);
        });

        // Update the total amounts in the defaultCategories List
        double deletedCategoryValue = db.getCategories().get(categoryID).getTotalAmount();
        db.getCategories().get(100).updateTotalAmount(deletedCategoryValue);

        db.deleteCategory(categoryID);
    }

    /*============================== ACCESSORS ==============================*/
    public DataDAO getDataStore() {
        return db;
    }
}
