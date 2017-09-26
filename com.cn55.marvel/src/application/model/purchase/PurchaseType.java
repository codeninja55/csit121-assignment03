package application.model.purchase;

public enum PurchaseType {
    ExistingCardPurchase(1, "Purchase with Existing Card"),
    NewCardPurchase(2, "Purchase with New Card"),
    CashPurchase(3, "Cash Purchase")
    ;

    private final String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int id;

    PurchaseType(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
