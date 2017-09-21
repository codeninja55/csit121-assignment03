package application.model.cardModel;

public enum CardType {
    Cash("Cash", 0),
    AnonCard("AnonCard", 1),
    BasicCard("BasicCard", 2),
    PremiumCard("PremiumCard", 3)
    ;

    private final String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int id;

    CardType(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
