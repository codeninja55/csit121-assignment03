package application.model.purchase;

import application.model.card.CardType;
import application.model.category.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@SuppressWarnings("SameParameterValue")
public class Purchase implements Comparable<Purchase> {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime purchaseTime;
    private final int receiptID;
    private String cardID;
    private String cardType;
    private final HashMap<Integer, Category> categories;

    /*============================== CONSTRUCTORS ==============================*/
    // Constructor for Cash purchasesMap
    public Purchase(HashMap<Integer, Category> categories, int receiptID) {
        this.receiptID = receiptID;
        this.cardID = null;
        this.cardType = CardType.Cash.name;
        this.categories = categories;
        this.purchaseTime = setPurchaseTime();
    }

    // Constructor for Card purchasesMap
    public Purchase(String cardID, String cardType, HashMap<Integer, Category> categories, int receiptID) {
        this.receiptID = receiptID;
        this.cardID = cardID;
        this.cardType = cardType;
        this.categories = categories;
        this.purchaseTime = setPurchaseTime();
    }

    // Constructor for importing cards
    public Purchase(String purchaseTimeStr, int receiptID, String cardType, String cardID, HashMap<Integer, Category> categories) {
        this.receiptID = receiptID;
        this.cardID = cardID;
        this.cardType = cardType;
        this.purchaseTime = LocalDateTime.parse(purchaseTimeStr, DATE_TIME_FORMAT);
        this.categories = categories;
    }

    // Clone constructor
    public Purchase(Purchase o) {
        this.purchaseTime = o.getPurchaseTime();
        this.receiptID = o.getReceiptID();
        this.cardID = o.getCardID();
        this.cardType = o.getCardType();
        this.categories = o.getCategories();
    }

    /*============================== MUTATORS  ==============================*/

    private LocalDateTime setPurchaseTime() {
        // REF: https://stackoverflow.com/questions/1459656/how-to-get-the-current-time-in-yyyy-mm-dd-hhmisec-millisecond-format-in-java
        // REF: https://docs.oracle.com/javase/tutorial/datetime/overview/design.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        return LocalDateTime.now();
    }

    public void convertPurchase() {
        this.cardType = CardType.Cash.name;
        this.cardID = null;
    }

    /*============================== ACCESSORS  ==============================*/
    public int getReceiptID() { return receiptID; }

    public String getCardID() { return cardID; }

    public String getCardType() { return cardType; }

    public HashMap<Integer, Category> getCategories() {
        return categories;
    }

    public LocalDateTime getPurchaseTime() { return purchaseTime; }

    public String getPurchaseTimeStr() { return purchaseTime.format(DATE_TIME_FORMAT); }

    public double getCategoriesTotal() {
        return categories.values().stream().mapToDouble(Category::getAmount).sum();
    }

    public String toString() {
        StringBuilder secondOutput = new StringBuilder();
        String firstOutput = String.format("%n%s%s%n%s%s%n%s%s%n%s%s",
                "Receipt ID: ", receiptID,
                "Card Type: ", cardType,
                "Card ID: ", cardID,
                "Purchase Time: ", getPurchaseTimeStr());

        categories.values().forEach(c -> secondOutput.append(String.format("%n%s$%.2f", (c.getName() + ": "), c.getAmount())));
        return firstOutput + secondOutput;
    }

    public int compareTo(Purchase o) { return this.purchaseTime.compareTo(o.purchaseTime); }
}
