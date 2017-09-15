package application.model;

import application.model.CardModel.Card;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import java.util.ArrayList;

@SuppressWarnings("unused")
public interface DataObservable {

    // Methods to register and unregister observers
    void register(DataObserver obj);
    void unregister(DataObserver obj);

    // Method to notify observers of change
    void notifyObservers();

    // Methods to get updates
    ArrayList<Card> getCardsUpdate(DataObserver who);
    ArrayList<Purchase> getPurchaseUpdate(DataObserver who);
    ArrayList<Category> getCategoriesUpdate(DataObserver who);
}