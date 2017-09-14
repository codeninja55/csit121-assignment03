package cn55.controller.Validator;

import cn55.model.DataStoreModel;
import cn55.model.Shop;

public class CategoryExistsRule implements ExistsRule {
    public int existsValidating(FormValidData validData) {
        int categoryID;
        String categoryIDStr = validData.getCategoryID();
        FormValidData input = new FormValidData();
        input.setCategoryID(categoryIDStr);
        FormRule validIDRule = new CategoryIDRule();

        if (validIDRule.validate(input)) {
            categoryID = Integer.parseInt(categoryIDStr);
            return Shop.getShopInstance().getDataStore().getCategoriesMap().getOrDefault(categoryID, -1);
        } else {
            return -1;
        }
    }
}
