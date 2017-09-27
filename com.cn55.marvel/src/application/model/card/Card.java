package application.model.card;

import application.model.Generator;

public abstract class Card implements Comparable<Card> {
    final String id;
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

    Card(Card o) {
        this.id = o.getID();
        this.points = o.getPoints();
        this.cardType = o.getCardType();
    }

    /*============================== MUTATORS  ==============================*/
    void setPoints(double points) {
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
    @SuppressWarnings("unused")
    public boolean equals(Card other) { return this.id.equals(other.id); }
    public int compareTo(Card o) {
        return this.id.compareTo(o.getID());
    }
}
