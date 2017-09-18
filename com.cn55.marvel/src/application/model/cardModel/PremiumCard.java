package application.model.cardModel;

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
        calcPoints(totalAmount);
    }

    PremiumCard(String id, String name, String email, double points, double balance) {
        super(id, name, email, points, balance);
        super.cardType = CardType.PremiumCard.getName();
    }

    /*============================== MUTATORS  ==============================*/
    public void calcPoints(double totalAmount) {
        if (totalAmount < 40 && super.balance < 1000)
            super.points += totalAmount * POINTS_RATE_LOW;
        else
            super.points += totalAmount * POINTS_RATE_HIGH;
    }

    public void calcBalance(double totalAmount) { super.balance += totalAmount; }

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
