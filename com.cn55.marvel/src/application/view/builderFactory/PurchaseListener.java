package application.view.builderFactory;

import java.util.EventListener;

public interface PurchaseListener extends EventListener{
    void formSubmitted(PurchaseFormView view);
}
