package application.model.card;

import java.time.Instant;

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
    public abstract void calcBalance(double totalAmount);
    public String getCardType() { return super.cardType; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public String toStringDelim() {
        return String.format("%s,%s,%s,%s,%s,%.4f,%.4f", Instant.now().toString(), id, cardType, name, email, points, balance);
    }

    public boolean equals(AdvancedCard other) { return this.name.equals(other.name); }
}
