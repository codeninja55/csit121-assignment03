package application.model.PurchaseModel;

import java.util.HashMap;

public interface PurchaseDAO {
    void createPurchase(Purchase purchase);
    HashMap<Integer,Purchase> getAllPurchases();
    Purchase getPurchase();
    void deletePurchase();
}
