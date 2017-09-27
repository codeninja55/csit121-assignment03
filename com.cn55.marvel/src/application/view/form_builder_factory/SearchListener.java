package application.view.form_builder_factory;
import java.util.EventListener;
public interface SearchListener extends EventListener {
    void searchFormSubmitted(SearchFormView e);
}
