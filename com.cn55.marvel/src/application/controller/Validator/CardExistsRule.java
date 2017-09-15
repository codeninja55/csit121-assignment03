package application.controller.Validator;

import application.model.Shop;

public class CardExistsRule implements ExistsRule {
    public int existsValidating(FormValidData validData) {
        // Gets the card HashMap from the DB Instance and check if it exists otherwise it returns
        // a negative number. Anything positive will be the index of the card in the cards ArrayList.

        return Shop.getShopInstance().getDataStore().getCardMap().getOrDefault(validData.getCardID(), -1);
    }
}
