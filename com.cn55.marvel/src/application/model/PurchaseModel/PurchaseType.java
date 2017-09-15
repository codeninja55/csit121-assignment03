package application.model.PurchaseModel;

public enum PurchaseType {
    // Constants
    ExistingCardPurchase(1, "Purchase with Existing Card"),
    NewCardPurchase(2, "Purchase with New Card"),
    CashPurchase(3, "Cash Purchase")
    ;

    // Instance Variables
    private final int id;
    private final String name;

    PurchaseType(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
