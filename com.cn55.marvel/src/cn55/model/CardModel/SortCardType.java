package cn55.model.CardModel;

public enum SortCardType {
    // Constants
    CreatedOrder(1, "Sort by Created Order"),
    ReverseCreatedOrder(2, "Sort by Descending Created Order"),
    Name(3, "Sort by Name"),
    Points(4, "Sort by Points")
    ;

    // Instance Variables
    private final int id;
    private final String name;

    SortCardType(int id, String name) {
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
