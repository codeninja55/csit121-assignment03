package cn55.model.CardModel;

/*
 * @author Dinh Che
 * Student Number: 5721970
 * Email: dbac496@uowmail.edu.au
 */

import cn55.model.Database;

@SuppressWarnings("ALL")
public abstract class Card implements Comparable<Card> {

    String id;
    double points;
    String cardType;

    /*============================== CONSTRUCTORS  ==============================*/
    // Default constructor
    Card() {
        this.id = Database.generateCardID();
        this.points = 0;
        this.cardType = null;
    }

    /*============================== MUTATORS  ==============================*/
    /*Abstract method to force implementation in all subclasses*/
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

    @Override
    public int compareTo(Card o) {
        return this.id.compareTo(o.getID());
    }
}
