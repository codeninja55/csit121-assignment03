package application.model.purchase;
public enum SortPurchaseType {
    All("All Purchases"),
    Card("Purchases with Cards"),
    Cash("Purchases Cash")
    ;
    public final String name;
    SortPurchaseType(String name) { this.name = name; }
    public String toString() { return name; }
}