package application.model.card;
public enum CardType {
    Cash("Cash"),
    AnonCard("AnonCard"),
    BasicCard("BasicCard"),
    PremiumCard("PremiumCard")
    ;
    public final String name;
    CardType(String name) { this.name = name; }
    public String toString() { return name; }
    public boolean equalsName(String other) { return name.equals(other); }
}
