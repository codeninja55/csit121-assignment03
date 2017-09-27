package application.view.form_builder_factory;
import java.util.EventListener;
public interface CardListener extends EventListener {
    void formActionOccurred(CardFormView e);
}
