package cn55.model.CardModel;

import cn55.model.CardType;

public class PremiumCard extends AdvancedCard {
    private final double POINTS_RATE_LOW = 0.025;
    private final double POINTS_RATE_HIGH = 0.03;
    @SuppressWarnings("unused")
    private final double SIGNUP_FEE = 25.0;

    /*============================== CONSTRUCTORS  ==============================*/
    public PremiumCard(String name, String email) {
        super(name, email);
        super.cardType = CardType.PremiumCard.getName();
    }

    // constructor with details
    public PremiumCard(String name, String email, double totalAmount) {
        super(name, email);
        super.cardType = CardType.PremiumCard.getName();
        super.balance = totalAmount; //- SIGNUP_FEE; - we were told to ignore this
    }

    /*============================== MUTATORS  ==============================*/
    public void calcPoints(double totalAmount) {
        if (totalAmount < 40 && this.balance < 1000)
            this.points += totalAmount * POINTS_RATE_LOW;
        else
            this.points += totalAmount * POINTS_RATE_HIGH;
    }

    public void calcBalance(double totalAmount) { this.balance += totalAmount; }

    /*============================== ACCESSORS  ==============================*/
    public String toString() {
        return String.format("%n%s: %s%n%s: %s%n%s: %.2f%n%s: $%.2f%n%s: %s%n%s: %s%n",
                "Card Type",this.cardType,
                "Card ID",this.id,
                "Points",this.points,
                "Balance",this.balance,
                "Customer Name",this.name,
                "Customer Email",this.email);
    }
}
