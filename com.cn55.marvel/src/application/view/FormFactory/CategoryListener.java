package application.view.FormFactory;

import java.util.EventListener;

public interface CategoryListener extends EventListener {
    void createCategoryEventOccurred(CategoryEvent e);
}
