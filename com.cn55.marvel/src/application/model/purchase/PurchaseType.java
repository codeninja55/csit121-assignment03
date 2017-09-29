package application.model.purchase;
public enum PurchaseType {
    ExistingCardPurchase("Purchase with Existing Card"),
    NewCardPurchase("Purchase with New Card"),
    CashPurchase("Cash Purchase")
    ;
    public final String name;
    PurchaseType(String name) { this.name = name; }
    public String toString() { return name; }
}
