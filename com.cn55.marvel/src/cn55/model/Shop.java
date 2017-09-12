package cn55.model;

import cn55.controller.Validator.CardExistsRule;
import cn55.controller.Validator.ExistsRule;
import cn55.controller.Validator.FormValidData;
import cn55.model.CardModel.AnonCard;
import cn55.model.CardModel.BasicCard;
import cn55.model.CardModel.Card;
import cn55.model.CardModel.PremiumCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class Shop {

    private final Database db;

    /*============================== CONSTRUCTORS  ==============================*/
    public Shop() {
        this.db = Database.getDBInstance();
        generateDefaultCategories();
        Database.mapCategoriesTotalMap(db.getCategories());
    }

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
            Database.updateCategoriesTotalMap(categories);
        } else {
            if (cardExistsRule.existsValidating(validData) >= 0) {
                Card card = db.getCards().get(db.getCardMap().get(cardID));
                String cardType = card.getCardType();
                Purchase newPurchase = new Purchase(cardID, cardType, categories, receiptID);
                card.calcPoints(newPurchase.getCategoriesTotal());

                if(!cardType.equals(CardType.AnonCard.getName()))
                    card.calcBalance(newPurchase.getCategoriesTotal());

                db.addPurchase(newPurchase);
                Database.updateCategoriesTotalMap(categories);
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

        Double newValue = Database.getCategoriesTotalMap().get(100) + Database.getCategoriesTotalMap().get(categoryID);
        Database.getCategoriesTotalMap().replace(100, newValue);
        Database.getCategoriesTotalMap().remove(categoryID);

        db.removeCategory(db.getCategoriesMap().get(categoryID));
    }
}
