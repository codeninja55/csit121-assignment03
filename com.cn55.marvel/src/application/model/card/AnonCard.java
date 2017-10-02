package application.model.card;

import java.time.Instant;

public class AnonCard extends Card {
    private static final double POINTS_RATE = 0.01;

    /*============================== CONSTRUCTORS  ==============================*/
    public AnonCard() {
        super();
        super.cardType = CardType.AnonCard.name;
        super.points = 0;
    }

    public AnonCard(String id, double points) {
        super(id, CardType.AnonCard.name, points);
    }

    public Card clone(Card o) {
        return new AnonCard(o.getID(), o.getPoints());
    }

    /*============================== MUTATORS  ==============================*/
    public void calcPoints(double totalAmount) {
        this.points += POINTS_RATE * totalAmount;
    }

    /*============================== ACCESSORS  ==============================*/
    public String getID() { return id; }
    public String getCardType() { return super.cardType; }
    public String toString() {
        return String.format("%n%s: %s%n%s: %s%n%s: %.2f%n",
                "Card Type", this.cardType,
                "Card ID", super.id,
                "Points", super.points);
    }
    public String toStringDelim() {
        return String.format("%s,%s,%s,,,%.4f,", Instant.now().toString(), id, cardType, points);
    }
}
