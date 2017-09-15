package cn55.model.PurchaseModel;

import cn55.model.CardModel.CardType;
import cn55.model.CategoryModel.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("SameParameterValue")
public class Purchase {
    private final int receiptID;
    private String cardID;
    private String cardType;
    private final String purchaseTime;
    private final HashMap<Integer, Category> categories;

    /*============================== CONSTRUCTORS ==============================*/
    // Constructor for Cash purchases
    public Purchase(HashMap<Integer, Category> categories, int receiptID) {
        this.receiptID = receiptID;
        this.cardID = null;
        this.cardType = CardType.Cash.getName();
        this.categories = categories;
        this.purchaseTime = setPurchaseTime();
    }

    // Constructor for Card purchases
    public Purchase(String cardID, String cardType, HashMap<Integer, Category> categories, int receiptID) {
        this.receiptID = receiptID;
        this.cardID = cardID;
        this.cardType = cardType;
        this.categories = categories;
        this.purchaseTime = setPurchaseTime();
    }

    // Constructor for importing cards
    public Purchase(String purchaseTime, int receiptID, String cardType, String cardID, HashMap<Integer, Category> categories) {
        this.receiptID = receiptID;
        this.cardID = cardID;
        this.cardType = cardType;
        this.purchaseTime = purchaseTime;
        this.categories = categories;
    }

    /*============================== MUTATORS  ==============================*/

    private String setPurchaseTime() {
        // REF: https://stackoverflow.com/questions/1459656/how-to-get-the-current-time-in-yyyy-mm-dd-hhmisec-millisecond-format-in-java
        // REF: https://docs.oracle.com/javase/tutorial/datetime/overview/design.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
        // REF: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void convertPurchase() {
        this.cardType = CardType.Cash.getName();
        this.cardID = null;
    }

    /*============================== ACCESSORS  ==============================*/
    public int getReceiptID() { return receiptID; }

    public String getCardID() { return cardID; }

    public String getCardType() { return cardType; }

    public HashMap<Integer, Category> getCategories() {
        return categories;
    }

    public String getPurchaseTime() { return purchaseTime; }

    public double getCategoriesTotal() {
        return categories.values().stream()
                .mapToDouble(Category::getAmount)
                .sum();
    }

    public String toString() {
        StringBuilder secondOutput = new StringBuilder();
        String firstOutput = String.format("%n%s%s%n%s%s%n%s%s%n%s%s",
                "Receipt ID: ", receiptID,
                "Card Type: ", cardType,
                "Card ID: ", cardID,
                "Purchase Time: ", purchaseTime);

        categories.forEach((k, v) -> secondOutput.append(String.format("%n%s$%.2f", (v.getName() + ": "), v.getAmount())));
        return firstOutput + secondOutput;
    }
}
