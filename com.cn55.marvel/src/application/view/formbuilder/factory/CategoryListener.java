package application.view.formbuilder.factory;
import java.util.EventListener;
public interface CategoryListener extends EventListener {
    void createCategoryEventOccurred(CategoryFormView e);
}
