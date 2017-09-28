package application.model.card;

@SuppressWarnings("FieldCanBeLocal")
public class BasicCard extends AdvancedCard {
    private final double POINTS_RATE = 0.015;

    /*============================== CONSTRUCTORS  ==============================*/
    public BasicCard(String name, String email) {
        super(name, email);
        super.cardType = CardType.BasicCard.getName();
    }

    // Constructor with for new cards without purchase
    @SuppressWarnings("unused")
    public BasicCard(String name, String email, double totalAmount) {
        super(name, email);
        super.cardType = CardType.BasicCard.getName();
        super.balance = totalAmount;
        calcPoints(totalAmount);
    }

    BasicCard(String id, String name, String email, double points, double balance) {
        super(id, name, email, points, balance);
        super.cardType = CardType.BasicCard.getName();
    }

    public Card clone(Card o) {
        return new BasicCard(o.getID(), ((BasicCard)o).getName(), ((BasicCard)o).getEmail(), o.getPoints(), ((BasicCard) o).getBalance());
    }

    /*============================== MUTATORS  ==============================*/
    public void calcPoints(double totalAmount) { super.points += POINTS_RATE * totalAmount; }

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
