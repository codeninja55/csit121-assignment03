package application.model;

import java.util.*;

public class GenereteID {
    private static int cardIDCounter = 10000;
    private static int categoryIDCounter = 100;
    private static final Set<Integer> receiptSet = new HashSet<>();

    /*============================== MUTATORS  ==============================*/
    public static int setReceiptID() {
        Random randomObj = new Random();

        int receiptID;
        receiptID = randomObj.ints(10000000,99999999).findFirst().getAsInt();

        if (receiptSet.contains(receiptID)) {
            return setReceiptID();
        } else {
            addReceiptID(receiptID);
            return receiptID;
        }
    }

    public static String setCardID() {
        return "MC" + (++cardIDCounter);
    }

    public static int setCategoryID() {
        return categoryIDCounter++;
    }

    private static void addReceiptID(int receiptID) { receiptSet.add(receiptID); }

    /*============================== ACCESSORS  ==============================*/
    public static int getNextCategoryID() {
        return categoryIDCounter;
    }

    public static String getNextCardID() {
        return "MC" + (cardIDCounter + 1);
    }
}