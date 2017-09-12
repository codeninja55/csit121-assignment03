package cn55.view.CategoriesView;

import java.util.EventListener;


public interface CategoryListener extends EventListener {
    void createCategoryEventOccurred(CategoryEvent e);
}
