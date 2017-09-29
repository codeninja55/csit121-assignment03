package application.view.formbuilder.factory;

import java.util.EventListener;

public interface PurchaseListener extends EventListener{
    void formSubmitted(PurchaseFormView view);
}
