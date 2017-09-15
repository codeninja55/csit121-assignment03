package cn55.model;

import cn55.model.CardModel.AnonCard;
import cn55.model.CardModel.BasicCard;
import cn55.model.CardModel.PremiumCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TestCode {

    private Shop shop;
    private DataStoreModel db;

    public TestCode(Shop shop) {
        this.shop = shop;
        this.db = shop.getDataStore();
    }

    /* TODO - REMOVE TEST CODE */
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

    /* TODO - REMOVE TEST CODE */
    private HashMap<Integer, Category> generateRandomCategoriesMap() {
        HashMap<Integer, Category> testingCategoryMap = new HashMap<>();
        db.getCategories().forEach((cat) -> testingCategoryMap.put(cat.getId(), new Category(cat)));
        testingCategoryMap.forEach((k,v)->v.setAmount(generateRandomValue()));

        return testingCategoryMap;
    }

    /* TODO - REMOVE TEST CODE */
    private void testMakePurchases(int numOfPurchases, String id) {
        for (int i = 0; i < numOfPurchases; i++)
            shop.makePurchase(id, Shop.generateReceiptID(), generateRandomCategoriesMap());
    }

    /* TODO - REMOVE TEST CODE */
    private void createTestCode() {

        // Cash purchase test
        testMakePurchases(4, "Cash");

        // AnonCard Test
        db.addCards(new AnonCard()); // MC10001
        testMakePurchases(1, "MC10001");

        db.addCards(new AnonCard()); // MC10002
        testMakePurchases(2, "MC10002");

        // BasicCard Test
        db.addCards(new BasicCard("Natasha Romanov",
                "blackwidow@avengers.team", 0));
        testMakePurchases(2,"MC10003");

        // More Cash purchases
        testMakePurchases(3, "Cash");

        // BasicCard Test 2
        db.addCards(new BasicCard("Steve Rogers",
                "captain_a@avengers.team",0D));
        testMakePurchases(2, "MC10004");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // More Romanov purchases
        testMakePurchases(5,"MC10003");

        // PremiumCard Test
        db.addCards(new PremiumCard("Tony Stark",
                "ironman@avengers.team",0));

        testMakePurchases(5,"MC10005");

        // More Cash purchases
        testMakePurchases(2, "Cash");

        // BasicCard Test 2
        db.addCards(new BasicCard("Bruce Banner", "hulk@avengers.team", 0D));
        testMakePurchases(2,"MC10006");

        // PremiumCard Test 2
        db.addCards(new PremiumCard("Nick Fury",
                "nick@shield.com",0));

        testMakePurchases(3, "MC10007");

        db.addCards(new AnonCard()); //MC10008
        testMakePurchases(2,"MC10008");

        db.addCards(new AnonCard()); //MC10009
        db.getCards().get(7).calcPoints(300D);

        db.addCards(new BasicCard("Hank Pym","ants@avengers.team",0)); // MC10009
        db.addCards(new BasicCard("Peter Parker", "spidey@avengers.team",0)); // MC10010
        db.addCards(new PremiumCard("Danny Rand","danny@randcorp.com",5000)); // MC10011
        testMakePurchases(3, "MC10012");
        db.addCards(new PremiumCard("Professor Charles Xavier", "x@xmen.com", 1238798)); // MC100012
        testMakePurchases(2,"MC10013");

        db.addCards(new BasicCard("Matthew Murdock","thedevil@hellskitchen.com", 666));
        db.addCards(new BasicCard("Thor Odinson", "thor@asgard.com",9000));

        testMakePurchases(2,"MC10005");
        shop.makePurchase("Cash", Shop.generateReceiptID(),
                generateRandomCategoriesMap());

        testMakePurchases(2,"MC10016");
        testMakePurchases(2,"MC10005");
        testMakePurchases(2,"MC10019");

        shop.makePurchase("Cash", Shop.generateReceiptID(),
                generateRandomCategoriesMap());

        db.addCards(new BasicCard("Clint Barton", "better_than_arrow@marvel.com", 500));
        db.addCards(new AnonCard());
        testMakePurchases(2,"MC10017");
        db.addCards(new PremiumCard("Oliver Queen", "the_best_arrow_user@dc.comics", 1500));
        testMakePurchases(3,"MC10018");
        db.addCards(new BasicCard("Loki", "the-baddest-dude@asgard.com", 0));
        testMakePurchases(2,"MC10017");
        db.addCards(new AnonCard());
        db.addCards(new AnonCard());
        db.addCards(new PremiumCard("Pepper Potts", "ceo@starkindustries.com", 1987234));
        testMakePurchases(1,"MC10005");
        db.addCards(new AnonCard());
        db.addCards(new AnonCard());
        db.addCards(new PremiumCard("King T'Challa", "tokenman@wakanda.africa", 105023));
        db.addCards(new AnonCard());
        db.addCards(new BasicCard("Logan", "wolverine@xmen.com", 0));
    }

    /* TODO - REMOVE TEST CODE */
    private void createTooManyCategories() {
        for (int i = 0; i < 35; i++)
            db.addCategory(new Category(String.format("%s%d","Testing", i)));
    }

}
