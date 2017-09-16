package application.model;

import application.model.CardModel.Card;
import application.model.CategoryModel.Category;
import application.model.PurchaseModel.Purchase;

import java.util.TreeMap;

@SuppressWarnings("unused")
public interface DataObservable {

    // Methods to register and unregister observers
    void register(DataObserver obj);
    void unregister(DataObserver obj);

    // Method to notify observers of change
    void notifyObservers();

    // Methods to get updates
    TreeMap<String, Card> getCardsUpdate(DataObserver who);
    TreeMap<Integer, Purchase> getPurchaseUpdate(DataObserver who);
    TreeMap<Integer, Category> getCategoriesUpdate(DataObserver who);
}
