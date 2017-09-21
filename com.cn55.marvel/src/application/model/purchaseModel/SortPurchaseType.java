package application.model.purchaseModel;

@SuppressWarnings("FieldCanBeLocal")
public enum SortPurchaseType {
    All(1, "All Purchases"),
    Card(2, "Purchases with Cards"),
    Cash(3, "Purchases Cash")
    ;

    private final String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int id;

    SortPurchaseType(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}