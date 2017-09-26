package application.model.purchase;

import java.util.HashMap;

public interface PurchaseDAO {
    @SuppressWarnings("unused")
    void createPurchase(Purchase purchase);
    @SuppressWarnings("unused")
    HashMap<Integer,Purchase> getAllPurchases();
    @SuppressWarnings("unused")
    Purchase getPurchase(int receiptID);
    @SuppressWarnings("unused")
    void deletePurchase();
}
