/*
package application;

import application.model.DataDAO;
import application.model.Generator;
import application.model.Shop;
import application.model.card.AnonCard;
import application.model.card.BasicCard;
import application.model.card.PremiumCard;
import application.model.category.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("unused")
class TestCode {
    private final DataDAO db;
    private final Shop shop;

    public TestCode(DataDAO db, Shop shop) {
        this.db = db;
        this.shop = shop;
    }

    public void generateDefaultCategories() {
        shop.makeCategory(new Category("Other", "For your general items"));
        shop.makeCategory(new Category("Asgardian Weapons", "Real bash and bling from Asgard"));
        shop.makeCategory(new Category("Weapons", "Never go into battle empty handed"));
        shop.makeCategory(new Category("Stark Industries", "Whatever Tony makes you know it will go boom!"));
        shop.makeCategory(new Category("Tesla Motor Vehicles", "Best we like to save the planet when we drive"));
        shop.makeCategory(new Category("Google Pixel", "Nobody wants an inferior phone"));
    }

    @SuppressWarnings("ConstantConditions")
    private Double generateRandomValue() {
        Random randomValueObj = new Random();
        ArrayList<Double> valueOptions = new ArrayList<>();
        valueOptions.add(0D);
        valueOptions.add(10D);
        valueOptions.add(20D);
        valueOptions.add(30D);
        valueOptions.add(40D);
        valueOptions.add(50D);
        valueOptions.add(60D);
        valueOptions.add(70D);
        valueOptions.add(80D);
        valueOptions.add(90D);
        valueOptions.add(100D);
        valueOptions.add(200D);

        return valueOptions.get(randomValueObj.nextInt(valueOptions.size()));
    }

    private HashMap<Integer, Category> generateRandomCategoriesMap() {
        HashMap<Integer, Category> testingCategoryMap = new HashMap<>();
        shop.getDataStore().getAllCategories().values().forEach(cat -> testingCategoryMap.put(cat.getId(), new Category(cat)));
        testingCategoryMap.forEach((k,v) -> v.setAmount(generateRandomValue()));

        return testingCategoryMap;
    }

    private void testMakePurchases(int numOfPurchases, String id) {
        for (int i = 0; i < numOfPurchases; i++)
            shop.makePurchase(id, Generator.getReceiptID(), generateRandomCategoriesMap());
    }

    public void createTestCode() {

        db.createCard(new BasicCard("Natasha Romanov",
                "blackwidow@avengers.team", 0));
        testMakePurchases(5,"MC10003");

        testMakePurchases(5, "Cash");

        db.createCard(new AnonCard()); // MC10001
        testMakePurchases(1, "MC10001");

        db.createCard(new AnonCard()); // MC10002
        testMakePurchases(2, "MC10002");

        testMakePurchases(3, "Cash");

        db.createCard(new BasicCard("Steve Rogers",
                "captain_a@avengers.team",0D));
        testMakePurchases(2, "MC10004");

        testMakePurchases(2, "Cash");

        testMakePurchases(5,"MC10003");
        db.createCard(new PremiumCard("Tony Stark",
                "ironman@avengers.team",0));
        testMakePurchases(8,"MC10005");
        testMakePurchases(2, "Cash");
        db.createCard(new BasicCard("Bruce Banner", "hulk@avengers.team", 0D));
        testMakePurchases(2,"MC10006");
        db.createCard(new PremiumCard("Nick Fury","nick@shield.com",0));

        testMakePurchases(3, "MC10007");

        db.createCard(new AnonCard()); //MC10008
        testMakePurchases(2,"MC10008");

        db.createCard(new AnonCard()); //MC10009

        db.createCard(new BasicCard("Hank Pym","ants@avengers.team",0)); // MC10009
        db.createCard(new BasicCard("Peter Parker", "spidey@avengers.team",0)); // MC10010
        db.createCard(new PremiumCard("Danny Rand","danny@randcorp.com",5000)); // MC10011
        testMakePurchases(3, "MC10012");
        db.createCard(new PremiumCard("Professor Charles Xavier", "x@xmen.com", 1000)); // MC100012
        testMakePurchases(2,"MC10013");

        db.createCard(new BasicCard("Matthew Murdock","thedevil@hellskitchen.com", 666));
        db.createCard(new BasicCard("Thor Odinson", "thor@asgard.com",9000));

        testMakePurchases(2,"MC10005");
        shop.makePurchase("Cash", Generator.getReceiptID(),
                generateRandomCategoriesMap());

        testMakePurchases(2,"MC10016");
        testMakePurchases(2,"MC10005");
        testMakePurchases(2,"MC10019");

        shop.makePurchase("Cash", Generator.getReceiptID(),
                generateRandomCategoriesMap());

        db.createCard(new BasicCard("Clint Barton", "better_than_arrow@marvel.com", 500));
        db.createCard(new AnonCard());
        testMakePurchases(2,"MC10017");
        db.createCard(new PremiumCard("Oliver Queen", "the_best_arrow_user@dc.comics", 1500));
        testMakePurchases(3,"MC10018");
        db.createCard(new BasicCard("Loki", "the-baddest-dude@asgard.com", 0));
        testMakePurchases(2,"MC10017");
        db.createCard(new AnonCard());
        db.createCard(new AnonCard());
        db.createCard(new PremiumCard("Pepper Potts", "ceo@starkindustries.com", 5000));
        testMakePurchases(1,"MC10005");
        db.createCard(new AnonCard());
        db.createCard(new AnonCard());
        db.createCard(new PremiumCard("King T'Challa", "tokenman@wakanda.africa", 0));
        db.createCard(new AnonCard());
        db.createCard(new BasicCard("Logan", "wolverine@xmen.com", 200));
        db.createCard(new AnonCard());
        db.createCard(new AnonCard());
        db.createCard(new BasicCard("Luke Cage", "unbreakable@defenders.marvel", 0));

    }
}
*/
