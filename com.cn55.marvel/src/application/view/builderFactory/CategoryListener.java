package application.view.builderFactory;
import java.util.EventListener;
public interface CategoryListener extends EventListener {
    void createCategoryEventOccurred(CategoryFormView e);
}
