package application.view.builderFactory;
import java.util.EventListener;
public interface SearchListener extends EventListener {
    void searchFormSubmitted(SearchFormView e);
}
