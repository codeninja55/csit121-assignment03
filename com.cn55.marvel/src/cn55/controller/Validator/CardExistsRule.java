package cn55.controller.Validator;

import cn55.model.DataStoreModel;

public class CardExistsRule implements ExistsRule {
    public int existsValidating(FormValidData validData) {
        // Gets the card HashMap from the DB Instance and check if it exists otherwise it returns
        // a negative number. Anything positive will be the index of the card in the cards ArrayList.
        //return PersistentData.getDataStoreInstance().getCardMap().getOrDefault(validData.getCardID(), -1);

        if (DataStoreModel.getDataStoreInstance().getCardMap().containsKey(validData.getCardID())) {
            return DataStoreModel.getDataStoreInstance().getCardMap().get(validData.getCardID());
        } else {
            return -1;
        }
    }
}
