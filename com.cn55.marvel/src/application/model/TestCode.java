package application.model;

import application.model.CardModel.AnonCard;
import application.model.CardModel.BasicCard;
import application.model.CardModel.PremiumCard;
import application.model.CategoryModel.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TestCode {

    private Shop shop;
    private DataDAO db;

    public TestCode(Shop shop) {
        this.shop = shop;
        this.db = shop.getDataStore();
        createTestCode();
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
        valueOptions.add(300D);
        valueOptions.add(400D);
        valueOptions.add(500D);

        return valueOptions.get(randomValueObj.nextInt(valueOptions.size()));
    }

    private HashMap<Integer, Category> generateRandomCategoriesMap() {
        HashMap<Integer, Category> testingCategoryMap = new HashMap<>();
        db.getAllCategories().values().forEach((cat) -> testingCategoryMap.put(cat.getId(), new Category(cat)));
        testingCategoryMap.forEach((k,v)->v.setAmount(generateRandomValue()));

        return testingCategoryMap;
    }

    private void testMakePurchases(int numOfPurchases, String id) {
        for (int i = 0; i < numOfPurchases; i++)
            shop.makePurchase(id, Generator.setReceiptID(), generateRandomCategoriesMap());
    }

    private void createTestCode() {

        // Cash purchase test
        testMakePurchases(4, "Cash");

        // AnonCard Test
        db.createCard(new AnonCard()); // MC10001
        testMakePurchases(1, "MC10001");

        db.createCard(new AnonCard()); // MC10002
        testMakePurchases(2, "MC10002");

        // BasicCard Test
        db.createCard(new BasicCard("Natasha Romanov",
                "blackwidow@avengers.team", 0));
        testMakePurchases(2,"MC10003");

        // More Cash purchases
        testMakePurchases(3, "Cash");

        // BasicCard Test 2
        db.createCard(new BasicCard("Steve Rogers",
                "captain_a@avengers.team",0D));
        testMakePurchases(2, "MC10004");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // More Romanov purchases
        testMakePurchases(5,"MC10003");

        // PremiumCard Test
        db.createCard(new PremiumCard("Tony Stark",
                "ironman@avengers.team",0));

        testMakePurchases(5,"MC10005");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // BasicCard Test 2
        db.createCard(new BasicCard("Bruce Banner", "hulk@avengers.team", 0D));
        testMakePurchases(2,"MC10006");

        // PremiumCard Test 2
        db.createCard(new PremiumCard("Nick Fury",
                "nick@shield.com",0));

        testMakePurchases(3, "MC10007");

        db.createCard(new AnonCard()); //MC10008
        testMakePurchases(2,"MC10008");
        db.createCard(new AnonCard()); //MC10009
        db.createCard(new BasicCard("Hank Pym","ants@avengers.team",0)); // MC10009
        db.createCard(new BasicCard("Peter Parker", "spidey@avengers.team",0)); // MC10010
        db.createCard(new PremiumCard("Danny Rand","danny@randcorp.com",5000)); // MC10011
        testMakePurchases(3, "MC10012");
        db.createCard(new PremiumCard("Professor Charles Xavier", "x@xmen.com", 0)); // MC100012
        testMakePurchases(2,"MC10013");

        db.createCard(new BasicCard("Matthew Murdock","thedevil@hellskitchen.com", 666));
        db.createCard(new BasicCard("Thor Odinson", "thor@asgard.com",9000));

        testMakePurchases(4,"MC10005");
        testMakePurchases(3, "Cash");
        testMakePurchases(2,"MC10016");
        testMakePurchases(2,"MC10005");
        testMakePurchases(2,"MC10019");
        testMakePurchases(2,"Cash");

        db.createCard(new BasicCard("Clint Barton", "better_than_arrow@marvel.com", 500));
        db.createCard(new AnonCard());
        testMakePurchases(2,"MC10017");
        db.createCard(new PremiumCard("Oliver Queen", "the_best_arrow_user@dc.comics", 1500));
        testMakePurchases(3,"MC10018");
        db.createCard(new BasicCard("Loki", "the-baddest-dude@asgard.com", 0));
        testMakePurchases(2,"MC10017");
        db.createCard(new AnonCard());
        db.createCard(new AnonCard());
        db.createCard(new PremiumCard("Pepper Potts", "ceo@starkindustries.com", 0));
        testMakePurchases(1,"MC10005");
        db.createCard(new AnonCard());
        db.createCard(new AnonCard());
        db.createCard(new PremiumCard("King T'Challa", "tokenman@wakanda.africa", 1050));
        db.createCard(new AnonCard());
        db.createCard(new BasicCard("Logan", "wolverine@xmen.com", 0));
    }

    private void createTooManyCategories() {
        for (int i = 0; i < 35; i++)
            db.createCategory(new Category(String.format("%s%d","Testing", i)));
    }

}
