package application.model.dao;

import application.model.purchase.Purchase;

import java.util.HashMap;

interface PurchaseDAO {
    @SuppressWarnings("unused")
    void createPurchase(Purchase purchase);
    @SuppressWarnings("unused")
    HashMap<Integer,Purchase> getOrigPurchasesMap();
    @SuppressWarnings("unused")
    Purchase getPurchase(int receiptID);
    @SuppressWarnings("unused")
    void deletePurchase();
}
