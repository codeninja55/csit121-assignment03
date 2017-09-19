package application.view.builderFactory;

import java.util.EventListener;

public interface DeleteListener extends EventListener {
    void formSubmitted(DeleteCardForm e);
}