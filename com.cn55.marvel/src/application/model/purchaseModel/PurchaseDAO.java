package application.model.purchaseModel;

import java.util.HashMap;

public interface PurchaseDAO {
    void createPurchase(Purchase purchase);
    HashMap<Integer,Purchase> getAllPurchases();
    Purchase getPurchase(int receiptID);
    void deletePurchase();
}
