package cn55.model;

public enum CardType {

    // Declare constants
    Cash("Cash", 0),
    AnonCard("AnonCard", 1),
    BasicCard("BasicCard", 2),
    PremiumCard("PremiumCard", 3)
    ;

    // Instance Variables
    private final int id;
    private final String name;

    CardType(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id; }

    public String getName() {
        return name;
    }
}
