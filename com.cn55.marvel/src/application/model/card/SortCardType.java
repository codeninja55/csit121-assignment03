package application.model.card;

public enum SortCardType {
    // Constants
    CreatedOrder(1, "Sort by Created Order"),
    ReverseCreatedOrder(2, "Sort by Descending Created Order"),
    Name(3, "Sort by Name"),
    Points(4, "Sort by Points")
    ;

    private final String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int id;

    SortCardType(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
