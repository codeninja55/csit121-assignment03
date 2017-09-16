package application.view.FormFactory;

import java.util.EventListener;

public interface PurchaseListener extends EventListener{
    void formActionOccurred(PurchaseEvent event);
}
