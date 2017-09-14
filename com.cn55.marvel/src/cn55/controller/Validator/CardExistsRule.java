package cn55.controller.Validator;

import cn55.model.DataStoreModel;
import cn55.model.Shop;

public class CardExistsRule implements ExistsRule {
    public int existsValidating(FormValidData validData) {
        // Gets the card HashMap from the DB Instance and check if it exists otherwise it returns
        // a negative number. Anything positive will be the index of the card in the cards ArrayList.

        return Shop.getShopInstance().getDataStore().getCardMap().getOrDefault(validData.getCardID(), -1);
    }
}
