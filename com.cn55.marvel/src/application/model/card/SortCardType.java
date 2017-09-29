package application.model.card;
public enum SortCardType {
    CreatedOrder("Sort by Created Order"),
    ReverseCreatedOrder("Sort by Descending Created Order"),
    Name("Sort by Name"),
    Points("Sort by Points")
    ;
    public final String name;
    SortCardType(String name) {
        this.name = name;
    }
    public String toString() { return name; }
}
