package application.controller.validator;
import application.Shop;
public class CategoryExistsRule implements ExistsRule {
    public boolean existsValidating(FormValidData validData) {
        return Shop.getShopInstance().getDataStore().getCategory(validData.getCategoryID()) != null;
    }
}
