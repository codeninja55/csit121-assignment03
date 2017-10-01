package application.model.dao;

import application.model.category.Category;

import java.util.HashMap;

@SuppressWarnings("All")
public interface CategoryDAO {
    void createCategory(Category category);
    HashMap<Integer, Category> getOrigCategoriesMap();
    Category getCategory(int id);
    void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap);
    void deleteCategory(int id);
}
