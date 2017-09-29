package application.model.card;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AdvancedCard extends Card {
    protected final String name;
    protected final String email;
    protected double balance;

    AdvancedCard (String name, String email) {
        super ();
        this.name = name;
        this.email = email;
        this.balance = 0;
    }

    AdvancedCard(String id, String name, String email,double points, double balance) {
        super(id, null, points);
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public abstract Card clone(Card o);
    public String getCardType() { return super.cardType; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public boolean equals(AdvancedCard other) { return this.name.equals(other.name); }
}
