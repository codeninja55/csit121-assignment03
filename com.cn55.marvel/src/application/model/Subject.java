package application.model;

import application.model.CardModel.Card;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import java.util.ArrayList;

@SuppressWarnings("unused")
public interface Subject {

    // Methods to register and unregister observers
    void register(Observer obj);
    void unregister(Observer obj);

    // Method to notify observers of change
    void notifyObservers();

    // Methods to get updates
    ArrayList<Card> getCardsUpdate(Observer who);
    ArrayList<Purchase> getPurchaseUpdate(Observer who);
    ArrayList<Category> getCategoriesUpdate(Observer who);
}
