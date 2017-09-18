package application.model.CardModel;

import application.model.Generator;

@SuppressWarnings("ALL")
public abstract class Card implements Comparable<Card> {

    String id;
    double points;
    String cardType;

    /*============================== CONSTRUCTORS  ==============================*/
    // Default constructor
    Card() {
        this.id = Generator.setCardID();
        this.points = 0;
        this.cardType = null;
    }

    Card(String id, double points) {
        this.id = id;
        this.points = points;
        this.cardType = null;
    }

    /*============================== MUTATORS  ==============================*/
    public void setPoints(double points) {
        this.points = points;
    }

    // Abstract method to force implementation in all subclasses
    public abstract void calcPoints(double totalAmount);

    public void calcBalance(double totalAmount) {}

    /*============================== ACCESSORS  ==============================*/
    public String getCardType() { return cardType; }
    public String getID() { return id; }
    public double getPoints() { return points; }
    public abstract String toString();
    public boolean equals(Card other) {
        return this.id.equals(other.id);
    }
    public int compareTo(Card o) {
        return this.id.compareTo(o.getID());
    }
}
