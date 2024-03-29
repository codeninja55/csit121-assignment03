package application.controller.validator;
import application.Shop;
public class CardExistsRule implements ExistsRule {
    public boolean existsValidating(FormValidData validData) {
        return Shop.getShopInstance().getDataStore().getCard(validData.getCardID()) != null;
    }
}
