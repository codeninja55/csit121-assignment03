package application.model.card;

import application.model.Generator;

@SuppressWarnings("WeakerAccess")
public abstract class Card implements Comparable<Card> {
    protected final String id;
    protected String cardType;
    protected double points;

    /*============================== CONSTRUCTORS  ==============================*/
    // Default constructor
    Card() {
        this.id = Generator.getCardID();
        this.cardType = null;
        this.points = 0;
    }

    Card(String id, String cardType, double points) {
        this.id = id;
        this.cardType = cardType;
        this.points = points;
    }

    public abstract Card clone(Card o);

    public abstract void calcPoints(double totalAmount);

    /*============================== ACCESSORS  ==============================*/
    public String getID() { return id; }
    public abstract String getCardType();
    public double getPoints() { return points; }
    public abstract String toString();
    @SuppressWarnings("unused")
    public boolean equals(Card other) { return this.id.equals(other.id); }
    public int compareTo(Card o) {
        return this.id.compareTo(o.getID());
    }
}
