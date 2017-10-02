package application.model.category;

import application.model.Generator;

import java.time.Instant;

public class Category implements Comparable<Category> {

    private final int id;
    private final String name;
    private final String description;
    private double amount;
    private double totalAmount;

    /*============================== CONSTRUCTORS  ==============================*/
    public Category(String name, String description) {
        this.id = Generator.getCategoryID();
        this.name = name;
        this.description = description;
        this.amount = 0D;
        this.totalAmount = 0D;
    }

    public Category(int id, String name, String description, double amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.totalAmount = 0D;
    }

    /*============================== COPY CONSTRUCTOR ==============================*/
    public Category(Category other) {
        this(other.getId(), other.getName(), other.getDescription(), other.getAmount());
    }

    public Category(Category other, double totalAmount) {
        this(other.getId(), other.getName(), other.getDescription(), other.getAmount());
        this.totalAmount = totalAmount;
    }

    /*============================== MUTATORS  ==============================*/
    public void setAmount(double amount) { this.amount = amount; }

    public void updateTotalAmount(double amount) { this.totalAmount += amount; }

    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    /*============================== ACCESSORS  ==============================*/
    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public double getAmount() { return amount; }

    public double getTotalAmount() { return totalAmount; }

    public String toString() {
        return String.format("%n%s : $%.2f", name, amount);
    }

    public String toStringDelim() {
        return String.format("%s,%s,%s,%s,%.2f", Instant.now().toString(),id,name,description,amount);
    }

    public int compareTo(Category o) {
        /* ASCENDING ORDER */
        return this.getName().compareTo(o.getName());
        /* DESCENDING ORDER */
        //return o.getName().compareTo(this.getName());
    }
}
