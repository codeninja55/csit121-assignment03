package application.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {
    private static int cardIDCounter = 10000;
    private static int categoryIDCounter = 100;
    private static final Set<Integer> receiptSet = new HashSet<>();

    /*============================== MUTATORS  ==============================*/
    public static int setReceiptID() {
        Random randomObj = new Random();
        int receiptID = randomObj.ints(100000,999999).findAny().orElse(0);
        return (receiptSet.contains(receiptID)) ? setReceiptID() : receiptID;
    }

    public static String setCardID() {
        return "MC" + (++cardIDCounter);
    }

    public static int setCategoryID() {
        return categoryIDCounter++;
    }

    public static void addReceiptID(int receiptID) { receiptSet.add(receiptID); }

    public static void updateCategoryIDCounter() { categoryIDCounter++; }

    public static void updateCardIDCounter() { ++cardIDCounter; }

    /*============================== ACCESSORS  ==============================*/
    public static int getNextCategoryID() {
        return categoryIDCounter;
    }

    public static String getNextCardID() {
        return "MC" + (cardIDCounter + 1);
    }
}
