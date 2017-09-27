package application.view.form_builder_factory;
import java.util.EventListener;
public interface CategoryListener extends EventListener {
    void createCategoryEventOccurred(CategoryFormView e);
}
