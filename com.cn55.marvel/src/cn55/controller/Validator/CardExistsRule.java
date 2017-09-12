package cn55.controller.Validator;

import cn55.model.Database;

public class CardExistsRule implements ExistsRule {
    public int existsValidating(FormValidData validData) {
        // Gets the card HashMap from the DB Instance and check if it exists otherwise it returns
        // a negative number. Anything positive will be the index of the card in the cards ArrayList.
        //return Database.getDBInstance().getCardMap().getOrDefault(validData.getCardID(), -1);

        if (Database.getDBInstance().getCardMap().containsKey(validData.getCardID())) {
            return Database.getDBInstance().getCardMap().get(validData.getCardID());
        } else {
            return -1;
        }
    }
}
