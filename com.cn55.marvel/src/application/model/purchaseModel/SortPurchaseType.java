package application.model.purchaseModel;

public enum SortPurchaseType {
    // Constants
    All(1, "All Purchases"),
    Card(2, "Purchases with Cards"),
    Cash(3, "Purchases Cash")
    ;

    // Instance Variables
    private final int id;
    private final String name;

    SortPurchaseType(int id, String name) {
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