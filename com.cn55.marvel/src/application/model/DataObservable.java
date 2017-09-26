package application.model;

import application.model.card.Card;
import application.model.category.Category;
import application.model.purchase.Purchase;

import java.time.LocalDateTime;
import java.util.TreeMap;

@SuppressWarnings("All")
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
    LocalDateTime getFirstPurchaseDate(DataObserver who);
    LocalDateTime getLastPurchaseDate(DataObserver who);
}
