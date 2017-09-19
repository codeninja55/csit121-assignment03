package application.view.builderFactory;
import java.util.EventListener;
public interface CardListener extends EventListener {
    void formActionOccurred(CardFormView e);
}
