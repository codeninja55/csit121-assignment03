package application.view.builderFactory;
import java.util.EventListener;
public interface DeleteCardListener extends EventListener {
    void formSubmitted(DeleteFormView e);
}