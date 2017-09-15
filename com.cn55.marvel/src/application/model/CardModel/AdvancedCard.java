package application.model.CardModel;

@SuppressWarnings("unused")
public abstract class AdvancedCard extends Card {
    String name;
    String email;
    double balance;

    AdvancedCard (String name, String email) {
        super ();
        this.name = name;
        this.email = email;
        this.balance = 0;
    }

    AdvancedCard(String id, String name, String email,double points, double balance) {
        super(id, points);
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public boolean equals (AdvancedCard other) { return this.name.equals(other.name); }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
}
