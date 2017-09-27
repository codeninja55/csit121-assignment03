package application.view.form_builder_factory;

import java.util.EventListener;

public interface PurchaseListener extends EventListener{
    void formSubmitted(PurchaseFormView view);
}
