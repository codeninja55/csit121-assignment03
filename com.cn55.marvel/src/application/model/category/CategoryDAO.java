package application.model.category;

import java.util.HashMap;

@SuppressWarnings("All")
public interface CategoryDAO {
    void createCategory(Category category);
    HashMap<Integer, Category> getAllCategories();
    Category getCategory(int id);
    void updateCategoryTotalAmount(HashMap<Integer,Category> purchaseCategoriesMap);
    void deleteCategory(int id);
}